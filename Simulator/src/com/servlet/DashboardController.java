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
			Integer selectedServiceMTBF1 = Integer.parseInt(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTBFPerRound(course, selectedServiceMTBF1));
			break;
		case "getMTRSPerRound":
			Integer selectedServiceMTRS1 = Integer.parseInt(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTRSPerRound(course, selectedServiceMTRS1));
			break;
		case "getMTBFPerTeam":
			Integer selectedServiceMTBF2 = Integer.parseInt(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTBFPerTeam(course, selectedServiceMTBF2));
			break;
		case "getMTRSPerTeam":
			Integer selectedServiceMTRS2 = Integer.parseInt(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTRSPerTeam(course, selectedServiceMTRS2));
			break;
		case "getMTBFPerService":
			Integer roundMTBF = Integer.parseInt(request.getParameter("round"));
			response.getWriter().print(
					DataMaker.getMTBFPerService(course, roundMTBF));
			break;
		case "getMTRSPerService":
			Integer roundMTRS = Integer.parseInt(request.getParameter("round"));
			response.getWriter().print(
					DataMaker.getMTRSPerService(course, roundMTRS));
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
			String[] strRanges = request.getParameterValues("ranges[]");
			ArrayList<String> ranges  = new ArrayList<>(Arrays.asList(strRanges));
			response.getWriter().print(
					DataMaker.getMTRSPieData(course, selectedTeam, selectedRound, mtrsServices, ranges));
			
		}

	}
}
