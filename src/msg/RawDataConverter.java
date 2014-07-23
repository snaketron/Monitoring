package msg;

/**
 * This is a routine that converts the raw data obtained from the motes into a real measure.
 * Such conversions are done for each data type (temperature, humidity, etc).
 */
public class RawDataConverter {
	
	public static double convertTemperature(double rawTemp) {
		return (-39.60 + 0.01 * rawTemp);
	}
	
	public static double convertHumidity(double rawHum) {
		return (-4 + 0.0405 * rawHum + (-2.8 * Math.pow(10, -6)) * (rawHum*rawHum));
	}
	
	public static double convertLight(double rawLight) {
		double I = ((rawLight/4096.0) * 1.5) / 100000.0;
		return 0.625 * 100000 * I * 1000;
	}
	
	public static double convertVoltage(double rawVolt) {
		return (rawVolt/4096) * 3; 
	}
}