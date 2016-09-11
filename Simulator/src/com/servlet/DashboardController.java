package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import log.LogUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import vis_utils.DataMaker;

import com.dao.TblServiceDao;
import com.daoImpl.TblServiceDaoImpl;
import com.model.TblService;

/**
 * Servlet implementation class DashboardController
 */
@WebServlet("/DashboardController")
public class DashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DashboardController() {
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
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		String action = request.getParameter("action");
		String course = request.getParameter("courseName");
		switch (action) {
		case "getPieData":
			response.getWriter().print(DataMaker.getTeamMT(course, 1));
			break;
		// case "getMTBFforLineChart":
		// Integer selectedService =
		// Integer.parseInt(request.getParameter("service"));
		// response.getWriter().print(DataMaker.getMTBFforLineChart("17-08-16",
		// selectedService));
		// break;
		case "getMTBFPerRound":
			Integer selectedService1 = Integer.parseInt(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTBFPerRound(course, selectedService1));
			break;
		case "getMTBFPerTeam":
			Integer selectedService2 = Integer.parseInt(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTBFPerTeam(course, selectedService2));
			break;
		case "getMTBFPerService":
			Integer round = Integer.parseInt(request.getParameter("round"));
			response.getWriter().print(
					DataMaker.getMTBFPerService(course, round));
			break;
		case "getServiceList":
			TblServiceDao serviceDao = new TblServiceDaoImpl();
			List<TblService> services = serviceDao.getAllServices();
			JSONArray result = new JSONArray();
			for (TblService service : services) {
				JSONObject jsonService = new JSONObject();
				jsonService.put("id", service.getServiceId());
				jsonService.put("text", service.getServiceName());
				result.add(jsonService);
			}
			response.getWriter().print(result);
			break;
		case "getRoundList":
			int lastRoundDone = LogUtils.openSettings(course)
					.getLastRoundDone();
			JSONArray roundsList = new JSONArray();
			for (int r = 0; r < lastRoundDone; r++) {
				JSONObject jsonRound = new JSONObject();
				jsonRound.put("id", (r+1));
				jsonRound.put("text","Round " + (r+1));
				roundsList.add(jsonRound);
			}
			response.getWriter().print(roundsList);
			break;
		case "getMTRSPieData":
			Integer mtrsServices = Integer.parseInt(request
					.getParameter("service"));
			Integer selectedRound = Integer.parseInt(request
					.getParameter("round"));
			String selectedTeam = request
					.getParameter("team");

			ArrayList<String> ranges  = new ArrayList<>(Arrays.asList(new String[]{"1-2","2-3","3-4","4-5"}));
			response.getWriter().print(
					DataMaker.getMTRSPieData(course, selectedTeam, selectedRound, mtrsServices, ranges));
			
		}

	}
}
