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
import com.dao.TblGeneralParametersDao;
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
	private String courseName;

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
			HashMap<String, Object> clocks = TimerManager.getClocks();
			clocks.put("serverTime", new Date());
			response.getWriter().print(gson.toJson(clocks));

		} else if (action.equals("startSimulator")) {
			courseName = request.getParameter("courseName");
			TblCourse course = new TblCourseDaoImpl().getCourseById(courseName);
			if (course != null) {
				int runTime = (int) getTimes(courseName).get("runTime");
				int roundTime = (int) getTimes(courseName).get("roundTime");
				int pauseTime = (int) getTimes(courseName).get("pauseTime");
				int sessionTime = (int) getTimes(courseName).get("sessionTime");
				int currentRound = course.getLastRoundDone();

				startSimulator(runTime, roundTime, currentRound, pauseTime, sessionTime);
				response.getWriter().print("OK");
			}
		} else if (action.equals("pauseSimulator")){
			pauseSimulator();
			
		} else if (action.equals("resumeSimulator")){
			resumeSimulator();
			
		} else if (action.equals("getGP")) {
			courseName = request.getParameter("courseName");
			response.getWriter().print(gson.toJson(getTimes(courseName)));
		}

		else if (action.equals("getIncidents")) {
			response.getWriter().print(new HomeData().getIncidents());

		}
	}

	protected HashMap<String, Object> getTimes(String courseName) {
		TblGeneralParametersDao daoGP = new TblGeneralParametersDaoImpl();
		HashMap<String, Object> timesMap = new HashMap<String, Object>();
		
		TblCourseDaoImpl daoCourse = new TblCourseDaoImpl();
		TblCourse course = daoCourse.getCourseById(courseName);
		int round = course.getLastRoundDone()+1;
		
		timesMap.put("sessionTime", daoGP.getSessionTime());
		timesMap.put("roundTime", daoGP.getRoundTime());
		timesMap.put("numOfRounds", daoGP.getGeneralParameters().getNumOfRounds());
		timesMap.put("pauseTime", daoGP.getGeneralParameters().getPauseTime());
		timesMap.put("runTime", daoGP.getGeneralParameters().getRunTime());
		timesMap.put("sessionsPerRound", daoGP.getGeneralParameters().getSessionsPerRound());
		timesMap.put("totalTime", daoGP.getGeneralParameters().getTotalTime());
		timesMap.put("currentRound", round/*new TblCourseDaoImpl().getCourseById(courseName).getLastRoundDone()+1*/);
		return timesMap;
	}

	public void startSimulator(int runTime, int roundTime, int currentRound, int pauseTime, int sessionTime) {
		TimerManager.startSimulator(runTime, roundTime, currentRound, pauseTime, sessionTime);
	}
	
	public void pauseSimulator(){
		TimerManager.pauseSimulator();
	}
	
	public void resumeSimulator(){
		TimerManager.resumeSimulator();
	}
}
