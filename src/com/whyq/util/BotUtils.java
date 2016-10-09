package com.whyq.util;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.whyq.controller.WorkServlet;
import com.whyq.dao.CafeDao;
import com.whyq.dao.ConfigDao;
import com.whyq.dao.OrderDao;
import com.whyq.dao.OrderDataDao;
import com.whyq.model.Cafe;
import com.whyq.model.CartItem;
import com.whyq.model.Config;
import com.whyq.model.GupshupObject;
import com.whyq.model.OrderInformation;
import com.whyq.model.Receipt;
import com.whyq.model.ReceiptElement;
import com.whyq.model.Token;
import com.whyq.session.SessionData;

public class BotUtils {
	static Logger log = Logger.getLogger(BotUtils.class.getName());

	public static JSONObject quickReplyTest(String message, ArrayList<String> options, String msgid) {
		// String[] str = { "Red", "Green", "Yellow", "Blue" };
		JSONObject startMessage = new JSONObject();
		startMessage.put("type", "quick_reply")
				.put("content", new JSONObject().put("type", "text").put("text", message)).put("options", options)
				.put("msgid", msgid);

		return startMessage;

	}

	public static JSONObject coralView(List<CartItem> orderList, String serverPath) {

		JSONObject my = new JSONObject();
		my.put("type", "catalogue").put("msgid", "coralview");
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

		Gson gson = new Gson();
		JSONObject my = new JSONObject();
		my.put("type", "survey").put("question", "How would you like to pay ?");

		// Fetch all payment options from config

		ConfigDao configDao = new ConfigDao();

		ArrayList<JSONObject> options = new ArrayList<JSONObject>();

		if (configDao.isConfigPresent("PAYMENTS", "ONLINE")) {
			String url = serverPath + "/payment.jsp?&orderInformation=" + gson.toJson(orderInformation)
					+ "&contextObject=" + orderInformation.getGupshupObject().getContextObj();

			options.add(new JSONObject().put("type", "url").put("title", "Pay Online").put("url", url)
					.put("webview_height_ratio", "tall"));
		}
		if (configDao.isConfigPresent("PAYMENTS", "CASH")) {
			options.add(new JSONObject().put("type", "text").put("title", "Cash"));
		}

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

		JSONObject my = new JSONObject();
		my.put("type", "catalogue").put("msgid", "coralview");
		ArrayList<JSONObject> itemObject = new ArrayList<JSONObject>();
		for (Token token : tokenList) {
			JSONObject catItem = null;
			if (token != null) {
				catItem = new JSONObject().put("title", "Token Number " + token.getId() + "").put("subtitle",
						"Order Id: " + token.getOrderId());
			}
			itemObject.add(catItem);
		}

		my.put("items", itemObject);
		return my;
	}

	public static JSONObject receiptTemplate(String serverPath, Receipt receipt) {

		JSONObject idObj = new JSONObject();
		JSONObject messageObj = new JSONObject();
		JSONObject attachementObj = new JSONObject();
		JSONObject payloadObj = new JSONObject();
		JSONObject totalcostObj = new JSONObject();

		JSONObject my = new JSONObject();

		ArrayList<JSONObject> itemObject = new ArrayList<JSONObject>();
		for (ReceiptElement receiptElement : receipt.getReceiptElementList()) {
			JSONObject elementObj = new JSONObject();
			elementObj.put("title", receiptElement.getTitle())
					.put("subtitle", "Token Number: " + receiptElement.getSubtitle())
					.put("quantity", receiptElement.getQuantity()).put("price", receiptElement.getPrice())
					.put("currency", receiptElement.getCurrency())
					.put("image_url", serverPath + "/img/" + receiptElement.getItemid() + ".jpg");
			itemObject.add(elementObj);

		}

		totalcostObj.put("total_cost", receipt.getTotal_cost());
		payloadObj.put("template_type", "receipt").put("recipient_name", receipt.getRecipientName())
				.put("order_number", receipt.getOrderNumber()).put("currency", receipt.getCurrency())
				.put("payment_method", "Online/Cash").put("timestamp", receipt.getTimestamp())
				.put("elements", itemObject).put("summary", totalcostObj);

		attachementObj.put("type", "template").put("payload", payloadObj);
		messageObj.put("attachment", attachementObj);
		idObj.put("id", receipt.getRecipientName());

		my.put("recipient", idObj).put("message", messageObj);

		return my;
	}

	public static void checkValidStatus(ArrayList<String> options) {
		log.info("Option set with size " + options.size());
		if (options.size() > 10) {
			log.error("No Response will be sent because option list send has more than 10 values. Values are :"
					+ options.size());

		}
	}

}
