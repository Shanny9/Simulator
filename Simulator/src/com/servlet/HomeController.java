package com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.TblCourseDaoImpl;
import com.dao.TblGeneralParametersDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblCourse;

import utils.HomeData;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// General settings
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// get the action
		String action = request.getParameter("action");

		if (action.equals("getTime")) {

			TblCourseDaoImpl dao = new TblCourseDaoImpl();
			TblCourse course = dao.getCourseById("IDF-AMAM-01");
			if (course != null) {
				int runTime = (int) getTimes().get("runTime");
				int roundTime = (int) getTimes().get("roundTime");
				int currentRound = course.getLastRoundDone();
				
				HashMap<String, Object> clocks = TimerManager.getClocks(runTime, roundTime, currentRound);
				clocks.put("serverTime", new Date());
				response.getWriter().print(gson.toJson(clocks));
			}

		} 
		
		else if (action.equals("startSimulator")) {
			startSimulator();
			response.getWriter().print("OK");
		} 
		
		else if(action.equals("getGP")){
			response.getWriter().print(gson.toJson(getTimes()));
		}
		
		else if(action.equals("getIncidents"))
		{
			response.getWriter().print(new HomeData().getIncidents());
			
		}
	}

	protected HashMap<String, Object> getTimes() {
		TblGeneralParametersDaoImpl dao = new TblGeneralParametersDaoImpl();
		HashMap<String, Object> timesMap = new HashMap<String, Object>();

		timesMap.put("sessionTime", dao.getSessionTime());
		timesMap.put("roundTime", dao.getRoundTime());
		timesMap.put("numOfRounds", dao.getGeneralParameters().getNumOfRounds());
		timesMap.put("pauseTime", dao.getGeneralParameters().getPauseTime());
		timesMap.put("runTime", dao.getGeneralParameters().getRunTime());
		timesMap.put("sessionsPerRound", dao.getGeneralParameters().getSessionsPerRound());
		return timesMap;
	}

	public void startSimulator() {
		TimerManager.startSimulator();

	}
	
}
