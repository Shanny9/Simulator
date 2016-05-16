
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Create New Course</title>

        <!-- CSS -->
        <link rel="stylesheet" href="css/fonts/fontFamilyRoboto.css">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/fonts/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="css/login-form.css">
        <link rel="stylesheet" href="css/course-style.css">

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
                        			<h3>New Course</h3>
                            		<p>Enter course name and general settings</p>
                        		</div>
                        		<div class="form-top-right">
                        			<i class="fa fa-plus"></i>
                        		</div>
                            </div>
                            <div class="form-bottom">
			                    <form role="form" action="HomeController?action=newCourse" method="post" class="login-form create-form">
			                    	
			                        <div class="form-group">
			                        <span class = "_label">Course name</span>
			                        	<input type="text" name="form-courseName" class="form-courseName form-control" id="form-courseName">
			                        </div>
			                        <hr class="separator">
			                        <div class="form-group">
			                    	<span class = "_label">Rounds</span>
			                        	<input type="number" name="form-numOfRounds" class="form-numOfRounds form-control" id="form-numOfRounds">
			                      	</div>
			                        <div class="form-group">
			                        <span class = "_label">Run time</span>
			                        	<input type="number" name="form-runTime" class="form-runTime form-control" id="form-runTime">
			                        </div>
			                        <div class="form-group">
			                        <span class = "_label">Pause time</span>
			                        	<input type="number" name="form-pauseTime" class="form-pauseTime form-control" id="form-pauseTime">
			                        </div>
			                        <div class="form-group">
			                        <span class = "_label">Sessions per round</span>
			                        	<input type="number" name="form-sessions" class="form-sessions form-control" id="form-sessions">
			                        </div>
			                        <div class="form-group">
			                        <span class = "_label">Initial capital</span>
			                        	<input type="number" name="form-initCapital" class="form-initCapital form-control" id="form-initCapital">
			                        </div>
			                        <button type="submit" class="btn" id="create">Create</button>
			                    </form>
			                    <div id="loginErr"><span class="glyphicon glyphicon-remove" ></span>...</div>
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
        <script src="js/newCourse.js"></script>
        
   <%
	if(session.getAttribute("err")!=null)
	{ 
   %>
			<script>
				inputError();
			</script>
	<% 
		session.setAttribute("err", null);
	 }
	%>
        
        <!--[if lt IE 10]>
            <script src="assets/js/placeholder.js"></script>
        <![endif]-->

    </body>

</html>