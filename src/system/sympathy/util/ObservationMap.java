package system.sympathy.util;

import java.util.ArrayList;
import java.util.HashMap;

import system.sympathy.util.SympathyConst.PrimaryObservation;
import system.sympathy.util.SympathyConst.SecondaryObservation;

public class ObservationMap {
	private HashMap<Integer, ArrayList<PrimaryObservation>> pri;
	private HashMap<Integer, ArrayList<SecondaryObservation>> sec;
	
	public ObservationMap() {
		this.pri = new HashMap<Integer, ArrayList<PrimaryObservation>>();
		this.sec = new HashMap<Integer, ArrayList<SecondaryObservation>>();
	}
	
	public void addPrimaryObservation(int moteId, ArrayList<PrimaryObservation> p) {
		this.pri.put(moteId, p);
	}
	
	public void addSecondaryObservation(int moteId, ArrayList<SecondaryObservation> s) {
		this.sec.put(moteId, s);
	}

	public HashMap<Integer, ArrayList<PrimaryObservation>> getPri() {
		return pri;
	}

	public void setPos(HashMap<Integer, ArrayList<PrimaryObservation>> pos) {
		this.pri = pos;
	}

	public HashMap<Integer, ArrayList<SecondaryObservation>> getSec() {
		return sec;
	}

	public void setSos(HashMap<Integer, ArrayList<SecondaryObservation>> sos) {
		this.sec = sos;
	}
}