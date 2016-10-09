
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Select Course</title>

<!-- CSS -->
<link rel="stylesheet" href="css/fonts/fontFamilyRoboto.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet"
	href="css/fonts/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="css/login-form.css">
<link rel="stylesheet" href="css/course-style.css">
<link rel="stylesheet" href="css/header.css">
<link rel="stylesheet" href="css/footer.css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

<script src="js/jquery-1.11.3.min.js"></script>

</head>

<body>
	<%@ include file="header.jsp"%>
	<!-- Top content -->
	<div class="top-content">

		<div class="inner-bg">
			<div class="container">
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 form-box">
						<div class="form-top">
							<div class="form-top-left">
								<h3>Select a Course</h3>
								<p>Select a course name to see reports</p>
							</div>
							<div class="form-top-right">
								<i class="fa fa-bar-chart"></i>
							</div>
						</div>
						<div class="form-bottom">
							<form role="form"  action="mtbf_reports.jsp"
								method="post" class="login-form create-form">

								<div class="form-group">
									<span class="_label">Course name</span> <select
										name="form-courseName" class="form-courseName form-control"
										id="form-courseName">
										<option value="">Please select a course...</option>
									</select>
								</div>

								<button type="submit" class="btn disabled" id="start">See
									Reports</button>
							</form>
							<p id="err" class="message">Settings.ser file does not exist</p>
						</div>
					</div>
				</div>

			</div>
		</div>

	</div>
	<%@ include file="footer.html"%>


	<!-- Javascript -->
	
	<script src="js/bootstrap-3.3.5.min.js"></script>
	<script src="js/jquery.backstretch.min.js"></script>
	<script src="js/selectCourse_reports.js"></script>


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
	<!--[if lt IE 10]>
            <script src="assets/js/placeholder.js"></script>
        <![endif]-->

</body>

</html>