package utils;

/**
 * Represents the constants used by multiple classes.
 */
public interface GlobalConst {
	static final String USB_PORT = "/dev/ttyUSB0";
	static final int PORT = 10000;
	static final int BAUD = 115200;
	
	static final int SINK_ID = 2;
	static final int DUMMY = 255;
	
	static final String DB = "database/tosdb";
	static final String DATA_TABLE = "data_t";
	static final String NETWORK_TABLE = "network_t";
}