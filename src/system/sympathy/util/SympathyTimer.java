package system.sympathy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import utils.event.ISympathyEvent;

public class SympathyTimer {
	private boolean active;
	private Timer timer;
	private List<ISympathyEvent> sympathyListeners;

	public SympathyTimer() {
		this.timer = new Timer(true);
		this.active = false;
		this.sympathyListeners = new ArrayList<ISympathyEvent>();
		this.timer.scheduleAtFixedRate(new TimerTask() {
			public void run() { 
				if(active) {
					for(ISympathyEvent se : sympathyListeners) {
						se.sympathyEvent();
					}
				}
			}
		}, 0, (int)SympathyConst.SYMPATHY_PERIOD * 1000);
	}
	
	public void addSympathyListeners(ISympathyEvent se) {
		sympathyListeners.add(se);
    }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}