package system.sympathy;

import java.util.HashMap;

import system.Mote;
import system.sympathy.util.ObservationMap;
import system.sympathy.util.SympathyConst;
import system.sympathy.util.SympathyReport;
import system.sympathy.util.SympathyConst.PrimaryObservation;
import system.sympathy.util.SympathyConst.SecondaryObservation;
import utils.GlobalConst;

public class SympathyActionTree {

	public static HashMap<Integer, SympathyReport> getActions(HashMap<Integer, Mote> motesMap, 
			ObservationMap obs) {
		
		HashMap<Integer, SympathyReport> reportMap = new HashMap<Integer, SympathyReport>();
		
		for(int moteId : motesMap.keySet()) {
			SympathyReport report = new SympathyReport(moteId);
			if(moteId != GlobalConst.SINK_ID) {
				if(obs.getPri().get(moteId).get(0).equals(PrimaryObservation.SEND_IRREGULAR)) {
					
					if(obs.getPri().get(moteId).get(1).equals(PrimaryObservation.DISCONNECTED)) {
						report.addFailure(PrimaryObservation.DISCONNECTED.name());
						
						if(checkCongestion(obs, moteId, motesMap) != null) {
							report.addFailureSuggestion(checkCongestion(obs, moteId, motesMap));
						}

						if(checkFar(obs, moteId, motesMap) != null) {
							report.addFailureSuggestion(checkFar(obs, moteId, motesMap));
						}
					}
					else {
						if(obs.getPri().get(moteId).get(2).equals(PrimaryObservation.PARENT_INSTABLE)) {
							report.addFailure(PrimaryObservation.PARENT_INSTABLE.name());

							if(checkFar(obs, moteId, motesMap) != null) {
								report.addFailureSuggestion(checkFar(obs, moteId, motesMap));
							}
						}
						if(obs.getPri().get(moteId).get(3).equals(PrimaryObservation.PARENT_LINK_BAD)) {
							report.addFailure(PrimaryObservation.PARENT_LINK_BAD.name());
							
							if(checkCongestion(obs, moteId, motesMap) != null) {
								report.addFailureSuggestion(checkCongestion(obs, moteId, motesMap));
							}

							if(checkFar(obs, moteId, motesMap) != null) {
								report.addFailureSuggestion(checkFar(obs, moteId, motesMap));
							}
						}
						
						if(obs.getPri().get(moteId).get(2).equals(PrimaryObservation.PARENT_STABLE) && 
								obs.getPri().get(moteId).get(3).equals(PrimaryObservation.PARENT_LINK_GOOD)) {
							report.addFailure(PrimaryObservation.UNKNOWN_PROBLEM.name());
							report.addFailureSuggestion("Unknown reason for irregular sending (check the suggestions)");
						}
					}

					int irregularMote = searchPath(moteId, motesMap, obs);
					System.out.println("irregular mote: " + irregularMote);
					if(irregularMote != GlobalConst.DUMMY) {
						report.clear();
						report.addFailure("Mote " + irregularMote + " on the path is irregular");
					}
				}
				
				if(checkCongestion(obs, moteId, motesMap) != null) {
					report.addIndividualSuggestion(checkCongestion(obs, moteId, motesMap));
				}

				if(checkFar(obs, moteId, motesMap) != null) {
					report.addIndividualSuggestion(checkFar(obs, moteId, motesMap));
				}
				
				if(obs.getSec().get(moteId).get(1).equals(SecondaryObservation.POSITION_TOO_CLOSE)) {
					report.addIndividualSuggestion(SecondaryObservation.POSITION_TOO_CLOSE.name() + ": move away from parent."); //TODO which node?
				}
			}
			else {
				if(checkCongestion(obs, moteId, motesMap) != null) {
					report.addIndividualSuggestion(checkCongestion(obs, moteId, motesMap));
				}
			}
			reportMap.put(moteId, report);
		}
		return reportMap;
	}
	
	private static String checkCongestion(ObservationMap obs, int moteId, HashMap<Integer, Mote> motesMap) {
		if(obs.getSec().get(moteId).get(0) == null) {
			return null;
		}
		
		if(motesMap.get(moteId).getChildrenDataCount() == null) {
			return null;
		}
		
 		if(obs.getSec().get(moteId).get(0).equals(SecondaryObservation.CONGESTION)) {
			return (SecondaryObservation.CONGESTION.name() + ": remove " + (motesMap.get(moteId).getChildrenDataCount().size() - 
					SympathyConst.CONGESTION_THRESHOLD) + " child node/s."); 
		}
 		
 		return null;
	}
	
	
	private static String checkFar(ObservationMap obs, int moteId, HashMap<Integer, Mote> motesMap) {
		if(obs.getSec().get(moteId).get(1) == null) {
			return null;
		}
		
		if(obs.getSec().get(moteId).get(1).equals(SecondaryObservation.POSITION_TOO_FAR)) {
			return (SecondaryObservation.POSITION_TOO_FAR.name() + ": move closer to " + motesMap.get(moteId).getParent());
		}
 		
 		return null;
	}
	

	private static int searchPath(int moteId, HashMap<Integer, Mote> motesMap, ObservationMap obs) {
		int irregularMote = GlobalConst.DUMMY;
		int parentId = motesMap.get(moteId).getParent();
		
		while(parentId != GlobalConst.SINK_ID) {
			if(obs.getPri().containsKey(parentId)){
				if(obs.getPri().get(parentId).get(0) == null) {
					return GlobalConst.DUMMY;
				}

				if(obs.getPri().get(parentId).get(0).equals(PrimaryObservation.SEND_IRREGULAR)) {
					irregularMote = parentId;
				}
				parentId = motesMap.get(parentId).getParent();
			}
			else {
				break;
			}
		}
		return irregularMote;
	}
}