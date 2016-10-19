package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import log.FilesUtils;

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
		
		if (action == null){
			return;
		}
		
		switch (action) {
		case "getHeaderData":
			Integer roundHeader = Integer.parseInt(request.getParameter("round"));
			response.getWriter().print(DataMaker.getGeneralData(roundHeader));
			break;
		case "getPieData":
			response.getWriter().print(DataMaker.getTeamMT(course, 1));
			break;
		case "getMTBFPerRound":
			Byte selectedServiceMTBF1 = Byte.parseByte(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTBFPerRound(course, selectedServiceMTBF1));
			break;
		case "getMTRSPerRound":
			Byte selectedServiceMTRS1 = Byte.parseByte(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTRSPerRound(course, selectedServiceMTRS1));
			break;
		case "getMTBFPerTeam":
			Byte selectedServiceMTBF2 = Byte.parseByte(request
					.getParameter("service"));
			response.getWriter().print(
					DataMaker.getMTBFPerTeam(course, selectedServiceMTBF2));
			break;
		case "getMTRSPerTeam":
			Byte selectedServiceMTRS2 = Byte.parseByte(request
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
		case "getBizUnitsHierachical":
			response.getWriter().print(DataMaker.getBizUnits(true, true,(byte)0));
			break;
		case "getBizUnitsTitles":
			response.getWriter().print(DataMaker.getBizUnits(false, false,(byte)0));
			break;
		case "generateITBudgetBreakdown":
			Integer roundBudget = Integer.parseInt(request
					.getParameter("round"));
			String teamBudget = request.getParameter("team");
			Byte serviceBudget = Byte
					.parseByte(request.getParameter("service"));

			response.getWriter().print(DataMaker.generateITBudgetBreakdown(course, roundBudget,
					teamBudget, serviceBudget, true));
/*			File file = new File("C:" + File.separator + "SIMULATOR"
						+ File.separator + "ITBudgetBreakdown.csv");
			response.setContentType("application/csv");
			response.setHeader("content-disposition","filename="+"ITBudgetBreakdown.csv");
			response.getWriter().print(file);*/
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
			Set<Integer> roundsDone = FilesUtils.openSettings(course)
					.getRoundsDone();
			JSONArray roundsList = new JSONArray();
			for (int r : roundsDone) {
				JSONObject jsonRound = new JSONObject();
				jsonRound.put("id", r);
				jsonRound.put("text", "Round " + r);
				roundsList.add(jsonRound);
			}
			response.getWriter().print(roundsList);
			break;
		case "getMTRSPieData":
			Byte mtrsServices = Byte.parseByte(request.getParameter("service"));
			Integer selectedRound = Integer.parseInt(request
					.getParameter("round"));
			String selectedTeam = request.getParameter("team");
			String[] strRanges = request.getParameterValues("ranges[]");
			ArrayList<String> ranges = new ArrayList<>(Arrays.asList(strRanges));
			response.getWriter().print(
					DataMaker.getMTRSPieData(course, selectedTeam,
							selectedRound, mtrsServices, ranges));
		}

	}
}
