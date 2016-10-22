<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.mysql.*"%>
<%@ page import="java.sql.*"%>
<html>

<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script>
	$(function() {
		$('#basic-events-table')
				.next()
				.click(
						function() {

							var $result = $('#events-result');

							$('#events-table')
									.bootstrapTable({
									/*
									onAll: function (name, args) {
									    console.log('Event: onAll, data: ', args);
									}
									onClickRow: function (row) {
									    $result.text('Event: onClickRow, data: ' + JSON.stringify(row));
									},
									onDblClickRow: function (row) {
									    $result.text('Event: onDblClickRow, data: ' + JSON.stringify(row));
									},
									onSort: function (name, order) {
									    $result.text('Event: onSort, data: ' + name + ', ' + order);
									},
									onCheck: function (row) {
									    $result.text('Event: onCheck, data: ' + JSON.stringify(row));
									},
									onUncheck: function (row) {
									    $result.text('Event: onUncheck, data: ' + JSON.stringify(row));
									},
									onCheckAll: function () {
									    $result.text('Event: onCheckAll');
									},
									onUncheckAll: function () {
									    $result.text('Event: onUncheckAll');
									}
									 */
									})
									
									.on(
											'all.bs.table',
											function(e, name, args) {
												console.log('Event:', name,
														', data:', args);
											})
									.on(
											'click-row.bs.table',
											function(e, row, $element) {
												$result
														.text('Event: click-row.bs.table, data: '
																+ JSON
																		.stringify(row));
											})
									.on(
											'dbl-click-row.bs.table',
											function(e, row, $element) {
												$result
														.text('Event: dbl-click-row.bs.table, data: '
																+ JSON
																		.stringify(row));
											})
									.on(
											'sort.bs.table',
											function(e, name, order) {
												$result
														.text('Event: sort.bs.table, data: '
																+ name
																+ ', '
																+ order);
											})
									.on(
											'check.bs.table',
											function(e, row) {
												$result
														.text('Event: check.bs.table, data: '
																+ JSON
																		.stringify(row));
											})
									.on(
											'uncheck.bs.table',
											function(e, row) {
												$result
														.text('Event: uncheck.bs.table, data: '
																+ JSON
																		.stringify(row));
											})
									.on(
											'check-all.bs.table',
											function(e) {
												$result
														.text('Event: check-all.bs.table');
											})
									.on(
											'uncheck-all.bs.table',
											function(e) {
												$result
														.text('Event: uncheck-all.bs.table');
											});
						});
	});

	
	
</script>
</head>

<body>
	<div class="alert alert-success" id="events-result">Here is the
		result of event.</div>
		 <h1>Refresh method with new url(<a href="https://github.com/wenzhixin/bootstrap-table/issues/409">#409</a>).</h1>
       
	<table id="events-table" data-url="http://localhost:8080/TestAwsProject/api/TokenDataServlet" data-height="299"
		data-search="true">
		<thead>
			<tr>
				<th data-field="quantity" data-checkbox="true"></th>
				<th data-field="tokenid" data-sortable="true">Item ID</th>
				<th data-field="orderid" data-sortable="true">Item Name</th>
				<th data-field="name" data-sortable="true">Item Price</th>
			</tr>
		</thead>
	</table>

</body>
</html>