<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>Simulator</title>

<!-- CSS -->
<link rel="stylesheet"
	href="css/fonts/fontFamilyRoboto.css">
<link rel="stylesheet"
	href="css/bootstrap.min.css">
<link rel="stylesheet"
	href="css/fonts/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="css/login-form.css">
<link rel="stylesheet" href="css/client-style.css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
<%
	String jspTeam = request.getParameter("team");
	if(jspTeam == null)
		response.sendRedirect("login.jsp");
%>
<script type="text/javascript">
	var team =  '<%= request.getParameter("team") %>';
</script>        
        
<script
	src="js/jquery-1.11.3.min.js"></script>
<script
	src="js/bootstrap-3.3.5.min.js"></script>
<script src="js/jquery.backstretch.min.js"></script>
<script src="js/date.format.js"></script>
<script src="js/utils.js"></script>
<script src="js/home.js"></script>
<script src="js/client.js"></script>

</head>

<body>

	<!-- Top content -->
	<div class="top-content">

		<div class="inner-bg">
			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-8 col-sm-offset-2 text">
						<h1>
							<strong>DANBERT</strong> ITIL Simulator
						</h1>
					</div>
					<br>
					<div class="col-sm-8 col-sm-offset-2 text">
						<div id="main-time" class="txt-score regular center">
							00:00:00</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3 form-box">
						<div class="form-top">
							<div class="form-top-left">
								<h3>Solve or purchase solution</h3>
							</div>
							<input type="number" pattern="[0-9]*" name="incidentID"
								placeholder="Incident #" class="form-username form-control"
								id="incidentID">
							<div class="row minHeight"></div>
							<p>What do you wish to do?</p>
							<div class="panel-group" id="accordion">
								<div class="panel panel-default">
									<div id="no-padding" class="panel-heading">
										<h4 class="panel-title">
											<a id="solveMenu" class="collapse-menu"
												data-parent="#accordion" href="#collapse1"><button
													class="collapse-menu btn btn-green">Solve</button></a>
										</h4>
									</div>
									<div id="collapse1" class="panel-collapse collapse">
										<div class="panel-body">
											<input type="number" pattern="[0-9]*" name="solutionID"
												placeholder="Solution #" class="form-username form-control"
												id="solutionID">
											<button id="submitSol" disabled="disabled"
												class="btn btn-green">Submit Solution</button>
											<img id="gif" style="-webkit-user-select: none"
												src="https://thomas.vanhoutte.be/miniblog/wp-content/uploads/spinningwheel.gif">
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div id="no-padding" class="panel-heading">
										<h4 class="panel-title">
											<a id="purchaseMenu" class="collapse-menu"
												data-parent="#accordion" href="#collapse2"><button
													class="btn btn-red">Purchase</button></a>
										</h4>
									</div>
									<div id="collapse2" class="panel-collapse collapse">
										<div class="panel-body">
											The solution cost is <span id="solCost">10 <i
												class="fa fa-ils"></i></span>
											<button id="buy" class="btn btn-red">Purchase</button>
										</div>
									</div>
								</div>
								<div class="row minHeight"></div>
								<center>
									<p id="noIncidentId" class="message">Please enter incident ID</p>
									<p id="wrongIncId" class="message">Wrong incident ID. Please try again</p>
									<h2 id="success" class="message">
										Success <span class="glyphicon glyphicon-ok"></span>
									</h2>
									<h2 id="failure" class="message">
										Try Again <span class="glyphicon glyphicon-remove"></span>
									</h2>
								</center>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</body>

</html>