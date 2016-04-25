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

import com.daoImpl.TblCIDaoImpl;
import com.google.gson.GsonBuilder;
import com.jdbc.DBUtility;

import log.SimulationLog;
import utils.Queries;
import utils.SolutionElement;
import utils.TimerManager;

/**
 * Servlet implementation class ClientController
 */
@WebServlet("/ClientController")
public class ClientController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private static HashMap<TblResolutionPK, TblResolution> resolutions = new
	// HashMap<>();

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
			response.getWriter().print(new GsonBuilder().setPrettyPrinting().create().toJson(getSolutions()));
			break;
		case "buySolution":
			isBaught = true;
		case "sendSolution":
			String team = request.getParameter("team");
			int ci_id = Integer.valueOf(request.getParameter("incID"));
			int time = Integer.valueOf(request.getParameter("time"));
			SimulationLog.getInstance().updateCILog(team, ci_id, time, isBaught);
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
