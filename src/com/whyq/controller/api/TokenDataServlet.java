package com.whyq.controller.api;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whyq.dao.TokenDao;
import com.whyq.util.ResultSetConverterUtil;

/**
 * Servlet implementation class TokenDataServlet
 */
@WebServlet("/api/TokenDataServlet")
public class TokenDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TokenDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		TokenDao tokenDao = new TokenDao();

		JSONArray tokenJsonArray = tokenDao.getAllOpenTokensForCafe(3);

		response.setContentType("application/json");
		response.getWriter().append(tokenJsonArray.toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("IN DO POST "+request.toString());
		String token = request.getParameter("value");
		JSONObject jObject  = new JSONObject(token); // json
		//String tokenid = jObject.getString("tokenid"); 
		
		System.out.println(token);
		System.out.println(jObject.toString());
		System.out.println(jObject.getString("status"));
		
		TokenDao tokenDao = new TokenDao();
		tokenDao.updateTokenToComplete(jObject.getInt("tokenid"));
		System.out.println("TOKEN UPDATED "+jObject.getInt("tokenid"));
		
	}

}
