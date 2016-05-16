
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Login ITIL Simulator</title>

        <!-- CSS -->
        <link rel="stylesheet" href="css/fonts/fontFamilyRoboto.css">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/fonts/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="css/login-form.css">
        <link rel="stylesheet" href="css/login-style.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Favicon and touch icons -->
        <link rel="shortcut icon" href="assets/ico/favicon.png">
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="assets/ico/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon-57-precomposed.png">

    </head>

    <body>

        <!-- Top content -->
        <div class="top-content">
        	
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-8 col-sm-offset-2 text">
                            <h1><strong>Login</strong> ITIL Simulator</h1>
                          <!--  <div class="description">
                            	<p>
	                            	This is a free responsive login form made with Bootstrap. 
	                            	Download it on <a href="http://azmind.com"><strong>AZMIND</strong></a>, customize and use it as you like!
                            	</p>
                            </div>-->
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                        	<div class="form-top">
                        		<div class="form-top-left">
                        			<h3>New Course</h3>
                            		<p>Enter course name and general settings</p>
                        		</div>
                        		<div class="form-top-right">
                        			<i class="fa fa-lock"></i>
                        		</div>
                            </div>
                            <div class="form-bottom">
			                    <form role="form" action="HomeController?action=newCourse" method="post" class="login-form">
			                    	<div class="form-group">
			                        <div class="form-group">
			                        <span class = "label">Course name</span>
			                        	<input type="text" name="form-courseName" class="form-courseName form-control" id="form-courseName">
			                        </div>
			                        <br>
			                    	<span class = "label">Rounds</span>
			                        	<input type="number" name="form-numOfRounds" class="form-numOfRounds form-control" id="form-numOfRounds">
			                        </div>
			                        <div class="form-group">
			                        <span class = "label">Run time</span>
			                        	<input type="number" name="form-runTime" class="form-runTime form-control" id="form-runTime">
			                        </div>
			                        <div class="form-group">
			                        <span class = "label">Pause time</span>
			                        	<input type="number" name="form-pauseTime" class="form-pauseTime form-control" id="form-pauseTime">
			                        </div>
			                        <div class="form-group">
			                        <span class = "label">Sessions per round</span>
			                        	<input type="number" name="form-sessions" class="form-sessions form-control" id="form-sessions">
			                        </div>
			                        <div class="form-group">
			                        <span class = "label">Initial capital</span>
			                        	<input type="number" name="form-initCapital" class="form-initCapital form-control" id="form-initCapital">
			                        </div>
			                        <button type="submit" class="btn">Create</button>
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


        <!-- Javascript -->
        <script src="js/jquery-1.11.3.min.js"></script>
        <script src="js/bootstrap-3.3.5.min.js"></script>
        <script src="js/jquery.backstretch.min.js"></script>
        <script src="js/login.js"></script>
        
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