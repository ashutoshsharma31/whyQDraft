<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>

<%
String id = request.getParameter("token_id");
String driverName = "com.mysql.jdbc.Driver";
String connectionUrl = "jdbc:mysql://redvelvetdb.czvyaawmcxuy.ap-south-1.rds.amazonaws.com:3306/";
String dbName = "botdb";
String userId = "awsadmin";
String password = "awsadmin";

try {
Class.forName(driverName);
} catch (ClassNotFoundException e) {
e.printStackTrace();
}

Connection connection = null;
Statement statement = null;
ResultSet resultSet = null;
%>
<html lang="en">


<head>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<link href="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.0.3/css/bootstrap.min.css"
    rel="stylesheet" type="text/css" />
      <script src="Scripts/jquery-1.3.2.js"
    type="text/javascript"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<link href="http://cdn.rawgit.com/davidstutz/bootstrap-multiselect/master/dist/css/bootstrap-multiselect.css" rel="stylesheet" type="text/css" />
<script src="http://cdn.rawgit.com/davidstutz/bootstrap-multiselect/master/dist/js/bootstrap-multiselect.js" type="text/javascript"></script>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link href="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.0.3/css/bootstrap.min.css"
    rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<link href="http://cdn.rawgit.com/davidstutz/bootstrap-multiselect/master/dist/css/bootstrap-multiselect.css"
    rel="stylesheet" type="text/css" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://cdn.rawgit.com/davidstutz/bootstrap-multiselect/master/dist/js/bootstrap-multiselect.js"
    type="text/javascript"></script>
   
    <script> $(function(){$('#ddlCars2').multiselect({ 
        includeSelectAllOption: true,
        enableFiltering:true         
        
  });
    });</script>
   
<!-- <script>
$(document).ready(function(){

 $("#tbUser").on('click','#remove',function(){
	 var index = btn.id.substr(7);
		var tokenid = document.getElementById('hdnTokenId_'+index);
		//response.redirect("update.jsp");
		
		alert(tokenid.value);
		
      
     });

}); -->

<script>
        function deleteRow(btn) {
        	
        	
           		var index = btn.id.substr(7);
           		var tokenid = document.getElementById('hdnTokenId_'+index);
           		alert(tokenid.value);
           		var abc=tokenid.value;
           	//	document.getElementById("tokenIdvalue").value=abc;
           	//	alert(document.getElementById("tokenIdvalue").value);
           		//alert('123');
           		
           		 document.getElementById("abc").value=abc;
           		document.getElementById("hid").value=abc; 
           		document.getElementById("loginid").submit();
           	 
           		document.getElementById("tbUser").deleteRow(0);
           	 $(this).closest('tr').remove();
           		
           		
        }
        
    </script>
    <script>
   
     $(document).ready(function() {
            $('#remove').click(function() {
                $('tr:nth-child(2)').hide();
                // if your table has header(th), use this
                //$('td:nth-child(2),th:nth-child(2)').hide();
            });
        });
     </script>
</head>
<body>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">WhyQ</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="NewFinal.jsp">Counter View</a></li>
      <li><a href="final1.jsp">Manager View</a></li>
     
    </ul>
  </div>
</nav>
 
<h2 align="center"><font><strong>Tokens</strong></font></h2></head>
<table class="table table-striped" id="tbUser" align="center" width="20%" height="%30">
	<tr></tr>
	<tr>
		<td><b>Token No</b></td>
		<td><b><select id="ddlCars2" multiple="multiple">
		<option value="Accord">Pani Puri</option>
		<option value="Duster">Bhel</option>
		<option value="Esteem">Dosa</option>
		<option value="Fiero">Misal Paw</option>
		<option value="Lancer">Sandwich</option>
		<option value="Phantom">Pav Bhaji</option>
		</select></b></td>
		<td><b>Order No</b></td>
		<td><b>Quantity</b></td>
		<td><b>Customer</b></td>
		<td><b>Status</b>
		<td><b>Order State</b></td>
	</tr>
		
		
		<%
		try{ 
		connection = DriverManager.getConnection(connectionUrl+dbName, userId, password);
		statement=connection.createStatement();
		String sql ="select tokenid ,mi.name, ol.orderid, ol.quantity, od.username,status from token t, orderline ol, menuitem mi, orderinfo od where t.tokenid = ol.orderlineid and mi.itemid = ol.itemid and od.orderid=ol.orderid";
		int i=0;
		resultSet = statement.executeQuery(sql);
		while(resultSet.next()){
		%>

		<tr>
		 <input type="hidden" id="hdnTokenId_<%=i %>" value="<%=resultSet.getString("tokenid")%>"/>
		<td><%=resultSet.getString("tokenid") %></td>
		<td><%=resultSet.getString("name") %></td>
		<td><%=resultSet.getString("orderid") %></td>
		<td><%=resultSet.getString("quantity") %></td>
		
		<td><%=resultSet.getString("username") %></td>
		<td><%=resultSet.getString("status") %></td>
		<input type="hidden" id="tokenIdvalue" name="tokenIdvalue"/>
		<td><button id="remove_<%=i %>" class= "btn btn-outline-secondary" onclick="deleteRow(this)" >Complete</button></td>
		<%i++; %>
		</tr>
  
		<%
		}
		
		} catch (Exception e) {
		e.printStackTrace();
		}
		%> 
	</table>
	<form action="update.jsp" id="loginid">
	<input type="hidden" id="abc" name="name"/>
	<input type="hidden" id="hid" name="hidname" value="naclac">
	<input type="submit">
</form>


</body>
</html>