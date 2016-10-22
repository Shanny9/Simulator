package log;

import java.io.Serializable;
import java.util.HashSet;

import com.daoImpl.TblServiceDaoImpl;
import com.model.TblService;

public class SolutionLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String team;
	private HashSet<String> events = new HashSet<>();
	private static TblServiceDaoImpl serviceDaoImpl = new TblServiceDaoImpl();
	
	public SolutionLog(String courseName, String team, HashSet<Byte> servicesFixed){
		this.team = team;
		for (Byte service_id : servicesFixed){
			TblService service = serviceDaoImpl.getServiceById(service_id);
			if (service != null){
				this.events.add(String.valueOf(service.getEventId()));
			}
		}
	}
	
	/**
	 * @return The team that solved
	 */
	public String getTeam(){
		return team;
	}
	
	/**
	 * @return The events fixed by the solution
	 */
	public HashSet<String> getEvents(){
		return events;
	}
}
