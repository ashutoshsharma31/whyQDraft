package com.whyq.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.whyq.dao.OrderDao;
import com.whyq.dao.OrderDataDao;
import com.whyq.dao.TokenDao;
import com.whyq.model.CartItem;
import com.whyq.model.GupshupObject;
import com.whyq.model.OrderInformation;
import com.whyq.model.Token;
import com.whyq.util.BotUtils;
import com.whyq.util.TokenUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

@WebServlet("/Purchase")
public class Purchase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Purchase() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			OrderDataDao orderDataDao = new OrderDataDao();
			TokenDao tokenDao = new TokenDao();
			TokenUtils tokenUtils = new TokenUtils();
			String strContext = request.getParameter("contextObject");
			String strOrder = request.getParameter("orderInformation");

			Gson gson = new Gson();

			OrderInformation orderInformation = gson.fromJson(URLDecoder.decode(strOrder, "UTF-8"),
					OrderInformation.class);
			
			orderDataDao.updateOrderStatus(orderInformation);
			tokenDao.createTokens(orderInformation);
			tokenUtils.sendMessage(strContext);
			String serverPath = request.getRequestURL().substring(0,
					request.getRequestURL().length() - request.getServletPath().length());
			tokenUtils.sendTokenCarolMessage(strContext, serverPath, tokenDao.getAllTokensForOrder(orderInformation.getOrderNum()));
			tokenUtils.sendReceipt(strContext,serverPath,tokenDao.getAllTokensForOrder(orderInformation.getOrderNum()),orderInformation);
			PrintWriter out = response.getWriter();
			out.println("Payment Successful !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	}
