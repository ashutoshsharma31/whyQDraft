<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<title>Refresh method with new url</title>

<link href="./content/bootstrap-3.3.7-dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="./content/boostrap-table/bootstrap-table.css"
	rel="stylesheet" />
<script src="./content/jquery/jquery-3.1.1.min.js"></script>
<script src="./content/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script src="./content/boostrap-table/bootstrap-table.js"></script>
<link href="./content/custom/sb-admin.css" rel="stylesheet" />
<link href="./content/custom/morris.css" rel="stylesheet" />
<link href="./content/plugin/font-awesome/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">


</head>
<body>

	<%
	String serverPath = request.getRequestURL().substring(0,request.getRequestURL().length() - request.getServletPath().length());
%>

	<div id="wrapper">

		<!-- Navigation -->
		<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-ex1-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.html">Whyq.co</a>
		</div>
		<!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
		<div class="collapse navbar-collapse navbar-ex1-collapse">
			<ul class="nav navbar-nav side-nav">
				<li class="active"><a href="managerview.jsp"><i
						class="fa fa-fw fa-dashboard"></i> Dashboard</a></li>
				<li><a href="tokenviewer.jsp"><i
						class="fa fa-fw fa-bar-chart-o"></i> Tokens</a></li>
		</div>
		<!-- /.navbar-collapse --> </nav>

		<div id="page-wrapper">

			<div class="container-fluid">

				<!-- Page Heading -->
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">
							Manager <small>View</small>
						</h1>
					</div>
				</div>

				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title">
									<i class="fa fa-money fa-fw"></i> Tokens
								</h3>
							</div>
							<div class="panel-body">
								<div class="table-responsive">
									<table id="table" data-toggle="table" data-toolbar=".toolbar"
										data-url='<%=serverPath %>/api/ConsolidateDataServlet'
										class="table table-bordered table-hover table-striped">
										<thead>
											<tr>
												<th data-field="name">Menu Item</th>
												<th data-field="pendingtokens">Count of pending Orders</th>												
											</tr>
										</thead>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- /.row -->

			</div>
			<!-- /.container-fluid -->

		</div>
		<!-- /#page-wrapper -->

	</div>
	<!-- /#wrapper -->


</body>

<script>
        var $table = $('#table');
        $(function () {
            $('#refresh').click(function () {
                $table.bootstrapTable('refresh', {
                    url: '<%=serverPath%>/api/ConsolidateDataServlet'
                });
            });
        });
        
        $(document).ready(function(){
            setInterval(function(){
            	$table.bootstrapTable('refresh', {
                    url: '<%=serverPath%>/api/ConsolidateDataServlet'
                });
            },5000);  // this will call your fetchData function for every 10 Sec.

        });        
    </script>

</html>

