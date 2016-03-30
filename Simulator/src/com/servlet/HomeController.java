package com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.ClockManager;

import com.dao.TblCourseDao;
import com.dao.TblGeneralParametersDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblCourse;

/**
 * Servlet implementation class HomeController
 */
@WebServlet("/HomeController")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext application = getServletConfig().getServletContext();
	private ClockManager clockManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	protected HashMap<String,Object> getTimes() 
	{
		TblGeneralParametersDao dao = new TblGeneralParametersDao();
		HashMap<String, Object> timesMap = new HashMap<String, Object>();
		
		timesMap.put("sessionTime", dao.getSessionTime());
		timesMap.put("roundTime", dao.getRoundTime());
		timesMap.put("numOfRounds", dao.getGeneralParameters().getNumOfRounds());
		timesMap.put("pauseTime", dao.getGeneralParameters().getPauseTime());
		timesMap.put("runTime", dao.getGeneralParameters().getRunTime());
		timesMap.put("sessionsPerRound", dao.getGeneralParameters().getSessionsPerRound());
		
		return timesMap;
	}
	
	public void startSimulator(String courseName){
		int runTime=(int) getTimes().get("runTime");
		int roundTime = (int) getTimes().get("roundTime");
		
		TblCourseDao dao = new TblCourseDao();	
		int currentRound = dao.getCourseById(courseName).getLastRoundDone();
		clockManager = new ClockManager(runTime, roundTime, currentRound);
		clockManager.run();
	}
	
	protected void getServerClocks (HttpServletResponse response) throws IOException{
		
		HashMap<String, Object> clocks = new HashMap<String, Object>();
		clocks.put("elapsedClock", clockManager.getElapsedClock());
		clocks.put("remainingClock", clockManager.getRemainingClock());
		
		response.getWriter().print(clocks);
	}
}
