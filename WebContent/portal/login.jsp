<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <html>
    <body>
    <% 
    
            String username = request.getParameter("user");
            String password = request.getParameter("pass");
           out.println("Checking login<br>");
            if (username == null || password == null) {
 
                out.print("Invalid paramters ");
            }
 
            // Here you put the check on the username and password
            if (username.toLowerCase().trim().equals("admin") && password.toLowerCase().trim().equals("admin"))
            {
         response.sendRedirect("menu.html");
            }
            else
            {
            	response.sendRedirect("login.html");
            }
            %>
            </body>
            </html>
 
 
 
