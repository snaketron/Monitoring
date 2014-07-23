package msg;

import org.jfree.data.time.Second;

/**
 * Class representing the structure of the data messages.
 */
public class DataMsg {
	private int origin;
	private double humidity;
	private double temp;
	private double volt;
	private double light;
	private Second second;
	private int msgId;
	
	public DataMsg(int origin, double humidity, 
			double temp,double volt, double light, 
			Second second, int msgId) {
		super();
		this.msgId = msgId;
		this.origin = origin;
		this.humidity = humidity;
		this.temp = temp;
		this.volt = volt;
		this.light = light;
		this.second = second;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getTemperature() {
		return temp;
	}

	public void setTemperature(double temp) {
		this.temp = temp;
	}

	public double getVolt() {
		return volt;
	}

	public void setVolt(double volt) {
		this.volt = volt;
	}

	public double getLight() {
		return light;
	}

	public void setLight(double light) {
		this.light = light;
	}

	public Second getSecond() {
		return second;
	}

	public void setSecond(Second second) {
		this.second = second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + msgId;
		result = prime * result + origin;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataMsg other = (DataMsg) obj;
		if (msgId != other.msgId)
			return false;
		if (origin != other.origin)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataMsg [msgId=" + msgId + ", origin=" + origin + "]";
	}
}