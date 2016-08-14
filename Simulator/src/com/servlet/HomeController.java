package com.servlet;

import java.io.File;
import java.io.FilenameFilter;
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
import com.daoImpl.TblGeneralParametersDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblGeneral_parameter;

import log.LogUtils;
import log.Settings;
import log.SimulationLog;
import log.SimulationTester;
import log.SolutionLog;
import utils.ClockIncrementor;
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
		// if(LogUtils.path.isEmpty())
		// {
		// ServletContext context = getServletConfig().getServletContext();
		// URL resourceUrl =
		// context.getResource("WEB-INF"+File.separator+"logs");
		// LogUtils.path = resourceUrl.getPath();
		// }
		LogUtils.path = getServletContext().getRealPath(File.separator + "logs");

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
				/*
				 * if(getServletContext().getAttribute("isLogged")!=null)
				 * response.sendRedirect("login.jsp"); else{
				 */
				request.getSession().setAttribute("isLogged", "1");
				// getServletContext().setAttribute("isLogged", "1");
				response.sendRedirect("opening.jsp");
				// }
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
			System.out.println("getTimes: " + new Date());
			HashMap<String, Object> clocks = TimerManager.getClocks();
			clocks.put("serverTime", new Date());
			response.getWriter().print(gson.toJson(clocks));
			break;
		case "startSimulator":
			// courseName = request.getParameter("courseName"); // V replace
			// with selectedCourseName
			// round = Integer.valueOf(request.getParameter("round"));
			// settings = SimulationLog.getInstance(courseName).getSettings();
			// // V remove this
			if (settings != null) {
				int runTime = settings.getRunTime();
				int roundTime = settings.getRoundTime();
				int pauseTime = settings.getPauseTime();

				if (!ClockIncrementor.isRunning()) {
					TimerManager.startSimulator(settings, round);
					response.getWriter().print("OK");
				}
			}
			break;
		case "getSettings": // TODO: check that it happens after start simulator
			// courseName = request.getParameter("courseName");
			// settings = LogUtils.openSettings(courseName); // V remove this
			response.getWriter().print(gson.toJson(settings));
			break;
		case "getEvents":
			response.getWriter().print(SimulationLog.getInstance().getEventsForHomeScreen());
			break;
		case "solutionStream":
			if (!ClockIncrementor.isRunning()) {
				return;
			}

			prepareResponseToStream(response);
			LinkedList<SolutionLog> solutionQueue = log.SimulationLog.getInstance().getSolutionQueue();
			if (!solutionQueue.isEmpty()) {
				response.getWriter().write(toStream(solutionQueue.poll()));
			}
			break;

		case "profitStream":
			if (!ClockIncrementor.isRunning()) {
				return;
			}

			prepareResponseToStream(response);
			int currentTime = ClockIncrementor.getRunTime();
			// HashMap<String, Double> profits =
			// SimulationLog.getInstance().getTeamProfits(currentTime);
			HashMap<String, Double> profits = SimulationLog.getInstance().getTeamScores(currentTime);

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

			Settings set = new Settings(courseName, rounds, runTime, pauseTime, sessionsPerRound, initCapital);
			if (courseName != null) {
				// TODO: why the hell this function is called before the user
				// sent the form?
				SimulationTester st = SimulationTester.getInstance();
				SimulationTester.initialize(set);
				new Thread(st).start();
			}

			response.sendRedirect("newCourse.jsp?action=OK");
			break;
		case "checkLog":
			response.getWriter().write(gson.toJson(LogUtils.getCourseRounds(request.getParameter("directory"))));
			break;
		case "getCourses":
			response.getWriter().write(gson.toJson(LogUtils.getCourses()));
			break;

		case "selectCourse":
			selectedCourseName = request.getParameter("form-courseName");
			round = Integer.valueOf(request.getParameter("form-round"));

			settings = LogUtils.openSettings(selectedCourseName);
			request.getSession().setAttribute("selectedCourseName", selectedCourseName);
			request.getSession().setAttribute("selectedRound", round);
			// for client.jsp - app scope attribute
			// *if Admin changes the course after client.jsp is on, client.jsp
			// need to refresh.
			getServletContext().setAttribute("selectedCourseName", selectedCourseName);
			response.sendRedirect("index.jsp");
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
		}
	}

	private boolean checkFile(String prefix, String course) {
		final String p = prefix;
		File file = new File(LogUtils.path + File.separator + course);
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
//		System.out.println("HomeController: username= " + request.getParameter("form-username"));
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

	// private List<JsonObject> getEvents(Settings settings) {
	//
	// List<JsonObject> events = new ArrayList<JsonObject>();
	// String query = Queries.eventsForHomeTable;
	// try {
	// Statement stmt = DBUtility.getConnection().createStatement();
	// ResultSet rs = stmt.executeQuery(query);
	// while (rs.next()) {
	// JsonObject row = new JsonObject();
	// row.addProperty("time", rs.getInt("incidentTime"));
	// row.addProperty("event_id", rs.getInt("event_id"));
	// events.add(row);
	// }
	// } catch (SQLException e) {
	// System.err.println(e.getMessage());
	// }
	// return events;
	// }

	/*
	 * more bug fixes. @HomeController: convertToSimulTime method fixed + new
	 * method - stretch
	 */
}