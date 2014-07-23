package utils.event;

import java.util.ArrayList;
import java.util.HashMap;

import system.Mote;
import system.connectivity.MapKey;
import system.sympathy.util.SympathyReport;

public interface IMoteEvent {
	void addReport(HashMap<Integer, SympathyReport> reportMap);
	
	void updateMotes(HashMap<Integer, Mote> motesMap);
	
	void addEdgeConnectivityReport(ArrayList<Mote> motes, HashMap<MapKey, Double> report);
}
