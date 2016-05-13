<!DOCTYPE html>
<html lang="en">
<head>
<title>Service Management Simulator</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/home.css">
<link rel="stylesheet"
	href="css/bootstrap.min.css">
<script
	src="js/jquery-1.11.3.min.js"></script>
<script
	src="js/bootstrap-3.3.5.min.js"></script>
<script src="js/jquery.backstretch.min.js"></script>
<script src="js/date.format.js"></script>
<script src="js/utils.js"></script>
<script src="js/home.js"></script>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);

%>

</head>
<body>


	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#myNavbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a id="startSimulator" class="navbar-brand" href="#">LOGO</a>
			</div>
			<div class="collapse navbar-collapse" id="myNavbar">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#home">HOME</a></li>
					<li><a href="#band">CHANGES</a></li>
					<li><a href="#tour">REPORTS</a></li>
					<li><a href="#contact">ABOUT</a></li>
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">MORE <span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a id="pause" href="#">Pause</a></li>
							<li><a id="resume" href="#">Resume</a></li>
							<li><a href="#">BLA3</a></li>
						</ul></li>
					<li><a href="#"><span class="glyphicon glyphicon-search"></span></a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="background-image">
		<center>
			<div class="container-fluid">
				<div class="headline">Service Management Simulator</div>
				<div class="scoreboard">
					<table class="main-tbl">
						<thead>
							<th class="text-center">
								<div class="header">MAROM</div>
								<div id="marom-score" class="txt-score regular">
									$10,256,412</div>
							</th>
							<th class="text-center">
								<div class="header">REMAINING</div>
								<div id="main-time" class="txt-score regular center">
									00:00:00</div>
							</th>
							<th class="text-center">
								<div class="header">RAKIA</div>
								<div id="rakia-score" class="txt-score regular">
									$1,376,526</div>
							</th>
						</thead>
						<tbody>
							<tr>
								<td colspan="3" class="space-row"></td>
							</tr>
							<tr>
								<td>
									<table class="score-tbl event-tbl">
										<thead>
											<tr>
												<th><div class="header">EVENT ID</div></th>
												<th><div class="header">TIME</div></th>
											</tr>
										</thead>
										<tbody class="score-tbl teamA">
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
										</tbody>
									</table>
								</td>

								<td class="round-session">
									<div class="header text-center">ROUND</div>
									<div class="txt-score regular center">
										<span id="round">1</span>/<span id="totalRounds">3</span>
									</div> <br>
									<div class="header text-center">SESSION</div>
									<div class="txt-score regular center">
										<span id="session">1</span>/<span id="sessionsPerRound">3</span>
									</div>
								</td>
								<td>
									<table class="score-tbl event-tbl">
										<thead>
											<tr>
												<th><div class="header">EVENT ID</div></th>
												<th><div class="header">TIME</div></th>
											</tr>
										</thead>
										<tbody class="score-tbl teamB">
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
											<tr>
												<td>&nbsp</td>
												<td>&nbsp</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
						</tbody>
					</table>
					<div>&nbsp</div>
					<div id="progress" class="progress">
						<div id="progress-custom" class="progress-bar progress-bar-success" role="progressbar" style="width: 0%;">
							Run
						</div>
						<div id="progress-custom" class="progress-bar progress-bar-danger progress-bar-custom" role="progressbar" style="width: 0%;">
							Pause
						</div>
					</div>

				</div>
				<!--scoreboard-->
				<div class="push"></div>
			</div>
			<!--container-fluid-->
		</center>
		<footer>
			<div class="container text-center">
				Theme made by Shanny Shohet & Tom Yanovich <span
					class="glyphicon glyphicon-copyright-mark"></span>
			</div>
		</footer>
	</div>
	<!--background-image-->
</body>
</html>
