<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Financial Reports</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="css/font-awesome.min.css" rel="stylesheet">
<!-- NProgress -->
<link href="css/nprogress.css" rel="stylesheet">
<!-- iCheck -->
<link href="css/iCheck-blue.css" rel="stylesheet">


<!-- Select2 -->
<link href="css/select2.min.css" rel="stylesheet">

<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">

<!-- Sequences Style -->
<link href="css/sequences.css" rel="stylesheet">


<style type="text/css">
.legend li span {
	display: inline-block;
	width: 12px;
	height: 12px;
	margin-right: 5px;
}

li {
	list-style: none;
}
</style>

<!-- jQuery -->
<script src="js/jquery-1.11.3.min.js"></script>
<!-- logout -->
<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<%
	if (session.getAttribute("isLogged") == null) {
		response.sendRedirect("login.jsp");
		return;
	}
%>
<!-- course selection -->
<%
	String requestCourse = request.getParameter("form-courseName");
	Object sessionCourse = session.getAttribute("selectedCourseReports");
	if (requestCourse == null && sessionCourse == null) {
		response.sendRedirect("selectCourse_reports.jsp");
	} else {
		if(sessionCourse == null || (sessionCourse != null && requestCourse != null) ){
			session.setAttribute("selectedCourseReports", requestCourse);
		}
%>

<script type="text/javascript">
				var courseName = '<%=session.getAttribute("selectedCourseReports")%>';

	console.log("Course Selected: " + courseName);
</script>
<%
	}
%>

</head>

<body class="nav-md">
	<div class="container body">
		<div class="main_container">
			<!-- Side Menu -->
			<%@ include file="sideMenu.jsp"%>

			<!-- top navigation -->
			<div class="top_nav">
				<div class="nav_menu">
					<nav>
						<div class="nav toggle">
							<a id="menu_toggle"><i class="fa fa-bars"></i></a>
						</div>

<!-- 						<ul class="nav navbar-nav navbar-right">
							<li class=""><a href="javascript:;"
								class="user-profile dropdown-toggle" data-toggle="dropdown"
								aria-expanded="false"> <img src="images/img.jpg" alt="">John
									Doe <span class=" fa fa-angle-down"></span>
							</a>
								<ul class="dropdown-menu dropdown-usermenu pull-right">
									<li><a href="javascript:;"> Profile</a></li>
									<li><a href="javascript:;"> <span
											class="badge bg-red pull-right">50%</span> <span>Settings</span>
									</a></li>
									<li><a href="javascript:;">Help</a></li>
									<li><a href="login.html"><i
											class="fa fa-sign-out pull-right"></i> Log Out</a></li>
								</ul></li>

							<li role="presentation" class="dropdown"><a
								href="javascript:;" class="dropdown-toggle info-number"
								data-toggle="dropdown" aria-expanded="false"> <i
									class="fa fa-envelope-o"></i> <span class="badge bg-green">6</span>
							</a>
								<ul id="menu1" class="dropdown-menu list-unstyled msg_list"
									role="menu">
									<li><a> <span class="image"><img src=""
												alt="Profile Image" /></span> <span> <span>John
													Smith</span> <span class="time">3 mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"><img src=""
												alt="Profile Image" /></span> <span> <span>John
													Smith</span> <span class="time">3 mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"><img src=""
												alt="Profile Image" /></span> <span> <span>John
													Smith</span> <span class="time">3 mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li><a> <span class="image"><img src=""
												alt="Profile Image" /></span> <span> <span>John
													Smith</span> <span class="time">3 mins ago</span>
										</span> <span class="message"> Film festivals used to be
												do-or-die moments for movie makers. They were where... </span>
									</a></li>
									<li>
										<div class="text-center">
											<a> <strong>See All Alerts</strong> <i
												class="fa fa-angle-right"></i>
											</a>
										</div>
									</li>
								</ul></li>
						</ul> -->
					</nav>
				</div>
			</div>
			<!-- /top navigation -->

			<!-- page content -->
			<div class="right_col" role="main">
				<!-- top tiles -->
				<div class="row tile_count">
					<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
						<span class="count_top"><i class="fa fa-clock-o"></i> Date</span>
						<div id="hDate" class="count blue" style="font-size: 32px;"></div>
<!-- 						<span class="count_bottom"><i class="green">4% </i> From
							last Week</span> -->
					</div>
					<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
						<span class="count_top"><i class="fa fa-asterisk"></i>
							Round</span>
						<div id="hRoundsNum" class="count"></div>
<!-- 						<span class="count_bottom"><i class="green"><i
								class="fa fa-sort-asc"></i>3% </i> From last Week</span> -->
					</div>
					<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
						<span class="count_top"><i class="fa fa-clock-o"></i> Round Time</span>
						<div id="hRoundTime" class="count blue"></div>
<!-- 						<span class="count_bottom"><i class="green"><i
								class="fa fa-sort-asc"></i>34% </i> From last Week</span> -->
					</div>
					<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
						<span class="count_top"><i class="fa fa-asterisk"></i> Configuration Items</span>
						<div id="hCi" class="count"></div>
<!-- 						<span class="count_bottom"><i class="red"><i
								class="fa fa-sort-desc"></i>12% </i> From last Week</span> -->
					</div>
					<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
						<span class="count_top"><i class="fa fa-asterisk"></i> Services</span>
						<div id="hServices" class="count blue"></div>
<!-- 						<span class="count_bottom"><i class="green"><i
								class="fa fa-sort-asc"></i>34% </i> From last Week</span> -->
					</div>
					<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">
						<span class="count_top"><i class="fa fa-asterisk"></i> Incidents</span>
						<div id="hInci" class="count"></div>
<!-- 						<span class="count_bottom"><i class="green"><i
								class="fa fa-sort-asc"></i>34% </i> From last Week</span> -->
					</div>
				</div>
				<!-- /top tiles -->

				<!-- Graph -->
				<div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="dashboard_graph">

							<div class="row x_title">
								<div class="col-md-5">
									<h3>
										IT Budget Breakdown 
									</h3>
								</div>

<!-- 								<div class="col-md-6"> -->
								<select id="teamSelection" style="min-width: 120px">
									<option value="both">Both Teams</option>
									<option value=1>Marom</option>
									<option value=2>Rakia</option>
								</select> <select id="serviceSelection" style="min-width: 290px">
										<option value=0>All Services</option>
									</select> 
									<select id="roundSelection" style="min-width: 120px">
										<option value=0>All Rounds</option>
									</select>
<!-- 								</div> -->
							</div>

							<div class="col-md-9 col-sm-9 col-xs-12">
								<div id="main">
									<div id="sequence"></div>
									<div id="chart">
<!-- 									<center>
										<img id="loading" src="css/ring.gif" style="display: none;">
									</center> -->
										<div id="explanation" style="visibility: hidden;">
											<span id="percentage"></span><br /> of the budget
										</div>
									</div>
								</div>
								      
     
							</div>
							<div class="col-md-3 col-sm-3 col-xs-12 bg-white">
								<div class="x_title">
									<h2>Legend</h2><br/>
									<div class="clearfix"></div>
								</div>

								<div class="col-md-6 col-sm-6 col-xs-6">
 									<div id="legend1" class="legend" ></div>
								</div>
							
							
							<div class="col-md-6 col-sm-6 col-xs-6">
 									<div id="legend2" class="legend" ></div>
								</div>
								
<!-- 								<div class="col-md-4 col-sm-4 col-xs-6">
 									<div id="legend3" class="legend" ></div>
								</div> 
 -->
							</div>

							<div class="clearfix"></div>
						</div>
					</div>

				</div>
				<br /> <br />

			</div>
		</div>
	</div>

	<!-- /page content -->

	<!-- footer content -->
	<footer>
		<div class="pull-right">
			Copyright � Tom Yanovich & Shanny Shohet
		</div>
		<div class="clearfix"></div>
	</footer>
	<!-- /footer content -->



	<!-- Bootstrap -->
	<script src="js/bootstrap-3.3.5.min.js"></script>
	<!-- FastClick -->
	<script src="js/fastclick.js"></script>
	<!-- NProgress -->
	<script src="js/nprogress.js"></script>
	<!-- Chart.js -->
	<script src="js/charts/Chart.min.js"></script>

	<!-- bootstrap-progressbar -->
	<script src="js/charts/bootstrap-progressbar.min.js"></script>
	<!-- iCheck -->
	<script src="js/icheck.min.js"></script>

	<!-- Flot -->
	<script src="js/charts/jquery.flot.js"></script>
	<script src="js/charts/jquery.flot.pie.js"></script>
	<script src="js/charts/jquery.flot.time.js"></script>
	<script src="js/charts/jquery.flot.stack.js"></script>
	<script src="js/charts/jquery.flot.resize.js"></script>

	<!-- Flot plugins -->
	<script src="js/charts/jquery.flot.orderBars.js"></script>
	<script src="js/charts/jquery.flot.spline.min.js"></script>
	<script src="js/charts/curvedLines.js"></script>
	<script src="js/charts/jquery.flot.axislabels.js"></script>
	<script src="js/charts/jquery.flot.tooltip.min.js"></script>


	<!-- Select2 -->
	<script src="js/select2.full.js"></script>

	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>

	<!-- Reports Header -->
	<script src="js/reports_header.js"></script>

	<!-- D3 -->
	<script src="js/d3.v3.min.js"></script>

	<!-- Sequence Graph -->
	<script src="js/sequences.js"></script>
	<!-- Financial js -->
	<script src="js/financial.js"></script>

</body>
</html>
