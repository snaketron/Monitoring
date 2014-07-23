package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import msg.DataMsg;
import msg.NetworkMsg;

/**
 * The class for representing motes. Each mote object stores the data read from the appropriate mote,
 * this will cause memory problems (sooner or later). 
 * TODO: flush after a number of reads.
 */
public class Mote {
	private int id;
	private ArrayList<DataMsg> data;
	private NetworkMsg network;
	private int parent;
	private int hops;
	private boolean connected;
	private int dataCount;
	private HashMap<Integer, Integer> childrenDataCount;
	private ArrayList<Integer> neighbors;
	private ReentrantLock locker;
	
	public Mote(int id) {
		this.id = id;
		this.data = new ArrayList<DataMsg>();
		this.childrenDataCount = new HashMap<Integer, Integer>();
		this.neighbors = new ArrayList<Integer>();
		this.locker = new ReentrantLock();
	}
	
	public Mote(NetworkMsg nm) {
		super();
		this.id = nm.getOrigin();
		this.data = new ArrayList<DataMsg>();
		this.network = nm;
		this.parent = nm.getParent();
		this.hops = nm.getHops();
		this.connected = (nm.getConnectivity() != 0);
		this.dataCount = 0;
		this.childrenDataCount = new HashMap<Integer, Integer>();
		this.neighbors = new ArrayList<Integer>();
		this.locker = new ReentrantLock();
		mapChildrenDataCount(nm);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(int connecivity) {
		this.connected = (connecivity != 0);;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public ArrayList<DataMsg> getData() {
		return data;
	}

	public void setData(ArrayList<DataMsg> data) {
		this.data = data;
	}

	public NetworkMsg getNetwork() {
		return network;
	}

	public void setNetwork(NetworkMsg nm) {
		this.locker.lock();
		this.network = nm;
		this.parent = nm.getParent();
		this.hops = nm.getHops();
		this.connected = (nm.getConnectivity() != 0);
		mapChildrenDataCount(nm);
		this.neighbors.clear();
		this.neighbors.addAll(nm.getNeighbors());
		this.locker.unlock();
	}
	
	private void mapChildrenDataCount(NetworkMsg nm) {
		for(Integer ch : network.getChildrenComm().keySet()) {
			if(!this.childrenDataCount.containsKey(ch)) {
				this.childrenDataCount.put(ch, 0);
			}
			
			int sum = this.childrenDataCount.get(ch) + nm.getChildrenComm().get(ch);
			this.childrenDataCount.put(ch, sum);
		}
	}
	
	public void clearSympathyData() {
		this.locker.lock();;
		this.dataCount = 0;
		this.childrenDataCount.clear();
		this.locker.unlock();
	}

	public void addData(DataMsg dm) {
		if(!data.contains(dm)) {
			this.data.add(dm);
			this.dataCount++;
		}
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
	
	public HashMap<Integer, Integer> getChildrenDataCount() {
		return childrenDataCount;
	}

	public void setChildrenDataCount(HashMap<Integer, Integer> childrenDataCount) {
		this.childrenDataCount = childrenDataCount;
	}

	public ArrayList<Integer> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Integer> neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mote other = (Mote) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Mote [id=" + id + ", parent=" + parent + ", con=" + connected + "]";
	}
}