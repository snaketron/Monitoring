package utils.event;

public interface IEngineEvent {
	void connectToUsb();
	void connectToFile();
	void exit();
	
	void startDetection();
	void stopDetection();
}
