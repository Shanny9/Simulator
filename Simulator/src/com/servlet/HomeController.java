package com.servlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import log.FilesUtils;
import log.Settings;
import log.SimulationLog;
import log.SimulationTester;
import log.SolutionLog;
import utils.ClockIncrementor;
import utils.DBValidator;
import utils.PasswordAuthentication;
import utils.SimulationTime;
import utils.TimerManager;

import com.dao.TblGeneralParametersDao;
import com.daoImpl.TblGeneralParametersDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblGeneral_parameter;

/**
 * Servlet implementation class HomeController
 */
@WebServlet("/HomeController")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int round;
	private Settings settings;
	private String selectedCourseName;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomeController() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		FilesUtils.setPath(getServletContext().getRealPath(
				File.separator + "logs"));

		// General settings
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String action = request.getParameter("action");
		switch (action) {

		case "authenticate":

			response.setContentType("text/html");

			switch (authenticate(request)) {
			case 1:
				Object session = getServletContext().getAttribute("isLogged");
				if (session != null) {
					try {
						((HttpSession) session).invalidate();
					} catch (IllegalStateException ise) {
						System.out.println("Session "+((HttpSession) session).getId()+" is invalid now.");
					}
					getServletContext().setAttribute("isLogged",
							request.getSession());
					request.getSession().setAttribute("isLogged", "1");
					response.sendRedirect("opening.jsp");
					System.out.println("New Session "+request.getSession().getId()+" was stored.");
				}
				// response.sendRedirect("login.jsp");
				else {
					request.getSession().setAttribute("isLogged", "1");
					getServletContext().setAttribute("isLogged",
							request.getSession());
					response.sendRedirect("opening.jsp");
				}
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
		case "getTimeToWait":
			JSONObject json = new JSONObject();
			json.put("timeToWait",  ClockIncrementor.getRemainingRoundTime());
			response.getWriter().print(json);
			break;
		case "isAlive":
			response.getWriter().print("yes");
			break;
		case "getTime":
			HashMap<String, Object> clocks = ClockIncrementor.getClocks();
			clocks.put("serverTime", new Date());
			response.getWriter().print(gson.toJson(clocks));
			break;
		case "startSimulator":
			if (settings != null) {
				if (!ClockIncrementor.isRunning()) {
					TimerManager.startSimulator(settings, round);
					response.getWriter().print("OK");
				} else {
					System.err
							.println("startSimulator method failed: The Simulation is already running.");
				}
			} else {
				System.err
						.println("startSimulator method failed: Course settings are missing.");
			}
			break;
		case "getSettings":
			response.getWriter().print(gson.toJson(settings));
			break;
		case "getEvents":
			response.getWriter().print(
					SimulationLog.getInstance().getEventsForHomeScreen());
			break;
		case "getSolutionHistory":
			response.getWriter().write(
					gson.toJson(SimulationLog.getInstance()
							.getSolutionHistory()));
			break;
		case "solutionStream":
			if (!ClockIncrementor.isRunning()) {
				return;
			}

			prepareResponseToStream(response);
			LinkedList<SolutionLog> solutionQueue = log.SimulationLog
					.getInstance().getSolutionQueue();
			if (!solutionQueue.isEmpty()) {
				response.getWriter().write(toStream(solutionQueue.poll()));
			}
			break;
		case "profitStream":

			prepareResponseToStream(response);
			if (!ClockIncrementor.isRunning() || ClockIncrementor.isPauseTime()) {
				return;
			}

			SimulationLog simLogg = SimulationLog.getInstance();

			HashMap<String, Double> profits = simLogg
					.getTeamScores(new SimulationTime(ClockIncrementor
							.getSimRunTime().getRunTime()));
			String streamMessage = "retry: 1000\ndata: [{\"team\": \"marom\", \"profit\": \""
					+ profits.get("Marom").intValue() + "\"}, ";
			streamMessage += "{\"team\": \"rakia\", \"profit\": \""
					+ profits.get("Rakia").intValue() + "\"}]\n\n";
			response.getWriter().write(streamMessage);
			break;

		case "newCourse":

			String courseName = request.getParameter("form-courseName");
			int rounds = Integer.valueOf(request
					.getParameter("form-numOfRounds"));
			int runTime = Integer.valueOf(request.getParameter("form-runTime"));
			int pauseTime = Integer.valueOf(request
					.getParameter("form-pauseTime"));
			int sessionsPerRound = Integer.valueOf(request
					.getParameter("form-sessions"));
			int initCapital = Integer.valueOf(request
					.getParameter("form-initCapital"));

			Settings set = new Settings(courseName, rounds, runTime, pauseTime,
					sessionsPerRound, initCapital);
			if (courseName != null) {
				FilesUtils.saveSettings(set);
				SimulationTester st = SimulationTester.getInstance();
				SimulationTester.initialize(set);
				new Thread(st).start();
			}

			response.sendRedirect("newCourse.jsp?action=OK");
			break;
		case "checkSettings": //checks the max/min number incident in session
			String courseNameCheck = request.getParameter("courseName");
			int roundsCheck = Integer.valueOf(request
					.getParameter("numOfRounds"));
			int runTimeCheck = Integer.valueOf(request.getParameter("runTime"));
			int pauseTimeCheck = Integer.valueOf(request
					.getParameter("pauseTime"));
			int sessionsPerRoundCheck = Integer.valueOf(request
					.getParameter("sessions"));
			int initCapitalCheck = Integer.valueOf(request
					.getParameter("initCapital"));

			Settings setCheck = new Settings(courseNameCheck, roundsCheck, runTimeCheck, pauseTimeCheck,
					sessionsPerRoundCheck, initCapitalCheck);
			SimulationTime.initialize(runTimeCheck, pauseTimeCheck, sessionsPerRoundCheck, roundsCheck);
			String msg = DBValidator.checkSettings(setCheck);
			response.setContentType("text/html");
			response.getWriter().print(msg); // msg == "" means all OK.
			break;
		case "checkLog":
			boolean courseExists = (FilesUtils.openSettings(request
					.getParameter("directory")) != null) ? true : false;

			response.getOutputStream().print(courseExists);
			break;
		case "getRounds":
			int tot_rounds = FilesUtils.openSettings(
					request.getParameter("directory")).getRounds();
			response.getWriter().write(gson.toJson(tot_rounds));
			break;
		case "getCourses":
			response.getWriter().write(gson.toJson(FilesUtils.getCourses()));
			break;

		case "selectCourse":
			selectedCourseName = request.getParameter("form-courseName");
			round = Integer.valueOf(request.getParameter("form-round"));

			settings = FilesUtils.openSettings(selectedCourseName);
			request.getSession().setAttribute("selectedCourseName",
					selectedCourseName);
			request.getSession().setAttribute("selectedRound", round);
			// for client.jsp - app scope attribute
			// *if Admin changes the course after client.jsp is on, client.jsp
			// need to refresh.
			getServletContext().setAttribute("selectedCourseName",
					selectedCourseName);
			response.sendRedirect("index.jsp");
			break;

		case "deleteCourse":
			try {
				response.setContentType("text/html");
				String selectedCourseNameDel = request.getParameter("name");
				FilesUtils.deleteCourse(selectedCourseNameDel);
				response.getWriter().print("OK");
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().print("ERR");
			}
			break;
		case "isRoundDone":
			String filePrefix = request.getParameter("filePrefix");
			String course = request.getParameter("course");
			response.getWriter().print(checkFile(filePrefix, course));
			break;
		case "pauseSimulator":
			TimerManager.forcePause();
			log.SimulationLog.setServerPaused(true);
			break;
		case "resumeSimulator":
			TimerManager.forceResume();
			log.SimulationLog.setServerPaused(false);
			break;
		case "getDoneCourses": // courses with at least 1 round done
			ArrayList<String> courses = new ArrayList<String>(
					Arrays.asList(FilesUtils.getCourses()));
			for (int i = 0; i < courses.size(); i++) {
				if (!checkFile("round", courses.get(i)))
					courses.remove(i);
			}
			response.getWriter().write(gson.toJson(courses));
			break;
		}
	}

	// TODO: why is this here and not in FilesUtils?
	private boolean checkFile(String prefix, String course) {
		final String p = prefix;
		File file = new File(FilesUtils.getPath() + File.separator + course);
		if (file.exists() && file.isDirectory()) {

			File[] settingsFiles = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(p);
				}
			});

			if (settingsFiles == null || settingsFiles.length == 0) {
				return false;
			}
			return true;
		} else
			return false;
	}

	/**
	 * Prepares the response to be sent as a stream to the client.
	 * 
	 * @param response
	 *            The session's response.
	 */
	private void prepareResponseToStream(HttpServletResponse response) {
		// content type must be set to text/event-stream
		response.setContentType("text/event-stream");

		// encoding must be set to UTF-8
		response.setCharacterEncoding("UTF-8");

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
		// System.out.println("HomeController: username= " +
		// request.getParameter("form-username"));
		char[] pass = request.getParameter("form-password").toCharArray();
		request.removeAttribute("form-password"); // for security
		request.removeAttribute("form-username");

		TblGeneralParametersDao daoGP = new TblGeneralParametersDaoImpl();
		TblGeneral_parameter gp = daoGP.getGeneralParameters();

		PasswordAuthentication au = new PasswordAuthentication(); // default
																	// cost is
																	// 16

		// Admin
		if (au.authenticate(user, gp.getHomeUser())
				&& au.authenticate(pass, gp.getHomePass())) {
			result = 1;
		}
		// Team
		else {
			// Marom
			if (String.valueOf(user).equals("Marom")
					&& String.valueOf(pass).equals("m"))
				result = 2;

			else {
				// Rakia
				if (String.valueOf(user).equals("Rakia")
						&& String.valueOf(pass).equals("r"))
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

	/**
	 * Empties an {@code Array} of characters for security reasons.
	 * 
	 * @param arr
	 *            An {@code Array} to empty.
	 */
	private void emptyChar(char[] arr) {
		for (int i = 0; i < arr.length; i++)
			arr[i] = 0;
	}

	/**
	 * @param obj
	 *            Object to stream.
	 * @return A {@code String} of text representing the object in stream
	 *         format.
	 */
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
}
