<!DOCTYPE html>
<html lang="en">
<head>
<title>Service Management Simulator</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/home.css">
<link rel="stylesheet" href="css/bootstrap.min.css">


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
				<div style="overflow: hidden; height: 330px; width: 680px;">
					<div id="youtube_canvas" style="height: 330px; width: 680px;">
						<iframe width="560" height="315"
							src="https://www.youtube.com/embed/4UVB_qJGFa0" frameborder="0"
							allowfullscreen></iframe>
					</div>
				</div>
				<div class="push"></div>
			</div>
			<!--container-fluid-->
		</center>
		<%@ include file="footer.html"%>
	</div>
	<!--background-image-->
</body>
</html>
