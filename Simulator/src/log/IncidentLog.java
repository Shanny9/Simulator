package log;

import java.io.Serializable;
import java.util.ArrayList;

import utils.SimulationTime;

/**
 * This class stores information of an Incident.
 */
public class IncidentLog implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The CI that triggers the incident
	 */
	private byte ci_id;
	/**
	 * The times of the incident log
	 */
	private ArrayList<Integer> times;

	/**
	 * @param start_time
	 * @param ci_id
	 */
	public IncidentLog(byte ci_id) {
		super();
		this.ci_id = ci_id;
		this.times = new ArrayList<>();
	}

	/**
	 * @return The ci_id
	 */
	public byte getCiId() {
		return ci_id;
	}

	/**
	 * Indicates whether the incident is open or not (during simulation time).
	 * 
	 * @param time
	 *            current simulation time
	 * @return {@code true} if the incident is open. Otherwise returns
	 *         {@code false}.
	 */
	boolean isOpen(SimulationTime time) {
		return times.size() % 2 != 0 /* is down */
				/* after last incident */
				&& time.after(times.get(times.size() - 1)) ;
	}

	/**
	 * Indicates whether the incident is open or not (after simulation time).
	 * 
	 * @return {@code true} if the incident is open. Otherwise returns
	 *         {@code false}.
	 */
	boolean isOpen() {
		return times.size() % 2 != 0;
	}

	/**
	 * Gets the duration of the incident.
	 * 
	 * @return The duration of the incident.
	 */
	public int getDuration() {
		int sum = 0;
		for (int index = 1; index < times.size(); index += 2) {
			sum += times.get(index) - times.get(index - 1);
		}
		return sum;
	}
	
	public void open(SimulationTime time) {
		if (!isOpen(time)) {
			times.add(time.getRunTime());
		}
	}
	
	public void close(SimulationTime time) {
		if (isOpen(time)) {
			times.add(time.getRunTime());
		}
	}

	public String toString() {
		return "ci id=" + ci_id + ", times= " + times;
	}
}