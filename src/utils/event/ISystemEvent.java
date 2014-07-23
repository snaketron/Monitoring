package utils.event;

import java.util.ArrayList;

import system.Mote;

public interface ISystemEvent {
	void startVisualization(ArrayList<Mote> motes);
	void stopVisualization();
}
