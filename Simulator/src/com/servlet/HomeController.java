package com.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import log.LogUtils;
import log.Settings;
import log.SimulationLog;
import log.SolutionLog;
import utils.ClockIncrementor;
import utils.PasswordAuthentication;
import utils.Queries;
import utils.TimerManager;

import com.dao.TblGeneralParametersDao;
import com.daoImpl.TblGeneralParametersDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jdbc.DBUtility;
import com.model.TblGeneral_parameter;

/**
 * Servlet implementation class HomeController
 */
@WebServlet("/HomeController")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String courseName;
	private int round;
	private SimulationLog simLog;
	private Settings settings;
	private String selectedCourseName;

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
		// System.out.println("action= " + action);

		switch (action) {

		case "authenticate":

			response.setContentType("text/html");

			switch (authenticate(request)) {
			case 1:
				request.getSession().setAttribute("isLogged", "1");
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
			courseName = request.getParameter("courseName"); // TODO: replace with selectedCourseName
			round = Integer.valueOf(request.getParameter("round"));
			settings = SimulationLog.getInstance(courseName).getSettings(); // TODO: remove this
			if (settings != null) {
				int runTime = settings.getRunTime();
				int roundTime = settings.getRoundTime();
				int pauseTime = settings.getPauseTime();

				TimerManager.startSimulator(settings, round);
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
			SimulationLog.getInstance(courseName); // TODO: remove this
			System.out.println("getGP");
			settings = simLog.getSettings(); // TODO: remove this
			response.getWriter().print(gson.toJson(settings));
			break;
		case "getEvents":
			response.getWriter().print(SimulationLog.getInstance(courseName).getEventsForHomeScreen());
			break;
		case "solutionStream":
			prepareResponseToStream(response);
			LinkedList<SolutionLog> solutionQueue = log.SimulationLog.getInstance(courseName).getSolutionQueue();
			if (!solutionQueue.isEmpty()) {
				response.getWriter().write(toStream(solutionQueue.poll()));
			}
			break;

		case "profitStream":
			prepareResponseToStream(response);
			int currentTime = ClockIncrementor.getRunTime();
			// HashMap<String, Double> profits =
			// SimulationLog.getInstance().getTeamProfits(currentTime);
			HashMap<String, Double> profits = SimulationLog.getInstance(courseName).getTeamScores(currentTime);

			String streamMessage = "retry: 1000\ndata: [{\"team\": \"marom\", \"profit\": \""
					+ profits.get("Marom").intValue() + "\"}, ";
			streamMessage += "{\"team\": \"rakia\", \"profit\": \"" + profits.get("Rakia").intValue() + "\"}]\n\n";
			response.getWriter().write(streamMessage);
			break;

		case "newCourse":
			String courseName = request.getParameter("form-courseName");
			int rounds = Integer.valueOf(request.getParameter("form-numOfRounds"));
			int runTime = Integer.valueOf(request.getParameter("form-runTime"));
			int pauseTime = Integer.valueOf(request.getParameter("form-pauseTime"));
			int sessionsPerRound = Integer.valueOf(request.getParameter("form-sessions"));
			double initCapital = Double.valueOf(request.getParameter("form-initCapital"));

			Settings s = new Settings(courseName, rounds, runTime, pauseTime, sessionsPerRound, initCapital);
			new Thread(new log.SimulationTester(s)).start();

			response.sendRedirect("newCourse.jsp");
			break;
		case "checkLog":
			response.getWriter().write(gson.toJson(LogUtils.getCourseRounds(request.getParameter("directory"))));
			break;
		case "getCourses":
			response.getWriter().write(gson.toJson(LogUtils.getCourses()));
			break;

		case "selectCourse":
			selectedCourseName = request.getParameter("form-courseName");
			settings = SimulationLog.getInstance(selectedCourseName).getSettings();
			response.sendRedirect("index.jsp");
			break;
		}
	}

	private void prepareResponseToStream(HttpServletResponse response) {
		// content type must be set to text/event-stream
		response.setContentType("text/event-stream");

		// encoding must be set to UTF-8
		response.setCharacterEncoding("UTF-8");

	}

	/*
	 * protected HashMap<String, Object> getTimes(String courseName) {
	 * TblGeneralParametersDao daoGP = new TblGeneralParametersDaoImpl();
	 * HashMap<String, Object> timesMap = new HashMap<String, Object>();
	 * 
	 * TblCourseDaoImpl daoCourse = new TblCourseDaoImpl(); TblCourse course =
	 * daoCourse.getCourseById(courseName); int round =
	 * course.getLastRoundDone() + 1;
	 * 
	 * timesMap.put("sessionTime", daoGP.getSessionTime());
	 * timesMap.put("roundTime", daoGP.getRoundTime());
	 * timesMap.put("numOfRounds",
	 * daoGP.getGeneralParameters().getNumOfRounds()); timesMap.put("pauseTime",
	 * daoGP.getGeneralParameters().getPauseTime()); timesMap.put("runTime",
	 * daoGP.getGeneralParameters().getRunTime());
	 * timesMap.put("sessionsPerRound",
	 * daoGP.getGeneralParameters().getSessionsPerRound());
	 * timesMap.put("totalTime", daoGP.getTotalTime());
	 * timesMap.put("currentRound", round new
	 * TblCourseDaoImpl().getCourseById(courseName). getLastRoundDone()+1 );
	 * return timesMap; }
	 */

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
		request.removeAttribute("form-password"); // for security
		request.removeAttribute("form-username");

		TblGeneralParametersDao daoGP = new TblGeneralParametersDaoImpl();
		TblGeneral_parameter gp = daoGP.getGeneralParameters();

		PasswordAuthentication au = new PasswordAuthentication(); // default
																	// cost is
																	// 16

		// Admin
		if (au.authenticate(user, gp.getHomeUser()) && au.authenticate(pass, gp.getHomePass())) {
			result = 1;
		}
		// Team
		else {
			// Marom
			if (String.valueOf(user).equals("Marom") && String.valueOf(pass).equals("m"))
				result = 2;

			else {
				// Rakia
				if (String.valueOf(user).equals("Rakia") && String.valueOf(pass).equals("r"))
					result = 3;

				// Invalid details
				else
					result = 0;
			}

		}
		gp = null;
		emptyChar(pass);
		emptyChar(user);
		return result;

	}

	private void emptyChar(char[] arr) {
		for (int i = 0; i < arr.length; i++)
			arr[i] = 0;
	}

	private String toStream(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(obj);
		String[] rows = json.split("\n");
		for (int i = 0; i < rows.length; i++) {
			rows[i] = "data: " + rows[i];
		}

		String streamMessage = String.join("\n", rows);
		streamMessage += "\n\n";

		return streamMessage;
	}

//	private List<JsonObject> getEvents(Settings settings) {
//
//		List<JsonObject> events = new ArrayList<JsonObject>();
//		String query = Queries.eventsForHomeTable;
//		try {
//			Statement stmt = DBUtility.getConnection().createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			while (rs.next()) {
//				JsonObject row = new JsonObject();
//				row.addProperty("time", rs.getInt("incidentTime"));
//				row.addProperty("event_id", rs.getInt("event_id"));
//				events.add(row);
//			}
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
//		return events;
//	}

	/*
	 * more bug fixes. @HomeController: convertToSimulTime method fixed + new method - stretch
	 */
}
