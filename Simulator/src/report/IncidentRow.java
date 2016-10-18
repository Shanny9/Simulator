package report;

import java.util.HashMap;
import java.util.HashSet;

import utils.SimulationTime;

import com.model.TblIncidentPK;

public class IncidentRow implements Comparable<IncidentRow> {
	
	public static final int NOTE_PRIORITY = 1;
	public static final int NOTE_INCIDENT_TYPE = 2;
	public static final int NOTE_SUPPLIER_PROFITABILITY = 3;
	
	public static final String NOTE_TRUE = "Buy";
	public static final String NOTE_FALSE = "No Buy";
	public static final String NOTE_SYS = "Sys";
	public static final String NOTE_REP = "Rep";
	public static final String NOTE_LOW = "5-Low";
	public static final String NOTE_MEDIUM = "4-Medium";
	public static final String NOTE_HIGH = "3-High";
	public static final String NOTE_CRITICAL = "2-Critical";
	public static final String NOTE_MAJOR = "1-Major";
	
	private static int rows = 0;
	private static HashSet<Byte> past_incidents = new HashSet<>();
	private static HashMap<Integer, Integer> incidents_in_session = new HashMap<>();
	private static HashMap<TblIncidentPK, Double> incident_max_downtime_costs = new HashMap<>();
	private static HashMap<Integer,Integer> noteTypes = new HashMap<>();
	
	private int row;
	private int event_id;
	private TblIncidentPK incident;
	private String ci_name;
	private byte service_id;
	private String service_code;
	private int sol_id;
	private int sol_marom;
	private int sol_rakia;
	private String priority;
	private boolean isSystematic;
	private boolean isRepeating;
	private double sol_cost;

	public static Integer getNumOfIncidentsInSession(int session) {
		return incidents_in_session.get(session);
	}
	
	public static void setNoteType(int round, int type){
		noteTypes.put(round, type);
	}

	public IncidentRow(int event_id, TblIncidentPK incident,
			String ci_name, byte service_id, String service_code, int sol_id,
			int sol_marom, int sol_rakia, String priority,
			boolean isSystematic, double sol_cost, double priority_cost, double fixed_income) {
		super();
		this.row = ++rows;
		this.event_id = event_id;
		this.incident = incident;
		this.ci_name = ci_name;
		this.service_id = service_id;
		this.service_code = service_code;
		this.sol_id = sol_id;
		this.sol_marom = sol_marom;
		this.sol_rakia = sol_rakia;
		this.priority = priority;
		this.isSystematic = isSystematic;
		this.sol_cost = sol_cost;
		this.isRepeating = IncidentRow.past_incidents.contains(incident
				.getCiId());
		
		// calculates the max downtime cost of the incident
		double max_downtime_cost = (priority_cost + fixed_income)
				* incident.getSimulationTime().getTimeUntilSessionEnds();

		Double sum_downtime_cost = IncidentRow.incident_max_downtime_costs
				.get(incident);
		if (sum_downtime_cost == null) {
			sum_downtime_cost = 0.d;
		}
		sum_downtime_cost += max_downtime_cost;
		IncidentRow.incident_max_downtime_costs.put(incident,
				sum_downtime_cost);
		
		// counts the number of incidents in a session
		int current_session = incident.getSimulationTime().getSession();
		Integer count = incidents_in_session.get(current_session);
		if (count == null) {
			count = 0;
		}
		count++;
		incidents_in_session.put(current_session, count);
		IncidentRow.past_incidents.add(incident.getCiId());
	}
	
	/**
	 * @return the row_num
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the time
	 */
	public SimulationTime getSimulationTime() {
		return incident.getSimulationTime();
	}

	public String getTimeInRound() {
		int time = incident.getSimulationTime().getRunTimeInRound();
		int hours = time / 3600;
		int minutes = (time % 3600) / 60;
		int seconds = time % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	/**
	 * @return the event_id
	 */
	public int getEventId() {
		return event_id;
	}

	/**
	 * @return the ci_id
	 */
	public byte getCiId() {
		return incident.getCiId();
	}

	/**
	 * @return the service_id
	 */
	public byte getServiceId() {
		return service_id;
	}

	/**
	 * @return the service_code
	 */
	public String getServiceCode() {
		return service_code;
	}

	/**
	 * @return the ci_code
	 */
	public String getCiName() {
		return ci_name;
	}

	/**
	 * @return the sol_id
	 */
	public int getSolutionId() {
		return this.sol_id;
	}

	/**
	 * @return the sol_marom
	 */
	public int getSolMarom() {
		return sol_marom;
	}

	/**
	 * @return the sol_rakia
	 */
	public int getSolRakia() {
		return sol_rakia;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		Integer noteType = noteTypes.get(getRound());
		if (noteType == null){
			return "";
		}
		
		switch (noteType) {
		case NOTE_PRIORITY:
			switch (this.priority) {
			case "Low":
				return NOTE_LOW;
			case "Medium":
				return NOTE_MEDIUM;
			case "High":
				return NOTE_HIGH;
			case "Critical":
				return NOTE_CRITICAL;
			case "Major":
				return NOTE_MAJOR;
			}
		
		case NOTE_INCIDENT_TYPE:
			if (isSystematic) {
				return NOTE_SYS;
			} else if (isRepeating) {
				return NOTE_REP;
			}
			return "";
		
		case NOTE_SUPPLIER_PROFITABILITY:
			return (isWorthBuy()) ? NOTE_TRUE : NOTE_FALSE;
		default:
			return "";
		}
	}

	/**
	 * @return the round
	 */
	public int getRound() {
		return incident.getSimulationTime().getRound();
	}

	/**
	 * @return the session
	 */
	public int getSessionInRound() {
		return incident.getSimulationTime().getSessionInRound();
	}

	public int getSession() {
		return incident.getSimulationTime().getSession();
	}

	/**
	 * @return the isWorthBuy
	 */
	public boolean isWorthBuy() {
		return this.sol_cost < incident_max_downtime_costs.get(incident);
	}

	/**
	 * @return the isSystematic
	 */
	public boolean isSystematic() {
		return this.isSystematic;
	}


	@Override
	public int compareTo(IncidentRow o) {
		return (getSimulationTime().before(o.getSimulationTime())) ? 1 : 0;

	}
}
