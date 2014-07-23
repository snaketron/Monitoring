package utils;

/**
 * Enumeration describing the two message types that can be 
 * received on the USB port: DATA and NETWORK
 */
public enum MsgType {
	DATA("data"),
	NETWORK("network");
	
	private String name;
	
	MsgType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static MsgType getMsgType(String test) {
		for(MsgType mt : values()) {
			if(mt.getName().equals(test)) {
				return mt;
			}
		}
		return null;
	}
}
