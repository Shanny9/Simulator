
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

</head>

<body>
	<%@ include file="header.html"%>
	<!-- Top content -->
	<div class="top-content">

		<div class="inner-bg">
			<div class="container">
				<div class="row">
					<!--                         <div class="col-sm-8 col-sm-offset-2 text">
                            <h1><strong>...</strong> .</h1>
                        </div> -->
				</div>
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 form-box">
						<div class="form-top">
							<div class="form-top-left">
								<h3>Select a Course</h3>
								<p>Select a course name and round number</p>
							</div>
							<div class="form-top-right">
								<i class="fa fa-play"></i>
							</div>
						</div>
						<div class="form-bottom">
							<form role="form" action="HomeController?action=selectCourse"
								method="post" class="login-form create-form">

								<div class="form-group">
									<span class="_label">Course name</span> 
									<select
										name="form-courseName" class="form-courseName form-control"
										id="form-courseName">
										<option value="">Please select a course...</option>
									</select>
								</div>

								<div class="form-group">
									<span class="_label">Round</span> <select name="form-round"
										class="form-round form-control disabled" id="form-round">
									</select>
								</div>

								<button type="submit" class="btn disabled" id="start">Start
									Simulator</button>
								<br>
								<button type="button" class="btn disabled" id="delete">Delete
									Course</button>
							</form>
							<p id="err" class="message">Settings.ser file does not exist</p>
							<p id="msg" class="message"><span class="glyphicon glyphicon-ok" ></span> Course was deleted successfully</p>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 social-login">
						<!--	<h3>...or login with:</h3>
                        	<div class="social-login-buttons">
	                        	<a class="btn btn-link-2" href="#">
	                        		<i class="fa fa-facebook"></i> Facebook
	                        	</a>
	                        	<a class="btn btn-link-2" href="#">
	                        		<i class="fa fa-twitter"></i> Twitter
	                        	</a>
	                        	<a class="btn btn-link-2" href="#">
	                        		<i class="fa fa-google-plus"></i> Google Plus
	                        	</a>
                        	</div>-->
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@ include file="footer.html"%>


	<!-- Javascript -->
	<script src="js/jquery-1.11.3.min.js"></script>
	<script src="js/bootstrap-3.3.5.min.js"></script>
	<script src="js/jquery.backstretch.min.js"></script>
	<script src="js/selectCourse.js"></script>


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