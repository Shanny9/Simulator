package log;

import java.io.Serializable;

import com.model.TblIncidentPK;

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
	 * The time when incident occurs
	 */
	private int start_time;
	/**
	 * The time when the incident is solved
	 */
	private int end_time;


	/**
	 * @param start_time
	 * @param ci_id
	 */
	public IncidentLog(TblIncidentPK inc_pk) {
		super();
		this.ci_id = inc_pk.getCiId();
		this.start_time = inc_pk.getTime();
	}

	/**
	 * @return The ci_id
	 */
	public byte getCiId() {
		return ci_id;
	}

	/**
	 * @return The start_time
	 */
	SimulationTime getStartTime() {
		return new SimulationTime(start_time);
	}

	/**
	 * @return The end_time
	 */
	SimulationTime getEndTime() {
		return new SimulationTime(end_time);
	}

	/**
	 * Closes the incident if it is open.
	 * 
	 * @param time
	 *            The time to close the incident
	 */
	void close(SimulationTime time) {
		if (isOpen(time)) {
			this.end_time = time.getRunTime();
		}
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
		return time.after(start_time) && end_time == 0;
	}

	/**
	 * Indicates whether the incident is open or not (after simulation time).
	 * 
	 * @return {@code true} if the incident is open. Otherwise returns
	 *         {@code false}.
	 */
	boolean isOpen() {
		return end_time == 0;
	}

	/**
	 * Gets the duration of the incident.
	 * 
	 * @return The duration of the incident.
	 */
	public int getDuration() {
		return end_time - start_time;
	}

	public String toString() {
		return "ci id=" + ci_id + ", start_time= " + start_time
				+ ", end_time= " + end_time;
	}
}