package log;

import java.io.Serializable;

public class IncidentLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte incident_id;
	private byte root_ci;
	private int start_time;
	private int end_time;

	/**
	 * @param incident_id
	 * @param root_ci
	 * @param start_time
	 * @param end_time
	 */
	public IncidentLog(byte incident_id, byte root_ci, int start_time) {
		super();
		this.incident_id = incident_id;
		this.root_ci = root_ci;
		this.start_time = start_time;
	}

	/**
	 * @return the incident_id
	 */
	public byte getIncident_id() {
		return incident_id;
	}

	/**
	 * @return the root_ci
	 */
	byte getRoot_ci() {
		return root_ci;
	}

	/**
	 * @return the start_time
	 */
	int getStart_time() {
		return start_time;
	}

	/**
	 * @return the end_time
	 */
	int getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time
	 *            the end_time to set
	 */
	void setEnd_time(int end_time) {
		this.end_time = end_time;
	}

	// to be used during the simulation
	boolean isOpen(int time) {
		return time > start_time && end_time == 0;
	}
	
	// to be used only after the simulation ended 
	boolean isOpen() {
		return end_time == 0;
	}
	
	public int getDuration(){
		return end_time - start_time;
	}

	public String toString() {
		return "Incident id=" + incident_id + ", start_time= " + start_time + ", end_time= " + end_time;
	}
}