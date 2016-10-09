<!DOCTYPE html>
<html lang="en">
<head>
<title>Manage Data</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet" type="text/css" href="css/home.css"> -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/header.css">
<link rel="stylesheet" href="css/footer.css">

<!-- Include one of jTable styles. -->
<link href="css/metro/blue/jtable.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />
<!-- <link href="css/metroblue_jquery-ui.css" rel="stylesheet" type="text/css" /> -->
<style>
.ui-dialog{
	min-width:350px;
}
/* .formError{
	margin-top: -35px
} */
</style>

<link href="css/validationEngine.jquery.css" rel="stylesheet" type="text/css" />

<!-- Include jTable script file. -->
<script src="js/jquery-1.11.3.min.js"></script>
<script src="js/bootstrap-3.3.5.min.js"></script>


<script src="js/jquery-1.8.2.js" type="text/javascript" charset="utf-8"></script>
<script src="js/jquery-ui-1.10.3.custom.js" type="text/javascript" charset="utf-8"></script>
<script src="js/jquery.jtable.js" type="text/javascript" charset="utf-8"></script>
<script src="js/jquery.jtable.editinline.js" type="text/javascript" charset="utf-8"></script>
<script src="js/jquery.jtable.toolbarsearch.js" type="text/javascript" charset="utf-8"></script>
<script src="js/exportToExcel.js" type="text/javascript" charset="utf-8"></script>

<!-- Include Validator Engine files -->
<script src="js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
<script src="js/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>

<script src="js/jquery.backstretch.min.js"></script>
<script src="js/date.format.js"></script>
<script src="js/utils.js"></script>
<script src="js/tables.js"></script>


<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<%-- <%
	if (session.getAttribute("isLogged") == null) {
		response.sendRedirect("login.jsp");
		return;
	}
%> --%>
<link rel="stylesheet" href="css/simple-sidebar.css">
<style type="text/css">

</style>
</head>
<body>

	<%@ include file="header.jsp"%>
	<div class="background-image">
		
		<div class="container-fluid">
			<div class="headline"></div>
			<div class="content">
				<div
					style="width: 60%; margin-right: 10%; margin-left: 20%; text-align: center;">

					<h4>Manage Data</h4>
					<div id="tableContainer"></div>

				</div>
			</div>
			<div class="push"></div>

			<div class="sidebar">
				<ul class="sidebar-nav">
					<li class="sidebar-brand"><a href="#"> Manage Data </a></li>
					<li><a href="#">Suppliers</a></li>
					<li><a href="#">Departments</a></li>
					<li><a href="#">Divisions</a></li>
					<li><a href="#">Events</a></li>
					<li><a href="#">Incidents</a></li>
					<li><a href="#">Services</a></li>
					<li><a href="#">Solutions</a></li>
				</ul>
			</div>
			<!--side bar-->
		<div class="push"></div>
		</div>
		<!--container-fluid-->
		
		<%@ include file="footer.html"%>
	</div>
	<!--background-image-->
</body>
</html>
