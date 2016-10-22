<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.mysql.*" %>
<%@ page import="java.sql.*" %>
<html>
<body>
<div id="content">


    <p>Displaying table contents: </p>

    <table border="0" cellpadding="10">
        <thead>
            <tr>
                  <th>ID</th>
                <th>Menu Item</th>
                <th>Token State</th>
                <th>Quantity</th>
                <th>Menu Id</th>
            </tr>
        </thead>
        <tbody>


            <%
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = null;
                	conn=	DriverManager.getConnection("jdbc:mysql://localhost:8080/whyq", "user", "password");
                Statement stmt = null;
                stmt = conn.createStatement();
                String query = "select * from tokens";
                ResultSet rs = null;
                rs = stmt.executeQuery(query);
                while(rs.next()){
            %>
            <tr>
                <%
                    int token_id = rs.getInt("token_id");
                    String menu_item = rs.getString("menu_item");
                    String token_state = rs.getString("token_state");
                    int token_quant = rs.getInt("token_quant");
                    int menu_id = rs.getInt("menu_id");
                %>
                <td><%=token_id %></td>
                <td><%=menu_item %></td>
                <td><%=token_state %></td>
                <td><%=token_quant %></td>
                <td><%=menu_id %></td>
            </tr>               

            <%      
                }
            %>

        </tbody>
    </table>
</div>
</body>
</html>