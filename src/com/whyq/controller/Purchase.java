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
import com.whyq.dao.TokenDao;
import com.whyq.model.CartItem;
import com.whyq.model.OrderInformation;
import com.whyq.model.Token;
import com.whyq.util.BotUtils;

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
			OrderDao orderDao = new OrderDao();
			TokenDao tokenDao = new TokenDao();

			String strContext = request.getParameter("contextObject");
			String strOrder = request.getParameter("orderInformation");
			Gson gson = new Gson();

			OrderInformation orderInformation = gson.fromJson(URLDecoder.decode(strOrder, "UTF-8"),
					OrderInformation.class);
			orderDao.updateOrderStatus(orderInformation);
			createTokens(orderInformation);
			sendMessage(strContext);
			String serverPath = request.getRequestURL().substring(0,
					request.getRequestURL().length() - request.getServletPath().length());
			sendTokenCarolMessage(strContext, serverPath, tokenDao.getAllTokensForOrder(orderInformation.getOrderNum()));
			PrintWriter out = response.getWriter();
			out.println("Payment Successful !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createTokens(OrderInformation orderInformation) {
		OrderDao orderDao = new OrderDao();
		List<CartItem> cartList = orderInformation.getOrderList();
		for (CartItem cartItem : cartList) {
			TokenDao tokenDao = new TokenDao();
			int maxToken = tokenDao.getMaxTokenNum() + 1;
			Token token = new Token();
			token.setOrderId(orderInformation.getOrderNum());
			token.setOrderLineId(orderDao.getOrderLineId(orderInformation.getOrderNum(),
					cartItem.getMenuItem().getItemid(), cartItem.getMenuItem().getSizeable()));
			token.setStatus("PENDING");
			tokenDao.saveToken(token);
			
			System.out.println("TOKEN " + token.toString());
		}
	}

	private static void sendMessage(String contextObject) {
		// TODO Auto-generated method stub
		String URL = "https://api.gupshup.io/sm/api/bot/WhyQDraft/msg";
		String body = "context=" + contextObject + "&message=Payment complete. Sit Back and Relax Order is on the Way.";

		System.out.println(body);
		try {
			okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
			okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			RequestBody body1 = RequestBody.create(mediaType, body);
			okhttp3.Request request1 = new okhttp3.Request.Builder().url(URL)
					.header("apikey", "4f331fc14fb34579c52dd66a805ae1c8").post(body1).build();
			okhttp3.Response response = client.newCall(request1).execute();
			System.out.println(response.code());
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void sendTokenCarolMessage(String contextObject, String serverPath, List<Token> tokenList) {
		// TODO Auto-generated method stub
		String URL = "https://api.gupshup.io/sm/api/bot/WhyQDraft/msg";
		String body = "context=" + contextObject + "&message=" + BotUtils.tokenCoralView(tokenList, serverPath).toString();

		System.out.println(body);
		try {
			okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
			okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			RequestBody body1 = RequestBody.create(mediaType, body);
			okhttp3.Request request1 = new okhttp3.Request.Builder().url(URL)
					.header("apikey", "4f331fc14fb34579c52dd66a805ae1c8").post(body1).build();
			okhttp3.Response response = client.newCall(request1).execute();
			System.out.println(response.code());
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
