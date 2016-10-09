<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

        <div class="col-md-3 left_col">
          <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
              <a href="index.html" class="site_title"><i class="fa fa-plane"></i> <span>ITIL Simulator</span></a>
            </div>

            <div class="clearfix"></div>

            <!-- menu profile quick info -->
<!--             <div class="profile">
              <div class="profile_pic">
                <img src="images/img.jpg" alt="..." class="img-circle profile_img">
              </div>
              <div class="profile_info">
                <span>Welcome,</span>
                <h2>John Doe</h2>
              </div>
            </div> -->
            <!-- /menu profile quick info -->

            <br />

            <!-- sidebar menu -->
            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
              <div class="menu_section">
                <h3>Management</h3>
                <ul class="nav side-menu">
                  <li><a><i class="fa fa-home"></i> Home <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="selectCourse_reports.jsp">Select Other Course</a></li>
                    </ul>
                  </li>
                  <li id="tables"><a><i class="fa fa-edit"></i> Tables <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu" id="ulTables">
                      <li><a href="tables.jsp?tbl=tblCi" id="tblCi">Configuration Item</a></li>
                      <li><a href="tables.jsp?tbl=tblCMDB" id="tblCMDB">CMDB</a></li>
                      <li><a href="tables.jsp?tbl=tblDepartment" id="tblDepartment">Department</a></li>
                      <li><a href="tables.jsp?tbl=tblDivision" id="tblDivision">Division</a></li>
                    <!--   <li><a href="tables.jsp?tbl=tblEvent" id="tblEvent">Event</a></li> -->
                      <li><a href="tables.jsp?tbl=tblIncident" id="tblIncident">Incident</a></li>
                      <li><a href="tables.jsp?tbl=tblLevel" id="tblLevel">Level</a></li>
                      <li><a href="tables.jsp?tbl=tblPriorityCost" id="tblPriorityCost">Priority Cost</a></li>
                      <li><a href="tables.jsp?tbl=tblService" id="tblService">Service</a></li>
                      <li><a href="tables.jsp?tbl=tblServiceDep" id="tblServiceDep">Service Department</a></li>
                     <!--  <li><a href="tables.jsp?tbl=tblServiceDiv" id="tblServiceDiv">Service Division</a></li> -->
                      <li><a href="tables.jsp?tbl=tblSolution" id="tblSolution">Solution</a></li>
                      <li><a href="tables.jsp?tbl=tblSupplier" id="tblSupplier">Supplier</a></li>
                    </ul>
                  </li>
                  <li><a><i class="fa fa-bar-chart"></i> Reports <span class="fa fa-chevron-down"></span></a>
                    <ul class="nav child_menu">
                      <li><a href="mtbf_reports.jsp">Mean Time Between Failure</a></li>
                      <li><a href="mtrs_reports.jsp">Mean Time to Restore Service</a></li>
                      <li><a href="financial_reports.jsp">Financial Reports</a></li>
                    </ul>
                  </li>

                </ul>
              </div>
              <div class="menu_section">
                <ul class="nav side-menu">
                  <li><a href="login.jsp?action=logout"><i class="fa fa-sign-out"></i> Logout</a></li>
                  <li><a href="opening.jsp"><i class="fa fa-arrow-left"></i> Go Back<!--  <span class="label label-success pull-right">Main Page</span> --></a></li>
                </ul>
              </div>

            </div>
            <!-- /sidebar menu -->

            <!-- /menu footer buttons -->
<!--             <div class="sidebar-footer hidden-small">
              <a data-toggle="tooltip" data-placement="top" title="Settings">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="FullScreen">
                <span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Lock">
                <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Logout">
                <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
              </a>
            </div> -->
            <!-- /menu footer buttons -->
          </div>
        </div>