package com.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import log.LogUtils;
import log.SimulationLog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.ClockIncrementor;
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
	 * The CI ID retrieved from the user interface.
	 */
	private int question_id;
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
	@SuppressWarnings("unchecked")
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
		if (action == null || action.isEmpty()) {
			return;
		}
		switch (action) {
		case "getPriceList":
			JSONArray questionCosts = new JSONArray();
			HashMap<Byte, SolutionElement> solutions = LogUtils
					.getCiSolutions();
			for (SolutionElement sol : solutions.values()) {
				JSONObject questionCost = new JSONObject();
				questionCost.put("question", sol.getQuestion_id());
				questionCost.put("cost", sol.getSolution_cost());
				questionCost.put("currency", sol.getCurrency());
				questionCosts.add(questionCost);
			}
			response.getWriter().print(questionCosts);
			break;
		case "buySolution":
			isBaught = true;
		case "sendSolution":
			int question_id = 0;
			int solution = 0;

			if (!isBaught){
				try{
					solution = Integer.valueOf(request.getParameter("solution"));
				} catch (NumberFormatException e){
					// solution is not an int
					return;
				}
			}
			
			try {
				question_id = Integer.valueOf(request
						.getParameter("question_id"));
			} catch (NumberFormatException e) {
				// question id is not an int
				return;
			}

			team = request.getParameter("team");
			time = ClockIncrementor.getSimRunTime();
			boolean isSolved = SimulationLog.getInstance().checkSolution(
					courseName, team, question_id, time, solution, isBaught);
			JSONObject result = new JSONObject();
			result.put("message", isSolved);
			response.getWriter().print(result);
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
		case "checkEndSimulator":
			while (ClockIncrementor.isRunning()) {
				synchronized (this) {
					try {
						wait(1000);
					} catch (Throwable e) {
						// e.printStackTrace();
					}
				}
			}
			response.getWriter().print(false);
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
}
