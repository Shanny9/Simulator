package com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.TblCourseDao;
import com.dao.TblGeneralParametersDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblCourse;

import utils.TimerManager;

/**
 * Servlet implementation class HomeController
 */
@WebServlet("/HomeController")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		String action = request.getParameter("action");
		if (action.equals("getTime")){   	
	    	HashMap<String,Object> clocks = TimerManager.getClocks();
	    	clocks.put("serverTime", new Date());
	    	Gson gson = new GsonBuilder().setPrettyPrinting().create();	
			response.getWriter().print(gson.toJson(clocks));
		} else if (action.equals("startSimulator")){
			startSimulator("IDF-AMAM-01");
		}
	}
	
	protected HashMap<String,Object> getTimes() 
	{
		TblGeneralParametersDao dao = new TblGeneralParametersDao();
		HashMap<String, Object> timesMap = new HashMap<String, Object>();
		
		timesMap.put("sessionTime", dao.getSessionTime());
		timesMap.put("roundTime", dao.getRoundTime());
		timesMap.put("numOfRounds", dao.getGeneralParameters().getNumOfRounds());
		timesMap.put("pauseTime", dao.getGeneralParameters().getPauseTime());
		timesMap.put("runTime", dao.getGeneralParameters().getRunTime());
		timesMap.put("sessionsPerRound", dao.getGeneralParameters().getSessionsPerRound());
		
		return timesMap;
	}
	
	public void startSimulator(String courseName){		
		TblCourseDao dao = new TblCourseDao();
		TblCourse course = dao.getCourseById(courseName);
		if(course!=null){
			int runTime=(int) getTimes().get("runTime");
			int roundTime = (int) getTimes().get("roundTime");
			int currentRound = course.getLastRoundDone();
			TimerManager.startSimulator(runTime,roundTime,currentRound);
//			System.out.println("HomeController: started simulator");
			}
	}
}
