
<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#myNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a id="startSimulator" href="opening.jsp"><img src="css/home_images/Danbert_logo.png"></a>
		</div>
		
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav navbar-right">

				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">COURSES <span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a id="selectc" class="mItem" href="selectCourse.jsp">Select
								Course</a></li>
						<li><a id="createc" class="mItem" href="newCourse.jsp">Create New
								Course</a></li>
						<li><a id="createc" class="mItem" href="selectCourse_file.jsp">Download Incident Flow File</a></li>
					</ul></li>


<!-- 				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">SIMULATOR <span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a id="pause" class="mItem" href="#">Pause</a></li>
						<li><a id="resume" class="mItem" href="#">Resume</a></li>
					</ul></li> -->
					
				<li><a href="selectCourse_reports.jsp">REPORTS</a></li>
				<li class="adminOnly"><a href="tables.jsp?tbl=tblCi">MANAGE DATA</a></li>
<!-- 				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">MORE <span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a id="pause" href="#">Pause</a></li>
						<li><a id="resume" href="#">Resume</a></li>
						<li><a href="#">BLA3</a></li>
					</ul></li> -->
				<li><a id="logout" href="login.jsp?action=logout"><span
						class="glyphicon glyphicon-log-out"></span></a></li>
			</ul>
		</div>
	</div>
</nav>
<%
	Object type = session.getAttribute("type");
	if (type != null) {

%>

<script type="text/javascript">
				var type =  '<%=type%>'; 
				console.log("User type: "+type);
				if(type == "Operator")
					$(".adminOnly").css("display","none");
</script>
<%
	}
%>