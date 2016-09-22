package com.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.jdbc.DBUtility;

import log.SimulationLog;
import log.SolutionLog;
import utils.ClockIncrementor;
import utils.Queries;
import utils.SimulationTime;
import utils.SolutionElement;

/**
 * Servlet implementation class ClientController
 */
@WebServlet("/ClientController")
public class ClientController extends HttpServlet {
	// TODO: get this from client in checkSimulator
	/**
	 * The name of the course retrieved from the user interface.
	 */
	private String courseName;
	/**
	 * The team of the user retrieved from the user interface.
	 */
	private String team;
	/**
	 * The incident ID retrieved from the user interface.
	 */
	private byte inc_id;
	/**
	 * The current simulation <b>server time</b> retrieved from the
	 * {@code ClockIncrementor}.
	 */
	private SimulationTime time;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientController() {
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
		// General settings
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		// check that courseName is set
		Object selectedCourse = getServletContext().getAttribute(
				"selectedCourseName");
		if (courseName == null && selectedCourse != null) {
			courseName = (String) selectedCourse;
			System.out.println("ClientController: Course '" + courseName
					+ "' is selcted.");
		}

		// get the action
		String action = request.getParameter("action");
		boolean isBaught = false;
		switch (action) {
		case "getSolutions":
			// System.out.println("ClientController: getSolutions= " +
			// getSolutions());
			response.getWriter().print(
					new GsonBuilder().setPrettyPrinting().create()
							.toJson(getSolutions()));
			break;
		case "checkIncident":
			team = request.getParameter("team");
			inc_id = Byte.valueOf(request.getParameter("inc_id"));
			time = ClockIncrementor.getSimTime();
			boolean isGood = SimulationLog.getInstance().checkIncident(
					SimulationLog.getTeamConst(team), inc_id, time);
			// System.out.println("ClientController: " + team + " Inc:" + inc_id
			// + " Time:" + time + " isGood:" + isGood);
			response.getWriter().print(isGood);
			break;
		case "buySolution":
			isBaught = true;
		case "sendSolution":
			team = request.getParameter("team");
			inc_id = Byte.valueOf(request.getParameter("inc_id"));
			time = ClockIncrementor.getSimTime();

			SimulationLog.getInstance().incidentSolved(
					SimulationLog.getTeamConst(team), inc_id, time, isBaught);
			log.SimulationLog.getInstance().addSolution(new SolutionLog(courseName, team, inc_id));
			response.getWriter().print(true);
			break;
		case "checkSimulator":
			while (!ClockIncrementor.isRunning()) {
				synchronized (this) {
					try {
						wait(1000);
					} catch (Throwable e) {
						// e.printStackTrace();
					}
				}
			}
			response.getWriter().print(true);
			break;

		case "pauseOrResume":
			while (log.SimulationLog.getServerPaused() == log.SimulationLog
					.getClientPaused()) {
				synchronized (this) {
					try {
						wait(1000);
					} catch (Throwable e) {
						// e.printStackTrace();
					}
				}
			}
			response.setContentType("text/event-stream");
			response.setCharacterEncoding("UTF-8");
			String pauseMsg = "retry: 1000\ndata: "
					+ ((log.SimulationLog.getServerPaused()) ? "pause"
							: "resume") + "\n\n";
			response.getWriter().write(pauseMsg);
			log.SimulationLog.setClientPaused(log.SimulationLog
					.getServerPaused());
			break;
		}
	}

	/**
	 * 
	 * @return A {@code HashMap} (key= {@code incident_id}, value=
	 *         {@code SolutionElement}) of all solutions including relevant data
	 *         for the client screen.
	 */
	private HashMap<Integer, SolutionElement> getSolutions() {
		HashMap<Integer, SolutionElement> solutions = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.solutionsForClient);
			while (rs.next()) {
				solutions.put(
						rs.getInt("incident_id"),
						new SolutionElement(rs.getInt("incident_id"), rs
								.getInt("solution_marom"), rs
								.getInt("solution_rakia"), rs
								.getDouble("solution_cost"), rs
								.getString("currency")));
			}
			return solutions;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
