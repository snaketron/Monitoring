package utils.event;

import system.Mote;
import msg.DataMsg;

public interface IDataEvent {
	void pushDataMessage(DataMsg dm);
	void pushMoteStats(Mote motes);
}
