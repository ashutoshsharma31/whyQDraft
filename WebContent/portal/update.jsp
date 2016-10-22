<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
 
<html>
<head>
<title>Update Operation</title>
</head>
<body>
 <form action="NewFile.jsp">
<%
String getHiddenValue=request.getParameter("name");
//out.println("Hidden field Value :"+getHiddenValue);
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
PreparedStatement pstatement = null;
int updateQuery = 0;
//ResultSet resultSet = null;
try{ 
	connection = DriverManager.getConnection(connectionUrl+dbName, userId, password);
	statement=connection.createStatement();
	
	 String queryString = "update token set status=? where tokenid= ?";
     pstatement = connection.prepareStatement(queryString);
     pstatement.setString(1, "COMPLETE");
        pstatement.setString(2, getHiddenValue);
        updateQuery = pstatement.executeUpdate();
	//String site = new String("NewFinal.jsp");
	response.sendRedirect("NewFinal.jsp");%>
	
	<% 
}
catch (Exception e) {
	e.printStackTrace();
	}


%>
</form>

<%-- String driverName = "com.mysql.jdbc.Driver";
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
<% 
try{ 
connection = DriverManager.getConnection(connectionUrl+dbName, userId, password);
statement=connection.createStatement();
String query = "update token "+"set status =(complete) where tokenid =(tokenid)";
/* PreparedStatement preparedStmt = connection.prepareStatement(query);
preparedStmt.setString   (1, "complete"); */
resultSet = statement.executeQuery(query);

response.sendRedirect("finaljsp");

}
catch (Exception e) {
e.printStackTrace();
} --%>

</body>
</html>