<html lang="en">
<head>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link href="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.0.3/css/bootstrap.min.css"
    rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<link href="http://cdn.rawgit.com/davidstutz/bootstrap-multiselect/master/dist/css/bootstrap-multiselect.css"
    rel="stylesheet" type="text/css" />
<script src="http://cdn.rawgit.com/davidstutz/bootstrap-multiselect/master/dist/js/bootstrap-multiselect.js"
    type="text/javascript"></script>
</head>
<body>
<form>


<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>

<%
String id = request.getParameter("tokenid");
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

<nav role="navigation" class="fixed-nav-bar">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a href="#" class="navbar-brand">WhyQ</a><% 
      %>
    </div>
    <!-- Collection of nav links and other content for toggling -->
    <div id="navbarCollapse" class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
            <li class="active"><a href="final.jsp">Admin Menu</a></li>
            <li><a href="final.jsp">Manager Menu</a></li>
            
        </ul>
        
    </div>
</nav>





<h2 align="center"><font><strong>Tokens</strong></font></h2></head>
<table class="table table-striped" id="tb" align="center" width="20%" height="%30">
<tr>

</tr>
<tr>
<td><b>Token Id</b></td>
<td><b>Order Id</b></td>
<td><b>quantity</b></td>
<td><b>Menu Item</b></td>
<td><b>Username</b>
<td><b>Status</b>



</tr>
<%
try{ 
connection = DriverManager.getConnection(connectionUrl+dbName, userId, password);
statement=connection.createStatement();
String sql =("select tokenid , ol.orderid, ol.quantity,mi.name, od.username from token t, orderline ol, menuitem mi, orderinfo od where t.orderlineid = ol.orderid and mi.itemid = ol.itemid and od.orderid=ol.orderid");

resultSet = statement.executeQuery(sql);
int i =0;
String j=request.getParameter("i");
while(resultSet.next()){
%>
<tr>
<input type="hidden" id="hdnTokenId_<%=i %>" value="<%=resultSet.getString("tokenid")%>"/>
<td><%=resultSet.getString("tokenid") %></td>
<td><%=resultSet.getString("orderid") %></td>
<td><%=resultSet.getString("quantity") %></td>
<td><%=resultSet.getString("name") %></td>
<td><%=resultSet.getString("username") %></td>

<td><button id="remove_<%=i %>" class= "btn btn-outline-secondary"  onclick="deleteRow(this)"> Complete</button></td>
<%i++; %>
</tr>
</form>
<%
}

} catch (Exception e) {
e.printStackTrace();
}
%>


<script language="javascript">
        function deleteRow(btn) {
           		
           		var index = btn.id.substr(7);
           		var tokenid = document.getElementById('hdnTokenId_'+index);
           		alert(tokenid.value);
           		
           		    $.ajax({url: "/updatetoken", success: function(result){
           		        $("#div1").html(result);
           		    }});
           		
        }
    </script>


</table>
</body>
</html>