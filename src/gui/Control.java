package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.Mote;
import system.connectivity.MapKey;
import system.sympathy.util.SympathyReport;
import utils.event.IEngineEvent;
import utils.event.IMoteEvent;
import utils.event.ISystemEvent;

/**
 * The control panel contains all the buttons which start/stop the tool. It also 
 * displays all the active motes and enables their selection.
 */
public class Control extends JPanel implements IMoteEvent {
	private static final long serialVersionUID = 1L;
	private JPanel nodesPanel; //active nodes panel
	private JPanel controlPanel; //buttons panel
	
	private JButton connectToUsb;
	private JButton connectToFile;
	private JButton start;
	private JButton stop;
	private JButton exit;
	private JButton enableDetection;
	private JButton disableDetection;
	
	private HashMap<Mote, JLabel> activeMap;
	private List<IEngineEvent> engineList;
	private List<ISystemEvent> systemList;
	
	public Control() {
		this.engineList = new ArrayList<IEngineEvent>();
		this.systemList = new ArrayList<ISystemEvent>();
		
		createControlPanel();
		createNodesPanel();
		
		this.setLayout(new BorderLayout());
		this.add(nodesPanel, BorderLayout.NORTH);
		this.add(controlPanel, BorderLayout.SOUTH);
		this.setPreferredSize(new Dimension(200, 500));
		
		this.activeMap = new HashMap<Mote, JLabel>();
		
		this.validate();
		this.repaint();
	}
	
	private void createControlPanel() {
		controlPanel = new JPanel(new GridLayout(7, 1));
		controlPanel.setBorder(BorderFactory.createTitledBorder("Control Panel"));
		
		connectToUsb = new JButton("Connect to USB");
		connectToUsb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				connectToUsb.setEnabled(false);
				connectToFile.setEnabled(false);
				exit.setEnabled(true);
				enableDetection.setEnabled(true);
				
				for(IEngineEvent ee : engineList) {
		            ee.connectToUsb();
				}
			}
		});
		
		connectToFile = new JButton("Connect to File");
		connectToFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				connectToFile.setEnabled(false);
				connectToUsb.setEnabled(false);
				start.setEnabled(false);
				stop.setEnabled(false);
				enableDetection.setEnabled(false);
				disableDetection.setEnabled(false);
				exit.setEnabled(true);
				
				for(IEngineEvent ee : engineList) {
		            ee.connectToFile();
				}
			}
		});
		
		start = new JButton("Start Visualization");
		start.setEnabled(false);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				ArrayList<Mote> selectedMotes = getSelectedMotes();
				if(!selectedMotes.isEmpty()) {
					start.setEnabled(false);
					stop.setEnabled(true);
					setLabelsState(false);
					setLabelsColor(Color.white);
					for(ISystemEvent se : systemList) {
						se.startVisualization(selectedMotes);
					}
				}
			}
		});
		
		stop = new JButton("Stop Visualization");
		stop.setEnabled(false);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				stop.setEnabled(false);
				start.setEnabled(true);
				setLabelsState(true);
				refreshButtonsState();
				for(ISystemEvent se : systemList) {
					se.stopVisualization();
				}
			}
		});
		
		exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        for(IEngineEvent ee : engineList) {
		            ee.exit();
				}
			}
		});
		
		
		enableDetection = new JButton("Enable Detection");
		enableDetection.setEnabled(false);
		enableDetection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableDetection.setEnabled(false);
				disableDetection.setEnabled(true);
		        for(IEngineEvent ee : engineList) {
		        	ee.startDetection();
				}
			}
		});
		
		disableDetection = new JButton("Disable Detection");
		disableDetection.setEnabled(false);
		disableDetection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableDetection.setEnabled(true);
				disableDetection.setEnabled(false);
		        for(IEngineEvent ee : engineList) {
		        	ee.stopDetection();
				}
			}
		});
		
		controlPanel.add(connectToUsb);
		controlPanel.add(connectToFile);
		controlPanel.add(start);
		controlPanel.add(stop);
		controlPanel.add(enableDetection);
		controlPanel.add(disableDetection);
		controlPanel.add(exit);
	}
	
	private void createNodesPanel() {
		nodesPanel = new JPanel(new GridLayout(20, 1)); //10 = to be const
		nodesPanel.setBorder(BorderFactory.createTitledBorder("Active Nodes"));
	}
	
	private void setLabelsState(boolean enabled) {
		for(JLabel label : activeMap.values()) {
			label.setEnabled(enabled);
			label.setVisible(enabled);
		}
	}
	
	private void setLabelsColor(Color color) {
		for(JLabel label : activeMap.values()) {
			label.setBackground(color);
		}
	}
	
	private ArrayList<Mote> getSelectedMotes() {
		ArrayList<Mote> selectedMotes = new ArrayList<Mote>();
		for(Mote mote : activeMap.keySet()) {
			if(activeMap.get(mote).getBackground().equals(Color.green)) {
				selectedMotes.add(mote);
			}
		}
		return selectedMotes;
	}
	
	private void refreshButtonsState() {
		boolean atLeastOneIsGreen = false;
		for(JLabel label : activeMap.values()) {
			if(!label.isEnabled()) {
				break;
			}
			
			if(label.getBackground().equals(Color.green)) {
				if(!stop.isEnabled()) {
					start.setEnabled(true);
				}
				atLeastOneIsGreen = true;
			}
		}
		
		if(!atLeastOneIsGreen) {
			start.setEnabled(false);
			stop.setEnabled(false);
		}
	}
	
	public void addListener(IEngineEvent ee, ISystemEvent se) {
    	engineList.add(ee);
    	systemList.add(se);
    }

	@Override
	public void updateMotes(HashMap<Integer, Mote> motesMap) {
		for(final Mote m : motesMap.values()) {
			if(m.isConnected()) {
				if(!activeMap.containsKey(m)) {
					final JLabel moteLabel = new JLabel("Mote " + m.getId());
					moteLabel.setOpaque(true);
					moteLabel.setBackground(Color.white);
					
					if(stop.isEnabled()) {
						moteLabel.setEnabled(false);
						moteLabel.setVisible(false);
					}
					
					this.activeMap.put(m, moteLabel);
					nodesPanel.add(moteLabel);
					this.validate();
					this.repaint();
					
					moteLabel.addMouseListener(new MouseListener() {
						@Override
						public void mouseReleased(MouseEvent arg0) {
							if(moteLabel.getBackground().equals(Color.white)) {
								moteLabel.setBackground(Color.green);
							}
							else {
								moteLabel.setBackground(Color.white);
							}
							refreshButtonsState();
						}
						
						@Override
						public void mousePressed(MouseEvent arg0) {
							activeMap.put(m, moteLabel);
						}
						@Override
						public void mouseExited(MouseEvent arg0) {}
						@Override
						public void mouseEntered(MouseEvent arg0) {}
						@Override
						public void mouseClicked(MouseEvent arg0) {}
					});
				}
			}
			else {
				if(activeMap.containsKey(m)) {
					this.nodesPanel.remove(activeMap.get(m));
					this.activeMap.remove(m);
					this.repaint();
					this.validate();
				}
			}
		}
	}

	@Override
	public void addEdgeConnectivityReport(ArrayList<Mote> motes, HashMap<MapKey, Double> report) {}
	
	@Override
	public void addReport(HashMap<Integer, SympathyReport> reportMap) {}
}