package log;

import java.io.Serializable;
import java.util.ArrayList;

class CILogItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int upDuration;
	private int downDuration;
	private int lastDuration;
	private int id;
	/*
	 * even cells = UP, odd cells = DOWN
	 */
	private ArrayList<Integer> times;

	/**
	 * @param id
	 * @param time
	 */
	public CILogItem(int id) {
		super();
		this.id = id;
		this.times = new ArrayList<>();
		this.times.add(0);
	}
	
	
	public int getId(){
		return id;
	}
	
	/**
	 * @return the times
	 */
	ArrayList<Integer> getTimes() {
		return times;
	}

	public int getUpDuration() {
		return upDuration;
	}

	public int getDownDuration() {
		return downDuration;
	}
	
	public boolean isUp() {
		return times.size() % 2 != 0;
	}

	public int getLastTime() {
		return this.times.get(times.size() - 1);
	}
	
	void updateStatus(int time){
		getTimes().add(time);
		int timesSize = getTimes().size();
		this.lastDuration = getTimes().get(timesSize - 1) - getTimes().get(timesSize - 2);
		if (isUp()){
			upDuration+=getLastDuaration();
		} else{
			downDuration+=getLastDuaration();
		}
	}
	
	int getLastDuaration(){
		return lastDuration;
	}
	
	public void update(int time){
		updateStatus(time);
	}
}
