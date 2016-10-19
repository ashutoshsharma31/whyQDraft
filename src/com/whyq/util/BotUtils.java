package com.whyq.util;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BotUtils {
	static Logger log = Logger.getLogger(BotUtils.class.getName());

	public static JSONObject quickReplyTest(String message, ArrayList<String> options, String msgid, boolean addImage,
			String serverPath) {
		// String[] str = { "Red", "Green", "Yellow", "Blue" };
		JSONObject startMessage = new JSONObject();

		ArrayList<JSONObject> optionsJson = new ArrayList<JSONObject>();
		if (addImage) {
			for (String str : options) {
				optionsJson.add(new JSONObject().put("type", "text").put("title", str).put("iconurl",
						serverPath + "/img/tiny_" + str.replace(" ", "_").toLowerCase() + ".jpg"));
			}
		} else {
			for (String str : options) {
				optionsJson.add(new JSONObject().put("type", "text").put("title", str));
			}
		}

		startMessage.put("type", "quick_reply")
				.put("content", new JSONObject().put("type", "text").put("text", message)).put("options", optionsJson)
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
			options.add(new JSONObject().put("type", "text").put("title", "Confirm/Cont. Order"));
			JSONObject catItem = null;
			if (order.getSize() != null) {
				catItem = new JSONObject().put("title", order.getMenuItem().getName())
						.put("subtitle", "Size: " + order.getSize().getDesc() + " Quantity:" + order.getQuantity()
								+ " Price:" + order.getTotalPrice())
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

	public static String sendOrderListAsText(List<CartItem> orderList, String serverPath, int maxCount,
			int currentCount) {
		String message = null;
		int counter = 1;
		for (CartItem cartItem : orderList) {
			message += (counter++) + " " + cartItem.getMenuItem().getName() + " " + cartItem.getSize() + " "
					+ cartItem.getQuantity();
		}

		return message;

	}

	public static JSONObject coralViewFirstNineOrder(List<CartItem> orderList, String serverPath, int maxCount,
			int currentCount) {
		JSONObject my = new JSONObject();
		my.put("type", "catalogue").put("msgid", "coralview");
		ArrayList<JSONObject> itemObject = new ArrayList<JSONObject>();

		int counter = ((currentCount - 1) * 9);
		int limit = counter + 9;
		log.info("Printing coral view for review order from index " + counter + " to " + limit);
		for (int i = counter; i < orderList.size() && i < limit; i++) {
			CartItem order = orderList.get(i);

			ArrayList<JSONObject> options = new ArrayList<JSONObject>();
			options.add(new JSONObject().put("type", "text").put("title", "Update " + order.getMenuItem().getName()));
			options.add(new JSONObject().put("type", "text").put("title", "Delete " + order.getMenuItem().getName()));
			options.add(new JSONObject().put("type", "text").put("title", "Confirm/Cont. Order"));
			JSONObject catItem = null;
			if (order.getSize() != null) {
				catItem = new JSONObject().put("title", order.getMenuItem().getName())
						.put("subtitle", "Size: " + order.getSize().getDesc() + " Quantity:" + order.getQuantity()
								+ " Price:" + order.getTotalPrice())
						.put("options", options);
			} else {
				catItem = new JSONObject().put("title", order.getMenuItem().getName())
						.put("subtitle", " Quantity:" + order.getQuantity() + " Price:" + order.getTotalPrice())
						.put("options", options);
			}
			catItem.put("imgurl", serverPath + "/img/" + order.getMenuItem().getItemid() + ".jpg");
			itemObject.add(catItem);
			counter++;
		}

		ArrayList<JSONObject> nextPreOption = new ArrayList<JSONObject>();

		if (currentCount <= maxCount) {
			nextPreOption.add(new JSONObject().put("type", "text").put("title", "Next Items >>"));
		}
		if (currentCount > 1) {
			nextPreOption.add(new JSONObject().put("type", "text").put("title", "<< Previous Items"));
		}
		nextPreOption.add(new JSONObject().put("type", "text").put("title", "Confirm Order"));

		JSONObject titileNextPre = new JSONObject();
		titileNextPre.put("title", "More Options");
		titileNextPre.put("subtitle", "Choose from Below");
		titileNextPre.put("options", nextPreOption);
		titileNextPre.put("imgurl", serverPath + "/img/moreoptions.jpg");

		itemObject.add(titileNextPre);
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
		idObj.put("id", receipt.getUserid());

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

	public static void sendNextPreMessage(SessionData sessionData, int currentCount, String serverPath) {
		log.info("in sendNextPreMessage " + sessionData.getGupshupObject().getContextObj());
		String contextObject = sessionData.getGupshupObject().getContextObj();
		int maxCount = sessionData.getMaxReviewOrderCounter();
		ArrayList<String> options = new ArrayList<String>();
		if (currentCount == 1) {
			options.add(">> Next 10");
		} else if (maxCount == currentCount) {
			options.add("<< Previous 10");
		} else {
			options.add("<< Previous 10");
			options.add(">> Next 10");
		}
		options.add(" Main Menu ");
		String message = " Please opt for the options ";
		String msgid = "ReviewOrder";
		String URL = "https://api.gupshup.io/sm/api/bot/WhyQDraft/msg";
		String body = "context=" + contextObject + "&message="
				+ quickReplyTest(message, options, msgid, false, serverPath);

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
		log.info("End sendMessage " + contextObject);
	}

}
