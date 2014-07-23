package msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.jfree.data.time.Second;

import utils.MsgType;

/**
 * Class (routine) used for parsing the incoming messages. The method checkMessageType defines the type 
 * of the message and than the appropriate method can be used to extract the message values.
 */
public class MessageParser {
	
	/**
	 * Classify the message as either network or data message (using enum MsgType) base on the first 
	 * token of the message.
	 */
	public static MsgType checkMessageType(StringBuffer strBuff) {
		StringTokenizer st = new StringTokenizer(strBuff.toString(), "|", false);
		return  MsgType.getMsgType(getNextToken(st));
	}
	
	/**
	 * Parse the data message, extract each value as a token. Generate and return a DataMsg object.
	 */
	public static DataMsg parseDataMsg(StringBuffer strBuff) {
		StringTokenizer mainSt = new StringTokenizer(strBuff.toString(), "|", false);
		if(mainSt.countTokens() != 2) {
			return null;
		}
		mainSt.nextToken(); // skip first |
		StringTokenizer st = new StringTokenizer(mainSt.nextToken(), ";", false);
	
		try {
			int origin = Integer.valueOf(getNextToken(st));
			double hum = RawDataConverter.convertHumidity(Double.valueOf(getNextToken(st)));
			double temp = RawDataConverter.convertTemperature(Double.valueOf(getNextToken(st)));
			double light = RawDataConverter.convertLight(Double.valueOf(getNextToken(st)));
			double volt = RawDataConverter.convertVoltage(Double.valueOf(getNextToken(st)));
			int msgId = Integer.valueOf(getNextToken(st));
			
			return(new DataMsg(origin, hum, temp, volt, light, new Second(new Date(System.currentTimeMillis())), msgId));
		} 
		catch (NumberFormatException  e) {
			return null;
		} 
	}
	
	
	/**
	 * Parse the network message, extract each value as a token. Generate and return a NetworkMsg object.
	 */
	public static NetworkMsg parseNeighboMsg(StringBuffer strBuff) {
		StringTokenizer mainSt = new StringTokenizer(strBuff.toString(), "|", false);
		if(mainSt.countTokens() != 4) {
			return null;
		}
		mainSt.nextToken(); // skip first |
		
		StringTokenizer firstDataToken = new StringTokenizer(mainSt.nextToken(), ";", false);
		try {
			int origin = Integer.valueOf(getNextToken(firstDataToken));
			int parent = Integer.valueOf(getNextToken(firstDataToken));
			int hops = Integer.valueOf(getNextToken(firstDataToken));
			int connectivity = Integer.valueOf(getNextToken(firstDataToken));
			
			if(connectivity == 0) {
				return new NetworkMsg(origin, parent, hops, connectivity, 
						new Second(new Date(System.currentTimeMillis())));
			}
			
			int connectedSince = Integer.valueOf(getNextToken(firstDataToken));
			int meanRSSI = Integer.valueOf(getNextToken(firstDataToken));
			int childrenCount = Integer.valueOf(getNextToken(firstDataToken));
			int neighborCount = Integer.valueOf(getNextToken(firstDataToken));
			int msgId = Integer.valueOf(getNextToken(firstDataToken));
			
			StringTokenizer secondDataToken = new StringTokenizer(mainSt.nextToken(), ";", false);
			HashMap<Integer, Integer> childrenComm = new HashMap<Integer, Integer>();
			for(int c = 0; c < childrenCount; c++) {
				StringTokenizer stTemp = new StringTokenizer(getNextToken(secondDataToken), ":", false);
				if(stTemp.countTokens() != 2) {
					return null;
				}
				
				int child = Integer.valueOf(getNextToken(stTemp));
				int count = Integer.valueOf(getNextToken(stTemp));
				
				childrenComm.put(child, count);
			}
			
			StringTokenizer thirdDataToken = new StringTokenizer(mainSt.nextToken(), ";", false);
			ArrayList<Integer> neighbors = new ArrayList<Integer>();
			for(int n = 0; n < neighborCount; n++) {
				neighbors.add(Integer.valueOf(getNextToken(thirdDataToken)));
			}
			
			return new NetworkMsg(origin, parent, hops, connectivity, 
					new Second(new Date(System.currentTimeMillis())), 
					connectedSince, meanRSSI, childrenComm, neighbors, msgId);
		} 
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	private static String getNextToken(StringTokenizer st) {
		if(st.hasMoreTokens()) {
			return st.nextToken().trim();
		}
		return null;
	}
}