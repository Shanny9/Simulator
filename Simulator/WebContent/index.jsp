<!DOCTYPE html>
<html lang="en">
<head>
<title>Service Management Simulator</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/home.css">
<link rel="stylesheet" href="css/bootstrap.min.css">

<%
	//TODO: change round in home.js to realRound.
	//TODO: change course in home.js to realCourse.
	String jspRound = request.getParameter("form-round");
	String jspCourse = request.getParameter("form-courseName");
	if (jspRound != null && jspCourse != null) {
		Integer round = Integer.valueOf(jspRound);
%>

<script type="text/javascript">
				var realRound =  '<%=round%>'; 
				var realCourse = '<%=jspCourse%>';
</script>
<%
	}
%>

<script src="js/jquery-1.11.3.min.js"></script>
<script src="js/bootstrap-3.3.5.min.js"></script>
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

<%
	if (session.getAttribute("isLogged") == null) {
		response.sendRedirect("login.jsp");
		return;
	}
%>


</head>
<body>

	<%@ include file="header.html"%>
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
						<div id="progress-custom"
							class="progress-bar progress-bar-danger progress-bar-custom"
							role="progressbar" style="width: 0%;">Pause</div>
						<div id="progress-custom"
							class="progress-bar progress-bar-success" role="progressbar"
							style="width: 0%;">Run</div>
					</div>
				</div>
				<!--scoreboard-->
				<div class="push"></div>
			</div>
			<!--container-fluid-->
		</center>
		<%@ include file="footer.html"%>
	</div>
	<!--background-image-->
</body>
</html>
