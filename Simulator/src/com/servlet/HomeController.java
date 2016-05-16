package com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.TblGeneralParametersDao;
import com.daoImpl.TblCourseDaoImpl;
import com.daoImpl.TblGeneralParametersDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblCourse;
import com.model.TblGeneral_parameter;

import log.Settings;
import log.SimulationLog;
import log.SolutionLog;
import utils.HomeData;
import utils.PasswordAuthentication;
import utils.TimerManager;

/**
 * Servlet implementation class HomeController
 */
@WebServlet("/HomeController")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String courseName;
	private int round;

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

		String action = request.getParameter("action");
		switch (action) {

		case "authenticate":

			response.setContentType("text/html");
			
			switch(authenticate(request)){
			case 1:
				response.sendRedirect("index.jsp");
				break;
			case 2:
				request.getSession().setAttribute("team", "Marom");
				response.sendRedirect("client.jsp");
				break;
			case 3:
				request.getSession().setAttribute("team", "Rakia");
				response.sendRedirect("client.jsp");
				break;
			case 0:
				request.getSession().setAttribute("err", "1");
				response.sendRedirect("login.jsp");
				break;
			}

			break;

		case "getTime":
			HashMap<String, Object> clocks = TimerManager.getClocks();
			clocks.put("serverTime", new Date());
			response.getWriter().print(gson.toJson(clocks));
			break;
		case "startSimulator":
			courseName = request.getParameter("courseName");
			round = Integer.valueOf(request.getParameter("round"));
			TblCourse course = new TblCourseDaoImpl().getCourseById(courseName);
			if (course != null) {
				int runTime = (int) getTimes(courseName).get("runTime");
				int roundTime = (int) getTimes(courseName).get("roundTime");
				int pauseTime = (int) getTimes(courseName).get("pauseTime");
				int sessionTime = (int) getTimes(courseName).get("sessionTime");

				log.SimulationLog.getInstance();
				TimerManager.startSimulator(courseName, runTime, roundTime, round, pauseTime, sessionTime);
				response.getWriter().print("OK");
			}
			break;
		case "pauseSimulator":
			TimerManager.forcePause();
			break;
		case "resumeSimulator":
			TimerManager.forceResume();
			break;
		case "getGP":
			courseName = request.getParameter("courseName");
			response.getWriter().print(gson.toJson(getTimes(courseName)));
			break;
		case "getEvents":
			response.getWriter().print(new HomeData().getEvents());
			break;
		case "solutionStream":
			// content type must be set to text/event-stream
			response.setContentType("text/event-stream");

			// encoding must be set to UTF-8
			response.setCharacterEncoding("UTF-8");

			LinkedList<SolutionLog> solutionQueue = log.SimulationLog.getInstance().getSolutionQueue();
			if (!solutionQueue.isEmpty()) {
				String json = gson.toJson(solutionQueue.poll());
				String[] rows = json.split("\n");
				for (int i= 0 ; i <rows.length ; i++){
					rows[i] = "data: " + rows[i];
				}
				String streamMessage = String.join("\n", rows);
				response.getWriter().write(streamMessage + "\n\n");
			}
			break;
			
		case "profitStream":
			// content type must be set to text/event-stream
			response.setContentType("text/event-stream");

			// encoding must be set to UTF-8
			response.setCharacterEncoding("UTF-8");

			int maromProfit = (int) SimulationLog.getInstance().getTeam("marom").getCurrentProfit();
			int rakiaProfit = (int) SimulationLog.getInstance().getTeam("rakia").getCurrentProfit();

			String streamMessage = "retry: 1000\ndata: [{\"team\": \"marom\", \"profit\": \"" + maromProfit + "\"}, ";
			streamMessage += "{\"team\": \"rakia\", \"profit\": \"" + rakiaProfit + "\"}]\n\n";
			response.getWriter().write(streamMessage);
			break;
			
		case "newCourse":
			String courseName = request.getParameter("form-courseName");
			int rounds = Integer.valueOf(request.getParameter("form-numOfRounds"));
			int runTime = Integer.valueOf(request.getParameter("form-runTime"));
			int pauseTime = Integer.valueOf(request.getParameter("form-pauseTime"));
			int sessionsPerRound = Integer.valueOf(request.getParameter("form-sessions"));
			double initCapital = Double.valueOf(request.getParameter("form-initCapital"));
			
			log.LogUtils.saveSettings(new Settings(courseName, rounds, runTime, pauseTime, sessionsPerRound, initCapital));
			response.sendRedirect("newCourse.jsp");
			break;
		}
	}

	protected HashMap<String, Object> getTimes(String courseName) {
		TblGeneralParametersDao daoGP = new TblGeneralParametersDaoImpl();
		HashMap<String, Object> timesMap = new HashMap<String, Object>();

		TblCourseDaoImpl daoCourse = new TblCourseDaoImpl();
		TblCourse course = daoCourse.getCourseById(courseName);
		int round = course.getLastRoundDone() + 1;

		timesMap.put("sessionTime", daoGP.getSessionTime());
		timesMap.put("roundTime", daoGP.getRoundTime());
		timesMap.put("numOfRounds", daoGP.getGeneralParameters().getNumOfRounds());
		timesMap.put("pauseTime", daoGP.getGeneralParameters().getPauseTime());
		timesMap.put("runTime", daoGP.getGeneralParameters().getRunTime());
		timesMap.put("sessionsPerRound", daoGP.getGeneralParameters().getSessionsPerRound());
		timesMap.put("totalTime", daoGP.getTotalTime());
		timesMap.put("currentRound",
				round/*
						 * new TblCourseDaoImpl().getCourseById(courseName).
						 * getLastRoundDone()+1
						 */);
		return timesMap;
	}

	/**
	 * uses utils.PasswordAuthentication to verify password from the client
	 * 
	 * @param response
	 * @return 1 - Admin login, 2 - Marom, 3 - Rakia, 0 - None (invalid details)
	 */
	protected int authenticate(HttpServletRequest request) {
		int result;
		char[] user = request.getParameter("form-username").toCharArray();
		char[] pass = request.getParameter("form-password").toCharArray();
		request.removeAttribute("form-password"); //for security
		request.removeAttribute("form-username");
		
		TblGeneralParametersDao daoGP = new TblGeneralParametersDaoImpl();
		TblGeneral_parameter gp = daoGP.getGeneralParameters();

		PasswordAuthentication au = new PasswordAuthentication(); // default cost is 16
		
		//Admin
		if(au.authenticate(user, gp.getHomeUser()) && au.authenticate(pass, gp.getHomePass()))
		{
			result = 1;
		}
		//Team
		else
		{
			//Marom
			if(String.valueOf(user).equals("Marom") && String.valueOf(pass).equals("m") )
				result = 2;
			
			else 
			{ 
				//Rakia
				if(String.valueOf(user).equals("Rakia") && String.valueOf(pass).equals("r"))
					result = 3;
			
				//Invalid details
				else
					result = 0;
			}
			
		}
		
		gp = null;
		emptyChar(pass);
		emptyChar(user);
		return result;

	}
	
	
	
	private void emptyChar(char[] arr){
		for (int i = 0; i < arr.length; i++)
			arr[i] = 0;
	}

	// private String toJSON(SolutionLog sl){
	// String team = "\"" + sl.getTeam() + "\"";
	// String events = "\"events\":[";
	// for (Integer event_id : sl.getEvents()){
	// events+="{\"event_id\":" + "\"" + event_id + "\"" + "}";
	// }
	// events+="]";
	//// HashSet<Integer> events = "\"" + sl.getEvents() + "\"";;
	//
	// String message = "data: {\ndata: \"team\": " + team + ",\ndata:
	// \"inc_id\": " + inc_id + "\ndata: }\n\n";
	// }
}
