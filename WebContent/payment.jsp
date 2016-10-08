<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.whyq.session.*"%>
<%@ page import="com.whyq.model.*"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="java.util.*"%>

<%@ page import="org.json.*"%>
<%@ page import="org.json.JSONException"%>
<%@ page import="org.json.JSONObject"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" type="text/css"
	href="static/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="static/font-awesome/css/font-awesome.min.css" />
<script type="text/javascript" src="static/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript"
	src="static/bootstrap/js/bootstrap.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<form action="/Purchase" method="POST">
		<%
			String strOrderInformation = request.getParameter("orderInformation");
			String strContextObject = request.getParameter("contextObject");
			Gson gson = new Gson();
			OrderInformation orderInformation = gson.fromJson(strOrderInformation, OrderInformation.class);
			int totalAmt = 0;
		%>
		<input type="hidden" id="contextObject" name="contextObject"
			value="<%=URLEncoder.encode(strContextObject, "UTF-8")%>" /> <input
			type="hidden" id="orderInformation" name="orderInformation"
			value="<%=URLEncoder.encode(strOrderInformation, "UTF-8")%>" />
		<table class="table table-condensed">
			<thead>
				<tr>
					<td><strong>Item Name</strong></td>
					<td class="text-center"><strong>Item Size</strong></td>
					<td class="text-center"><strong>Item Price</strong></td>
					<td class="text-center"><strong>Item Quantity</strong></td>
					<td class="text-right"><strong>Total</strong></td>
				</tr>
			</thead>
			<tbody>
				<%
					List<CartItem> orderList = orderInformation.getOrderList();

					for (CartItem order : orderList) {
						totalAmt+=order.getTotalPrice();
				%>
				<tr>
					<td><%=order.getMenuItem().getName()%></td>
					<td class="text-center">
						<%
							if (order.getSize() == null) {
						%>&nbsp;<%
							} else {
						%><%=order.getSize()%> <%
 	}
 %>
					</td>
					<td class="text-center"><%=order.getUnitPrice()%></td>
					<td class="text-center"><%=order.getQuantity()%></td>
					<td class="text-right"><%=order.getTotalPrice()%></td>
				</tr>


				<%
					}
					//coverting into paise
					totalAmt *= 100; 
				%>
			</tbody>
		</table>

		<script src="https://checkout.razorpay.com/v1/checkout.js"
			data-key="rzp_test_y6WOhvJ5nCHqfU" data-amount="<%=totalAmt %>"
			data-buttontext="Pay with Razorpay" data-name="Merchant Name"
			data-description="Purchase Description"
			data-image="https://your-awesome-site.com/your_logo.jpg"
			data-prefill.name="Harshil Mathur"
			data-prefill.email="support@razorpay.com" data-theme.color="#F37254"></script>

	</form>
</body>
</html>