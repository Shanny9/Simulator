package log;

import java.io.Serializable;

import utils.SimulationTime;

/**
 * This class stores information of an Incident.
 */
public class IncidentLog implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The ID of the incident
	 */
	private byte incident_id;
	/**
	 * The CI that triggers the incident
	 */
	private byte root_ci;
	/**
	 * The time when incident occurs
	 */
	private int start_time;
	/**
	 * The time when the incident is solved
	 */
	private int end_time;

	/**
	 * Full Constructor
	 * 
	 * @param incident_id
	 *            The ID of the incident
	 * @param root_ci
	 *            The CI that triggers the incident
	 * @param start_time
	 *            The time when incident occurs
	 * @param end_time
	 *            The time when the incident is solved
	 */
	public IncidentLog(byte incident_id, byte root_ci, SimulationTime start_time) {
		super();
		this.incident_id = incident_id;
		this.root_ci = root_ci;
		this.start_time = start_time.getRunTime();
	}

	/**
	 * @return The incident_id
	 */
	public byte getIncident_id() {
		return incident_id;
	}

	/**
	 * @return The root_ci
	 */
	byte getRoot_ci() {
		return root_ci;
	}

	/**
	 * @return The start_time
	 */
	SimulationTime getStart_time() {
		return new SimulationTime(start_time);
	}

	/**
	 * @return The end_time
	 */
	SimulationTime getEnd_time() {
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
		return "Incident id=" + incident_id + ", start_time= " + start_time
				+ ", end_time= " + end_time;
	}
}