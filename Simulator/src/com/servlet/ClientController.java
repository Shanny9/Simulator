package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import utils.SolutionElement;

/**
 * Servlet implementation class ClientController
 */
@WebServlet("/ClientController")
public class ClientController extends HttpServlet {
	private String team;
	private int inc_id;
	private int time;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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

		// get the action
		String action = request.getParameter("action");
		boolean isBaught = false;
		switch (action) {
		case "getSolutions":
			// System.out.println("ClientController: getSolutions= " +
			// getSolutions());
			response.getWriter().print(new GsonBuilder().setPrettyPrinting().create().toJson(getSolutions()));
			break;
		case "checkIncident":
			team = request.getParameter("team");
			inc_id = Integer.valueOf(request.getParameter("inc_id"));
			time = Integer.valueOf(request.getParameter("time"));
			boolean isGood = SimulationLog.getInstance().checkIncident(team, inc_id, time);
			response.getWriter().print(isGood);
			break;
		case "buySolution":
			isBaught = true;
		case "sendSolution":
			team = request.getParameter("team");
			inc_id = Integer.valueOf(request.getParameter("inc_id"));
			time = ClockIncrementor.getRunTime();
//			time = Integer.valueOf(request.getParameter("time"));
			SimulationLog.getInstance().incidentSolved(team, inc_id, time, isBaught);
			log.SimulationLog.getInstance().getSolutionQueue().offer(new SolutionLog(team, inc_id));
			response.getWriter().print(true);
			break;
		case "checkSimulator":
			while (!ClockIncrementor.isRunning()) {
				synchronized (this) {
					try {
						wait(1000);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
			response.getWriter().print(true);
			break;
		}
	}

	private HashMap<Integer, SolutionElement> getSolutions() {
		HashMap<Integer, SolutionElement> solutions = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.solutionsForClient);
			while (rs.next()) {
				solutions.put(rs.getInt("incident_id"),
						new SolutionElement(rs.getInt("incident_id"), rs.getInt("solution_marom"),
								rs.getInt("solution_rakia"), rs.getDouble("solution_cost"), rs.getString("currency")));
			}
			return solutions;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
