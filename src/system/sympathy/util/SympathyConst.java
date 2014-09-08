package system.sympathy.util;

public interface SympathyConst {
	static final double SYMPATHY_PERIOD = 60;
	
	/**set in tinyos project to 4 sec**/
	static final double PERIOD_FOR_DATA = 4;
	
	/**messages expected in a period (defined in tinos)**/
	static final double DATA_EXPECTED = SYMPATHY_PERIOD / PERIOD_FOR_DATA;
	
	/**messages expected in a period (defined in tinos)**/
	static final double DATA_EXPECTED_FROM_PARENT = SYMPATHY_PERIOD / PERIOD_FOR_DATA;
	
	/**msg count threshold : e.g. if 90% of expected messages are received => good**/
	static final double MSG_COUNT_THRESHOLD = 0.7;
	
	/**a routing parent should not be dropped for more than 3 sympathy periods.**/
	static final double PARENT_STABILITY_THRESHOLD = SYMPATHY_PERIOD;
	
	/**
	 * Enumeration containing all the possible states of the sympathy
	 * decision tree.
	 **/
	public enum PrimaryObservation {
		SEND_REGULAR,
		SEND_IRREGULAR,
		CONNECTED,
		DISCONNECTED,
		PARENT_STABLE,
		PARENT_INSTABLE,
		PARENT_LINK_BAD,
		PARENT_LINK_GOOD,
		UNKNOWN_PROBLEM
	}
	
	static final double RSSI_MIN = -35;
	
	static final double RSSI_MAX = 0;
	
	static final double CONGESTION_THRESHOLD = 3;
	
	/**
	 * Enumeration containing the suggestion states of the auxiliary 
	 * sympathy decision tree.
	 **/
	public enum SecondaryObservation {
		POSITION_TOO_CLOSE,
		POSITION_TOO_FAR,
		POSITION_OK,
		CONGESTION,
		CONGESTION_OK,
	}
	
	/**
	 * If the removal of an edge disconnects more K_EDGE % of 
	 * all the vertices an k-edge alert is fired 
	 **/
	static final double K_EDGE = 0.15;
}
