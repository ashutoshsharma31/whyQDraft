<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<title>Refresh method with new url</title>

<link href="./content/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet" />
<link href="./content/boostrap-table/bootstrap-table.css" rel="stylesheet" />
<script src="./content/jquery/jquery-3.1.1.min.js"></script>
<script src="./content/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script src="./content/boostrap-table/bootstrap-table.js"></script>
<link href="./content/custom/css/sb-admin.css" rel="stylesheet" />
<link href="./font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

</head>
<body>
	<div class="container">
		<%
			String serverPath = request.getRequestURL().substring(0,request.getRequestURL().length() - request.getServletPath().length());
			
		 %>
		
		<h1>
			Token Viewer 
		</h1>
		<div class="toolbar">
			<button id="refresh" class="btn btn-default">Refresh</button>
		</div>
		<table id="table" data-toggle="table" data-toolbar=".toolbar"
			data-url='<%=serverPath %>/api/TokenDataServlet'>
			<thead>
				<tr>
					<th data-field="quantity">Token ID</th>
					<th data-field="orderid">Order ID</th>
					<th data-field="name">Menu Item</th>
					<th data-field="orderid">Quantity</th>
					<th data-field="username">Customer</th>
					<th data-field="status">Status</th>
         			<th data-field="action" data-formatter="actionFormatter" data-events="actionEvents">Mark As Complete</th>
				</tr>
			</thead>
		</table>
	</div>
	<script>
        var $table = $('#table');
        $(function () {
            $('#refresh').click(function () {
                $table.bootstrapTable('refresh', {
                    url: '<%=serverPath%>/api/TokenDataServlet'
                });
            });
        });
        function actionFormatter(value, row, index) {
            return [
                '<a class="Complete" href="javascript:void(0)" title="Complete">',
                '<i class="glyphicon glyphicon-ok"></i>',
                '</a>',                
            ].join('');
        }
        $(document).ready(function(){
            setInterval(function(){
            	$table.bootstrapTable('refresh', {
                    url: '<%=serverPath%>/api/TokenDataServlet'
                });
            },5000);  // this will call your fetchData function for every 10 Sec.

        });
        window.actionEvents = {
            'click .Complete': function (e, value, row, index) {
                alert('You click Complete icon, row: ' + JSON.stringify(row));
                var token = 'DEFAULT';
                token = JSON.stringify(row);
                $.ajax({
                	method: "POST",
                	url: "<%=serverPath%>/api/TokenDataServlet", 
                	data:{'value':token},                	
                	success: function(result){
               		$table.bootstrapTable('refresh', {
                           url: '<%=serverPath%>/api/TokenDataServlet'
                       });
                }});
                console.log(value, row, index);
            }
        };
    </script>
</body>
</html>

