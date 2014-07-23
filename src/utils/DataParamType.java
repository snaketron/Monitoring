package utils;

/**
 * Enumeration representing the different parameters included in a typical data message.
 * It is used extensively by the charting classes.
 */
public enum DataParamType {
	HUMIDITY("Humidity", 0, 100),
	TEMPERATURE("Temperature", 0, 40),
	LIGHT("Light-Intensity", 0, 1000),
	VOLTAGE("Voltage", 0, 4);
	
	private String name;
	private double min;
	private double max;
	
	private DataParamType(String name, double min, double max) {
		this.name = name;
		this.min = min;
		this.max = max;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}
	
	public static DataParamType findParamType(String name) {
		for(DataParamType pt : values()) {
			if(pt.equals(name)) {
				return pt;
			}
		}
		return null;
	}
}
