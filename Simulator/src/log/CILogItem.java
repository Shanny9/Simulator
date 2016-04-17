package log;

import java.util.ArrayList;

class CILogItem {
	private int upDuration;
	private int downDuration;
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

	public void addUpDuration(int duration) {
		upDuration += duration;
	}

	public void addDownDuration(int duration) {
		downDuration += duration;
	}

	public boolean isUp() {
		return times.size() % 2 != 0;
	}

	public int getLastTime() {
		return this.times.get(times.size() - 1);
	}

	void updateStatus(int time) {
		getTimes().add(time);
		int timesSize = getTimes().size();
		if (isUp()) {
			// last time is up - updated downLength
			addDownDuration(getTimes().get(timesSize - 1) - getTimes().get(timesSize - 2));
		} else {
			// last time is down - updated upLength
			addUpDuration(getTimes().get(timesSize - 1) - getTimes().get(timesSize - 2));
		}
	}

}
