package com.servlet;

import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.TblCIDaoImpl;
import com.model.TblIncident;
import com.model.TblResolution;
import com.model.TblResolutionPK;

/**
 * Servlet implementation class ClientController
 */
@WebServlet("/ClientController")
public class ClientController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashMap<TblResolutionPK,TblResolution> resolutions = new HashMap<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// General settings
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		
		// get the action
		String action = request.getParameter("action");
		
		if (action.equals("getSolutions")){
			getSolutions(request.getParameter("team"), response);
		}
	}

	private void getSolutions(String team, HttpServletResponse response) throws IOException {
		TblCIDaoImpl ciDao = new TblCIDaoImpl();
		response.getWriter().print(ciDao.getSolutions(team));
	}
	
	
	private void addResolution(HttpServletRequest request){
		byte incidentID, isPurchased;
		Time time;
		String course, team;
		TblResolutionPK pk = new TblResolutionPK();
		
		incidentID = Byte.valueOf(request.getParameter("incident"));
		course = request.getParameter("course"); 
		isPurchased = Byte.valueOf(request.getParameter("isPurchased"));
		time = new Time(Long.valueOf(request.getParameter("time")));
		team = request.getParameter("team");
		pk.setCourse(course);
		pk.setIncident_ID(incidentID);
		TblResolution res;
		
		//update existing row
		if(resolutions.containsKey(pk))
		{
			res = resolutions.get(pk);
		}
		//add new row
		else
		{
			res = new TblResolution();
			res.setId(pk);
			TblIncident in = new TblIncident();
			in.setIncident_ID(incidentID);
			res.setTblIncident(in);
		}
		
		if(team.equals("Marom"))
		{
			res.setIsPurchasedA(isPurchased);
			res.setIsResolvedA(new Byte("1"));
			res.setResolution_timeA(time);	
		}
		else
		{
			res.setIsPurchasedB(isPurchased);
			res.setIsResolvedB(new Byte("1"));
			res.setResolution_timeB(time);	
		}
		resolutions.put(pk, res);
		
	}


	
	
}

