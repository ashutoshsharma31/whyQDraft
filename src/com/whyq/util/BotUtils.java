package com.whyq.util;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.whyq.dao.CafeDao;
import com.whyq.dao.OrderDao;
import com.whyq.dao.OrderDataDao;
import com.whyq.model.Cafe;
import com.whyq.model.CartItem;
import com.whyq.model.OrderInformation;
import com.whyq.model.Token;
import com.whyq.session.SessionData;

public class BotUtils {

	public static JSONObject quickReplyTest(String message, ArrayList<String> options, String msgid) {
		// String[] str = { "Red", "Green", "Yellow", "Blue" };
		JSONObject startMessage = new JSONObject();
		startMessage.put("type", "quick_reply")
				.put("content", new JSONObject().put("type", "text").put("text", message)).put("options", options)
				.put("msgid", msgid);

		return startMessage;

	}

	public static JSONObject coralView(List<CartItem> orderList, String serverPath) {

		/*
		 * { "type": "catalogue", "msgid": "cat_212", "items": [{ "title":
		 * "White T Shirt", "subtitle":
		 * "Soft cotton t-shirt \nXs, S, M, L \n$10", "imgurl":
		 * "http://petersapparel.parseapp.com/img/item100-thumb.png", "options":
		 * [ { "type": "url", "title": "View Details", "url":
		 * "http://petersapparel.parseapp.com/img/item100-thumb.png" }, {
		 * "type": "text", "title": "Buy" }
		 * 
		 * ] }, { "title": "Grey T Shirt", "subtitle":
		 * "Soft cotton t-shirt \nXs, S, M, L \n$12", "imgurl":
		 * "http://petersapparel.parseapp.com/img/item101-thumb.png", "options":
		 * [ { "type": "url", "title": "View Details", "url":
		 * "http://petersapparel.parseapp.com/img/item101-thumb.png" }, {
		 * "type": "text", "title": "Buy" }] }] }
		 */

		// List arrOptions = Arrays.asList(options);
		JSONObject my = new JSONObject();
		my.put("type", "catalogue").put("msgid", "coralview");
		// .put("content", new JSONObject().put("type", "text").put("text",
		// message))
		// .put("options", arrOptions).put("msgid", msgid);
		ArrayList<JSONObject> itemObject = new ArrayList<JSONObject>();
		for (CartItem order : orderList) {
			ArrayList<JSONObject> options = new ArrayList<JSONObject>();
			options.add(new JSONObject().put("type", "text").put("title", "Update " + order.getMenuItem().getName()));
			options.add(new JSONObject().put("type", "text").put("title", "Delete " + order.getMenuItem().getName()));
			options.add(new JSONObject().put("type", "text").put("title", "Confirm Order"));
			JSONObject catItem = null;
			if (order.getSize() != null) {
				catItem = new JSONObject()
						.put("title", order.getMenuItem().getName()).put("subtitle", "Size: " + order.getSize()
								+ " Quantity:" + order.getQuantity() + " Price:" + order.getTotalPrice())
						.put("options", options);
			} else {
				catItem = new JSONObject().put("title", order.getMenuItem().getName())
						.put("subtitle", " Quantity:" + order.getQuantity() + " Price:" + order.getTotalPrice())
						.put("options", options);
			}
			catItem.put("imgurl", serverPath + "/img/" + order.getMenuItem().getItemid() + ".jpg");
			itemObject.add(catItem);

		}

		my.put("items", itemObject);

		return my;
	}

	public static JSONObject paymentOptions(String serverPath, OrderInformation orderInformation) {

		// {
		// "type": "survey",
		// "question": "What would you like to do?",
		// "options": ["Eat", "Drink", "{\"type\":\"url\",\"title\":\"View
		// website\",\"url\":\"www.gupshup.io\"}"],
		// "msgid": "3er45"
		// }
		Gson gson = new Gson();
		JSONObject my = new JSONObject();
		my.put("type", "survey").put("question", "How would you like to pay ?");
		ArrayList<JSONObject> options = new ArrayList<JSONObject>();
		String url = serverPath + "/payment.jsp?&orderInformation=" + gson.toJson(orderInformation) + "&contextObject="
				+ orderInformation.getGupshupObject().getContextObj();
		options.add(new JSONObject().put("type", "url").put("title", "Pay Online").put("url", url));
		options.add(new JSONObject().put("type", "text").put("title", "Cash"));
		my.put("options", options).put("msgid", "3er45");
		return my;
	}

	public static String createOrderNumber(int cafeid, int cafeOrderNum) {

		// XXX-DDDYY-00012
		CafeDao cafeDao = new CafeDao();
		String code = cafeDao.getCafeCode(cafeid);

		// Current Day of Year
		Calendar localCalendar = Calendar.getInstance();
		int CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);

		// Current Year
		DateFormat df = new SimpleDateFormat("YY");
		Date date = new Date();
		String datestring = df.format(date).toString();

		// Create Order Number
		String orderNum = code + "-" + CurrentDayOfYear + datestring + "-" + String.format("%05d", cafeOrderNum);
		System.out.println("ordernum " + orderNum);
		return orderNum;
	}

	public static OrderInformation createOrder(SessionData sessionData) {

		// Max Order number for cafe
		OrderDataDao orderDataDao = new OrderDataDao();
		int cafeOrderNum = orderDataDao.getMaxOrderNumForCafe(sessionData.getCafeid()) + 1;

		OrderInformation orderInformation = new OrderInformation();
		orderInformation.setCafeid(sessionData.getCafeid());
		orderInformation.setOrderList(sessionData.getOrderList());
		orderInformation.setGupshupObject(sessionData.getGupshupObject());
		orderInformation.setPayStatus("PENDING");
		orderInformation.setOrderNum(createOrderNumber(sessionData.getCafeid(), cafeOrderNum));
		orderInformation.setMaxnumber(cafeOrderNum);
		float totalAmount = 0;
		for (CartItem cartItem : sessionData.getOrderList()) {
			totalAmount += cartItem.getTotalPrice();
		}

		orderInformation.setTotalamount(totalAmount);

		System.out.println("IN STORE ORDER :: ORDER DETAILS :: " + orderInformation);
		return orderInformation;
	}

	public static JSONObject tokenCoralView(List<Token> tokenList, String serverPath) {

		OrderDao orderDao = new OrderDao();
		JSONObject my = new JSONObject();
		my.put("type", "catalogue").put("msgid", "coralview");
		ArrayList<JSONObject> itemObject = new ArrayList<JSONObject>();
		for (Token token : tokenList) {
			ArrayList<JSONObject> options = new ArrayList<JSONObject>();
			options.add(new JSONObject().put("type", "text").put("title", "Token " + token.getId() + ""));
			JSONObject catItem = null;
			if (token != null) {
				catItem = new JSONObject().put("title", token.getId() + "")
						.put("subtitle", "Order: " + token.getOrderId()).put("options", options);
			}
			catItem.put("imgurl", serverPath + "/img/" + orderDao.getMenuItemId(token.getId()) + ".jpg");
			itemObject.add(catItem);

		}

		my.put("items", itemObject);

		return my;
	}

}
