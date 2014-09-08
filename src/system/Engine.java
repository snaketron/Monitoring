package system;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.DatabaseRoutine;
import msg.DataMsg;
import msg.MessageParser;
import msg.NetworkMsg;
import system.connectivity.MapKey;
import system.connectivity.MatrixOps;
import system.sympathy.SympathyActionTree;
import system.sympathy.util.ObservationMap;
import system.sympathy.util.SympathyConst;
import system.sympathy.util.SympathyReport;
import system.sympathy.util.SympathyTimer;
import system.sympathy.SympathyObservationTree;
import utils.MsgType;
import utils.event.IDataEvent;
import utils.event.IEngineEvent;
import utils.event.IMoteEvent;
import utils.event.ISympathyEvent;

/**
 * 
 * The most important class which performs the following operations:<br>
 * 	+ it handles the incoming messages and forwards them towards the GUI component in a form 
 * 	  such that they can be displayed for the user. <br>
 *  + it establishes a database connection and inserts the incoming messages to the appropriate 
 *    database table. <br>
 *  + it listens for commands from the GUI to start/stop reading from the USB port.
 *  
 */
public class Engine implements IEngineEvent, ISympathyEvent, Runnable {
	private HashMap<Integer, Mote> motesMap;
	private List<IMoteEvent> moteListeners;
	private List<IDataEvent> dataListeners;
	private UsbReader usb;
	private CoojaReader coojaReader;
	private Connection conn;
	private SympathyTimer sympathy;
	
	private boolean activeUsb = false;
	private boolean activeFile = false;
	private boolean exit = false;

	public Engine() {
		this.motesMap = new HashMap<Integer, Mote>();
		this.moteListeners = new ArrayList<IMoteEvent>();
		this.dataListeners = new ArrayList<IDataEvent>();
		this.usb = new UsbReader();
		this.coojaReader = new CoojaReader();
		this.conn = DatabaseRoutine.connectToDb();
		this.sympathy = new SympathyTimer();
		this.sympathy.addSympathyListeners(this);
	}
	
	
    public void addMoteListeners(IMoteEvent me) {
    	moteListeners.add(me);
    }
    
    
    public void addDataListeners(IDataEvent de) {
    	dataListeners.add(de);
    }
    
    
    private void processNetworkMessage(NetworkMsg nm) {
    	if(!motesMap.containsKey(nm.getOrigin())) {
    		motesMap.put(nm.getOrigin(), new Mote(nm));
    	}
    	motesMap.get(nm.getOrigin()).setNetwork(nm);

    	for(IMoteEvent me : moteListeners) {
			me.updateMotes(motesMap);
		}
    	
    	if(this.conn != null) {
    		DatabaseRoutine.insertNetworkMsg(this.conn, nm);
    	}
    }
    
    
    /**
     * Upon receiving a data message, insert the new data message to the data_t 
     * table of the database.
     */
    private void processDataMessage(DataMsg dm) {
    	if(motesMap.keySet().contains(dm.getOrigin())) {
    		motesMap.get(dm.getOrigin()).addData(dm);
    	}
    	
    	if(this.conn != null) {
			DatabaseRoutine.insertDataMsg(this.conn, dm);
		}
    }

	@Override
	public void connectToUsb() {
		activeUsb = usb.connectToPort();
	}
	
	@Override
	public void connectToFile() {
		activeFile = coojaReader.connectToFile();
	}

	@Override
	public void exit() {
		this.exit = true;
	}

	
	private void doUsbLoop() {		
		String line = null;
		try {
			while((line = usb.getBuffReader().readLine()) != null) {
				StringBuffer sb = new StringBuffer(line);
				
				MsgType mt = MessageParser.checkMessageType(sb);
				if(mt != null) {
					switch (mt) {
					case DATA:
						DataMsg dm = MessageParser.parseDataMsg(sb);
						if(dm != null) {
							processDataMessage(dm);
							for(IDataEvent de : dataListeners) {
								de.pushDataMessage(dm);
							}
						}
						break;
					case NETWORK:
						NetworkMsg nm = MessageParser.parseNeighboMsg(sb);
						if(nm != null) {
							System.out.println(nm);
							processNetworkMessage(nm);
							
							for(IDataEvent de : dataListeners) {
								de.pushMoteStats(motesMap.get(nm.getOrigin()));
							}						
						}
						break;
					default:
						System.err.println("wrong msg type.");
						break;
					}
				}		
				if(exit) {
					usb.getBuffReader().close();
					System.exit(0);
				}	
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void doFileLoop() {
		String line = null;
		try {
			long startingTime = 0;
			int detIteration = 1;
			HashMap<Integer, ArrayList<SympathyReport>> collectedReports = new HashMap<Integer, ArrayList<SympathyReport>>();
			while((line = coojaReader.getDataReader().readLine()) != null) {
				StringBuffer sb = new StringBuffer(line);
				
				MsgType mt = MessageParser.checkMessageType(sb);
				if(mt != null) {
					switch (mt) {
					case DATA:
						DataMsg dm = MessageParser.parseDataMsg(sb);
						if(dm != null) {
							processDataMessage(dm);
							for(IDataEvent de : dataListeners) {
								de.pushDataMessage(dm);
							}
						}
						break;
					case NETWORK:
						NetworkMsg nm = MessageParser.parseNeighboMsg(sb);
						if(nm != null) {
							System.out.println(nm);
							processNetworkMessage(nm);
							
							for(IDataEvent de : dataListeners) {
								de.pushMoteStats(motesMap.get(nm.getOrigin()));
							}						
						}
						break;
					default:
						System.err.println("wrong msg type.");
						break;
					}
				}
				
				long time = Long.valueOf(coojaReader.getTimeReader().readLine());

				if(time - startingTime >= SympathyConst.SYMPATHY_PERIOD * 1000) {
					startingTime = (int)SympathyConst.SYMPATHY_PERIOD * 1000 * detIteration;
					detIteration++;
					synchronized (motesMap) {
						ObservationMap obs = SympathyObservationTree.getObservations(motesMap);
						HashMap<Integer, SympathyReport> act = SympathyActionTree.getActions(motesMap, obs);
						
						for(Integer x : act.keySet()) {
							if(collectedReports.get(x) == null) {
								collectedReports.put(x, new ArrayList<SympathyReport>());
							}
							
							collectedReports.get(x).add(act.get(x));
						}
						
						System.out.println(obs.getPri());
						System.out.println(obs.getSec());
						System.out.println(act);
						
						for(IMoteEvent me : moteListeners) {
							me.addReport(act);
						}
						
						for(Mote m : motesMap.values()) {
							m.clearSympathyData();
						}
						
						MatrixOps.displayMatrix(MatrixOps.getAdjMatrix(new ArrayList<Mote>(motesMap.values())));
						HashMap<MapKey, Double> edgeReport = MatrixOps.testEdgeConnectivity(new ArrayList<Mote>(motesMap.values()));
						for(MapKey mk : edgeReport.keySet()) {
							System.out.println(mk.toString() + " - " + edgeReport.get(mk));
						}
						
						for(IMoteEvent me : moteListeners) {
							me.addEdgeConnectivityReport(new ArrayList<Mote>(motesMap.values()), edgeReport);
						}
					}
				}
			}
			coojaReader.exportReports(collectedReports);
			System.exit(0);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		while(!activeUsb && !activeFile) {
			try {
				Thread.sleep(2000);
				System.out.println("not-connected");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(exit) {
				System.exit(0);
			}
		}
		
		if(activeUsb) {
			doUsbLoop();
		}
		else {
			doFileLoop();
		}
	}
	
	
	@Override
	public void sympathyEvent() {
		synchronized (motesMap) {
			ObservationMap obs = SympathyObservationTree.getObservations(motesMap);
			HashMap<Integer, SympathyReport> act = SympathyActionTree.getActions(motesMap, obs);
			System.out.println(obs.getPri());
			System.out.println(obs.getSec());
			System.out.println(act);
			
			for(IMoteEvent me : moteListeners) {
				me.addReport(act);
			}
			
			for(Mote m : motesMap.values()) {
				m.clearSympathyData();
			}
			
			HashMap<MapKey, Double> edgeConn = MatrixOps.testEdgeConnectivity(new ArrayList<Mote>(motesMap.values()));
			for(MapKey mk : edgeConn.keySet()) {
				System.out.println(mk.toString() + " - " + edgeConn.get(mk));
			}
		}
	}

	@Override
	public void startDetection() {
		sympathy.setActive(true);
	}

	
	@Override
	public void stopDetection() {
		sympathy.setActive(false);
		for(IMoteEvent me : moteListeners) {
			me.addReport(new HashMap<Integer, SympathyReport>());
		}
	}
}