package system.sympathy;

import java.util.ArrayList;
import java.util.HashMap;

import system.Mote;
import system.sympathy.util.ObservationMap;
import system.sympathy.util.SympathyConst;
import system.sympathy.util.SympathyConst.PrimaryObservation;
import system.sympathy.util.SympathyConst.SecondaryObservation;
import utils.GlobalConst;

public class SympathyObservationTree {
	
	public static ObservationMap getObservations(HashMap<Integer, Mote> motesMap) {
		ObservationMap obs = new ObservationMap();
		for(int moteId : motesMap.keySet()) {
			obs.addPrimaryObservation(moteId, getPrimaryObservations(moteId, motesMap));
			obs.addSecondaryObservation(moteId, getSecondaryObservations(moteId, motesMap));
		}
		
		return obs;
	}
	
	
	private static ArrayList<PrimaryObservation> getPrimaryObservations(int moteId, HashMap<Integer, Mote> motesMap) {
		ArrayList <PrimaryObservation> po = new ArrayList<PrimaryObservation>();
		if(moteId != GlobalConst.SINK_ID) {
			PrimaryObservation count = evaluateMessageCount(motesMap.get(moteId));
			po.add(count);
			if(count.equals(PrimaryObservation.SEND_IRREGULAR)) {
				po.add(evaluateConnectivity(motesMap.get(moteId)));
				po.add(evaluateParentStability(motesMap.get(moteId)));
				po.add(evaluateLink(motesMap.get(moteId), motesMap.get(motesMap.get(moteId).getParent())));
			}
		}
		return po;
	}
	
	
	public static ArrayList<SecondaryObservation> getSecondaryObservations(int moteId, HashMap<Integer, Mote> motesMap) {
		ArrayList <SecondaryObservation> sm = new ArrayList <SecondaryObservation>();

		sm.add(evaluateMoteCongestion(motesMap.get(moteId)));
		if(moteId != GlobalConst.SINK_ID) {
			sm.add(evaluateRssi(motesMap.get(moteId)));
		}
		return sm;
	}
	
	
	private static PrimaryObservation evaluateMessageCount(Mote mote) {
		double res = (mote.getDataCount() / SympathyConst.DATA_EXPECTED);
		System.out.println(mote.getId() + " message count:" + res + " <-> " 
					+ mote.getDataCount() + "/" + SympathyConst.DATA_EXPECTED);
		if(res < SympathyConst.MSG_COUNT_THRESHOLD) {
			return PrimaryObservation.SEND_IRREGULAR;
		}
		
		return PrimaryObservation.SEND_REGULAR;
	}
	
	
	private static PrimaryObservation evaluateConnectivity(Mote mote) {
		if(!mote.isConnected()) {
			return PrimaryObservation.DISCONNECTED;
		}
		
		return PrimaryObservation.CONNECTED;
	}
	
	
	private static PrimaryObservation evaluateParentStability(Mote mote) {
		if(mote.getNetwork().getConnectedSince() < SympathyConst.PARENT_STABILITY_THRESHOLD) {
			return PrimaryObservation.PARENT_INSTABLE;
		}
		
		return PrimaryObservation.PARENT_STABLE;
	}
	
	
	private static PrimaryObservation evaluateLink(Mote mote, Mote parent) {
		if(mote != null && parent != null && parent.getChildrenDataCount().get(mote.getId()) != null) {
			int tx = parent.getChildrenDataCount().get(mote.getId());
			System.out.println("parent msg count : " + tx);
			if((tx / SympathyConst.DATA_EXPECTED_FROM_PARENT) < SympathyConst.MSG_COUNT_THRESHOLD) {
				return PrimaryObservation.PARENT_LINK_GOOD;
			}
		}
		
		return PrimaryObservation.PARENT_LINK_BAD;
	}
	
	
	private static SecondaryObservation evaluateRssi(Mote mote) {
		if(mote.getNetwork().getMeanRSSI() > SympathyConst.RSSI_MAX) {
			return SecondaryObservation.POSITION_TOO_CLOSE;
		}
		
		if(mote.getNetwork().getMeanRSSI() < SympathyConst.RSSI_MIN) {
			return SecondaryObservation.POSITION_TOO_FAR;
		}
		
		return SecondaryObservation.POSITION_OK;
	}
	
	
	private static SecondaryObservation evaluateMoteCongestion(Mote mote) {
		if(mote.getNetwork().getChildrenComm().size() > SympathyConst.CONGESTION_THRESHOLD) {
			return SecondaryObservation.CONGESTION;
		}
		
		return SecondaryObservation.CONGESTION_OK;
	}
}
