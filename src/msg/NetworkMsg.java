package msg;

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.data.time.Second;

/**
 * Class representing the structure of the network messages.
 */
public class NetworkMsg {
	private int origin;
	private int parent;
	private int hops;
	private int connectivity;
	private Second time;
	private int connectedSince;
	private int meanRSSI;
	private HashMap<Integer, Integer> childrenComm;
	private ArrayList<Integer> neighbors;
	private int msgId;
	
	public NetworkMsg(int origin, int parent, int hops, int connectivity,
			Second time) {
		super();
		this.origin = origin;
		this.parent = parent;
		this.hops = hops;
		this.connectivity = connectivity;
		this.time = time;
		this.childrenComm = new HashMap<Integer, Integer>();
		this.neighbors = new ArrayList<Integer>();
	}
	
	public int getOrigin() {
		return origin;
	}

	public NetworkMsg(int origin, int parent, int hops, int connectivity, Second time, 
			int connectedSince, int meanRSSI, HashMap<Integer, Integer> childrenComm,
			ArrayList<Integer> neighbors, int msgId) {
		super();
		this.origin = origin;
		this.parent = parent;
		this.hops = hops;
		this.connectivity = connectivity;
		this.time = time;
		this.connectedSince = connectedSince;
		this.meanRSSI = meanRSSI;
		this.childrenComm = childrenComm;
		this.neighbors = neighbors;
		this.msgId = msgId;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public int getConnectivity() {
		return connectivity;
	}

	public void setConnectivity(int connectivity) {
		this.connectivity = connectivity;
	}
	
	public Second getTime() {
		return time;
	}

	public void setTime(Second time) {
		this.time = time;
	}

	public int getConnectedSince() {
		return connectedSince;
	}

	public void setConnectedSince(int connectedSince) {
		this.connectedSince = connectedSince;
	}

	public int getMeanRSSI() {
		return meanRSSI;
	}

	public void setMeanRSSI(int meanRSSI) {
		this.meanRSSI = meanRSSI;
	}

	public HashMap<Integer, Integer> getChildrenComm() {
		return childrenComm;
	}

	public void setChildrenComm(HashMap<Integer, Integer> childrenComm) {
		this.childrenComm = childrenComm;
	}

	public ArrayList<Integer> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Integer> neighbors) {
		this.neighbors = neighbors;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "NetworkMsg [origin=" + origin + ", parent=" + parent
				+ ", hops=" + hops + ", connectivity=" + connectivity
				+ ", time=" + time + ", connectedSince=" + connectedSince
				+ ", meanRSSI=" + meanRSSI + ", childrenComm=" + childrenComm
				+ ", neighbors=" + neighbors + ", msgId=" + msgId + "]";
	}
}