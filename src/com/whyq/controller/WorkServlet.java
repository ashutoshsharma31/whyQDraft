package com.whyq.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.whyq.conf.BaseOptions;
import com.whyq.conf.Quantity;
import com.whyq.conf.StatusMessages;
import com.whyq.dao.CafeDao;
import com.whyq.dao.OrderDataDao;
import com.whyq.dao.ReceiptDao;
import com.whyq.dao.SizeDao;
import com.whyq.dao.TokenDao;
import com.whyq.model.Cafe;
import com.whyq.model.CartItem;
import com.whyq.model.GupshupObject;
import com.whyq.model.MenuItem;
import com.whyq.model.MenuItemsForCafe;
import com.whyq.model.OrderInformation;
import com.whyq.model.Receipt;
import com.whyq.model.Size;
import com.whyq.session.SessionData;
import com.whyq.session.Storage;
import com.whyq.util.BotUtils;
import com.whyq.util.CommonUtils;
import com.whyq.util.ParseUtils;
import com.whyq.util.TokenUtils;

/**
 * Servlet implementation class WorkServlet
 */
@WebServlet("/WorkServlet")
public class WorkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(WorkServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WorkServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("Request Recieved " + request);

		// Write Object for response writing
		PrintWriter writer = response.getWriter();
		String serverPath = request.getRequestURL().substring(0,
				request.getRequestURL().length() - request.getServletPath().length());
		GupshupObject go = ParseUtils.parseRequestToGupsupObject(request);
		String orderStatus = StatusMessages.START;
		String itemStatus = StatusMessages.START;

		SessionData sessionData = Storage.getObject(go.getChannelId());
		MenuItemsForCafe menuItemsForCafe = new MenuItemsForCafe();
		// Set Session Object or Update
		if (sessionData != null) {
			orderStatus = sessionData.getOrderStatus();
			itemStatus = sessionData.getItemStatus();
		} else {
			sessionData = new SessionData();
			// sessionData.setOrderStatus(orderStatus);
			// sessionData.setItemStatus(itemStatus);
			// menuItemsForCafe.loadMenuItems(2);
			sessionData.setMenuItemsForCafe(menuItemsForCafe);
			sessionData.setGupshupObject(go);
			Storage.addElementToMap(go.getChannelId(), sessionData);
		}
		menuItemsForCafe = sessionData.getMenuItemsForCafe();
		String usermessage = go.getUserMessage();
		log.debug("---------USER MESSAGE ----- " + usermessage);

		// Parse Messages
		// For the start of conversation
		if (CommonUtils.getEvent(usermessage).equals("START")) {
			log.debug("************************ 0 ************************");
			String message = "From Which cafe would you like to order? ";
			CafeDao cafeDao = new CafeDao();
			ArrayList<String> options = new ArrayList<String>();
			options = cafeDao.getAllCafeList();
			BotUtils.checkValidStatus(options);
			String msgid = "CafeMessage";
			sessionData.setCafeDefine(StatusMessages.CAFE_DEFINE_START);
			log.debug("this is start of chat !");
			writer.println(BotUtils.quickReplyTest(message, options, msgid));
		} else if (CommonUtils.getEvent(usermessage).equals("MAIN")) {
			log.debug("************************ 0 ************************");
			String message = "What would you like to do? ";
			ArrayList<String> options = new ArrayList<String>();
			options = BaseOptions.getMainMenuOptions();
			BotUtils.checkValidStatus(options);
			String msgid = "MainMenu";
			sessionData.setCafeDefine(StatusMessages.CAFE_DEFINE_START);
			writer.println(BotUtils.quickReplyTest(message, options, msgid));
		}

		else if (StatusMessages.CAFE_DEFINE_START.equals(sessionData.getCafeDefine())) {
			log.debug("************************ 1 ************************");
			Cafe cafe = new Cafe();
			CafeDao cafeDao = new CafeDao();
			cafe = cafeDao.getCafeDetails(usermessage.trim());
			if (cafe != null && cafe.getCafeid() != 0) {
				log.debug(" Setting Cafe ID as " + cafe.getCafeid() + " " + cafe.getName());
				sessionData.setCafeid(cafe.getCafeid());
				sessionData.setCafeDefine(StatusMessages.CAFE_DEFINED);
				menuItemsForCafe.loadMenuItems(cafe.getCafeid());
				String message = "What would you like to order ?";
				ArrayList<String> options = new ArrayList<String>();
				options = menuItemsForCafe.getStartOptions();
				BotUtils.checkValidStatus(options);
				String msgid = "StartMessage";
				sessionData.setOrderStatus(StatusMessages.ORDER_START);
				sessionData.setItemStatus(StatusMessages.ITEM_START);
				writer.println(BotUtils.quickReplyTest(message, options, msgid));
			} else {
				String message = "Didn't understand your response! Please select the cafe ";
				ArrayList<String> options = new ArrayList<String>();
				options = cafeDao.getAllCafeList();
				BotUtils.checkValidStatus(options);
				String msgid = "CafeMessage";
				sessionData.setCafeDefine(StatusMessages.CAFE_DEFINE_START);
				writer.println(BotUtils.quickReplyTest(message, options, msgid));

			}

		}
		// UPDATE Quantity
		else if (usermessage.trim().contains("Update")) {
			String itemName = usermessage.substring(7, usermessage.length() - 2);
			int index = Integer.parseInt(usermessage.substring(usermessage.length() - 1));
			ArrayList<String> options = new ArrayList<String>();
			options = Quantity.getQuantity();
			BotUtils.checkValidStatus(options);
			String message = "Please specify new quantity of " + itemName;
			sessionData.setItemStatus(StatusMessages.ITEM_NEW_QUANTITY);
			String msgid = "update";
			CartItem item = sessionData.getOrderList().get(index - 1);
			sessionData.setCartItem(item);
			log.debug(item + "  added as current item !");
			writer.println(BotUtils.quickReplyTest(message, options, msgid));
		}

		// Update Quantity Number
		else if (StatusMessages.ITEM_NEW_QUANTITY.equals(sessionData.getItemStatus())) {
			CartItem item = sessionData.getCartItem();
			item.setQuantity(Integer.parseInt(usermessage.trim()));
			sessionData.updatePrice();
			String message = "Item updated .. What else ?";
			String msgid = "ITEM_UPDATED";
			ArrayList<String> options = new ArrayList<String>();
			options = menuItemsForCafe.getReviewOrder();
			BotUtils.checkValidStatus(options);
			sessionData.initialzeAfterItemAddition();
			sessionData.setItemStatus(StatusMessages.ITEM_COMPLETE);
			sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);
			writer.println(BotUtils.quickReplyTest(message, options, msgid));
		}
		// DELETE ITEM
		else if (usermessage.contains("Delete")) {
			String itemName = usermessage.substring(7);
			int index = Integer.parseInt(usermessage.substring(usermessage.length() - 1));
			sessionData.removeOrderFromListUsingIndex(index - 1);
			String message = "Item deleted .. what else ?";
			String msgid = "ITEM_DELETED";
			ArrayList<String> options = new ArrayList<String>();
			options = menuItemsForCafe.getReviewOrder();
			BotUtils.checkValidStatus(options);
			sessionData.initialzeAfterItemAddition();
			sessionData.setItemStatus(StatusMessages.ITEM_COMPLETE);
			sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);
			writer.println(BotUtils.quickReplyTest(message, options, msgid));
		}

		// REVIEW ORDER
		else if (usermessage.contains("Review Order")) {
			List<CartItem> orderList = sessionData.getOrderList();
			String message = "";
			for (CartItem order : orderList) {
				log.debug(order);
				message += order;
			}
			// String[] options = menuItemsForCafe.getMenuItems("Review Order");
			JSONObject coralObject = BotUtils.coralView(orderList, serverPath);
			log.debug("Coral Object" + coralObject);
			writer.println(coralObject);

		}

		else if (usermessage.contains("Confirm Order")) {

			// WRITE CODE FOR PAYMENT BUTTON

			OrderInformation orderInformation = BotUtils.createOrder(sessionData);
			JSONObject paymentOption = BotUtils.paymentOptions(serverPath, orderInformation);
			OrderDataDao orderDataDao = new OrderDataDao();
			orderDataDao.saveOrder(orderInformation);
			orderDataDao.saveOrderLines(orderInformation);
			sessionData.setOrderInformation(orderInformation);
			log.debug("paymentOption Object" + paymentOption);
			//sessionData.clearOrderList();
			writer.println(paymentOption);
		}

		else if (usermessage.toLowerCase().equals("cash")) {
			OrderDataDao orderDataDao = new OrderDataDao();
			TokenDao tokenDao = new TokenDao();
			TokenUtils tokenUtils = new TokenUtils();
			orderDataDao.updateOrderStatus(sessionData.getOrderInformation());
			log.debug(" Update of Order status is complete ");
			tokenDao.createTokens(sessionData.getOrderInformation());
			log.debug(" Token Creation complete.");
			//tokenUtils.sendTokenCarolMessage(sessionData.getOrderInformation().getGupshupObject().getContextObj(),
			//		serverPath, tokenDao.getAllTokensForOrder(sessionData.getOrderInformation().getOrderNum()));
			//log.debug(" Sending token is completed to facebook. ");
			Receipt receipt = new Receipt();
			ReceiptDao receiptDao = new ReceiptDao();
			receipt = receiptDao.getReceipt(sessionData.getOrderInformation().getOrderNum());			
			tokenUtils.sendReceipt(sessionData.getOrderInformation().getGupshupObject().getContextObj(), serverPath, receipt);
			log.debug(" Sending receipt is completed to facebook. ");
			sessionData.clearOrderList();
		}

		// For quantity of the product
		else if ((StatusMessages.ITEM_QUANTITY.equals(sessionData.getItemStatus())
				|| StatusMessages.ITEM_SIZE.equals(sessionData.getItemStatus())
				|| StatusMessages.ITEM_MULTIPLE_SIZE.equals(sessionData.getItemStatus())
				|| StatusMessages.ITEM_MULTIPLE_SIZE_COUNTER.equals(sessionData.getItemStatus()))
				&& ParseUtils.isInteger(usermessage.trim())) {
			log.debug("************************ 2 ************************");
			CartItem item = sessionData.getCartItem();
			log.debug(" CART ITEM TO PROCESS is " + item);
			if (menuItemsForCafe.isSizeable(item.getMenuItem())) {
				// If the product is sizable
				log.debug("************************ 3 ************************");
				SizeDao sizeDao = new SizeDao();
				if (sessionData.getItemStatus().equals(StatusMessages.ITEM_QUANTITY)) {
					sessionData.setRemQuantity(Integer.parseInt(usermessage.trim()));
				} else {
					sessionData.setRemQuantity(sessionData.getRemQuantity() - Integer.parseInt(usermessage.trim()));
				}

				if (Integer.parseInt(usermessage.toLowerCase().trim()) == 1
						&& !sessionData.getItemStatus().equals(StatusMessages.ITEM_MULTIPLE_QUANTITY)
						&& !sessionData.getItemStatus().equals(StatusMessages.ITEM_MULTIPLE_SIZE_COUNTER)
						&& !sessionData.getItemStatus().equals(StatusMessages.ITEM_MULTIPLE_SIZE)) {
					// if quantity choose is 1
					log.debug("************************ 4 ************************");
					String msgid = "SIZE_SELECT_ONE_QTY";
					ArrayList<String> options = new ArrayList<String>();
					String message = "What size do you want that? ";
					options = sizeDao.getAllSizesForItem(item.getMenuItem());
					BotUtils.checkValidStatus(options);
					sessionData.setItemStatus(StatusMessages.ITEM_SINGLE_SIZE);
					sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);
					writer.println(BotUtils.quickReplyTest(message, options, msgid));
				} else {
					// Sizable Item quantity more than 1
					log.debug("************************ 5 ************************");
					ArrayList<Size> sizeList = sessionData.getSizesList();
					int sizeCounter = sessionData.getSizeCounter();

					int remQty = sessionData.getRemQuantity();
					if (sizeList.isEmpty()) {
						// Fill the size list
						log.debug("************************ 6 ************************");
						sizeList = sizeDao.getAllSizes(item.getMenuItem());
						sessionData.setSizesList(sizeList);
						remQty = Integer.parseInt(usermessage);
					}
					if (remQty == 0 || sizeCounter == sizeList.size() - 1) {
						// for last item
						log.debug("************************ 7 ************************");
						String message = "Item added ..  Whats else ?";
						if (Integer.parseInt(usermessage) > 0) {
							CartItem finalitem = new CartItem(item.getMenuItem(), Integer.parseInt(usermessage),
									sizeList.get(sizeCounter));
							sessionData.addItemToCart(finalitem);
						}
						String msgid = "ITEM_COMPLETED";
						sessionData.initialzeAfterItemAddition();
						ArrayList<String> options = new ArrayList<String>();
						options = menuItemsForCafe.getReviewOrder();
						BotUtils.checkValidStatus(options);
						sessionData.setItemStatus(StatusMessages.ITEM_COMPLETE);
						sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);
						writer.println(BotUtils.quickReplyTest(message, options, msgid));
					} else {
						// all the items except last
						log.debug(
								"************************ 9 ************************ size counter" + sizeCounter + " ");
						ArrayList<String> options = new ArrayList<String>();
						options = Quantity.getQuantityFromZero(sessionData.getRemQuantity());
						BotUtils.checkValidStatus(options);
						log.debug(" STATUS " + sizeCounter + " " + sessionData.getItemStatus());
						if (sizeCounter == 0
								&& !sessionData.getItemStatus().equals(StatusMessages.ITEM_MULTIPLE_SIZE_COUNTER)) {
							// FIRST COUNTER .. No need to add the item
							log.debug("************************ 123 ************************");
							Size size = sizeList.get(sizeCounter);
							item.setSize(size);
							// item.setQuantity(Integer.parseInt(usermessage));
							String message = "How many " + size.getDesc() + " ? ";
							String msgid = "ITEM_MULTIPLE_SIZE_COUNTER";
							// sessionData.setSizeCounter(sizeCounter+1);
							sessionData.setItemStatus(StatusMessages.ITEM_MULTIPLE_SIZE_COUNTER);
							writer.println(BotUtils.quickReplyTest(message, options, msgid));
						} else {
							log.debug("************************ 1000 ************************");
							log.debug(item.getMenuItem() + " " + Integer.parseInt(usermessage) + " "
									+ sizeList.get(sizeCounter).getDesc());
							if (Integer.parseInt(usermessage) > 0) {
								CartItem finalitem = new CartItem(item.getMenuItem(), Integer.parseInt(usermessage),
										sizeList.get(sizeCounter));
								sessionData.addItemToCart(finalitem);
							}
							sessionData.setSizeCounter(sizeCounter + 1);
							String msgid = "ITEM_MULTIPLE_SIZE_COUNTER";
							String message = "How many " + sizeList.get(sizeCounter + 1).getDesc() + " ? ";
							writer.println(BotUtils.quickReplyTest(message, options, msgid));
						}

						sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);

					}
				}
			} else {
				// Product is not sizeable
				log.debug("************************ 13 ************************");
				item.setQuantity(Integer.parseInt(usermessage.trim()));
				String message = "Item added " + item.getMenuItem().getName() + " with quantity " + item.getQuantity()
						+ "! What else ?";
				String msgid = "itemaddednosize";
				ArrayList<String> options = new ArrayList<String>();
				options = menuItemsForCafe.getReviewOrder();
				BotUtils.checkValidStatus(options);
				sessionData.addItemToCart(item);
				sessionData.setItemStatus(StatusMessages.ITEM_COMPLETE);
				sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);
				writer.println(BotUtils.quickReplyTest(message, options, msgid));
			}

		}

		else if (StatusMessages.ITEM_SINGLE_SIZE.equals(sessionData.getItemStatus())) {
			log.debug("************************ 14 ************************");
			// Item added to cartList with size .. this is only for 1 item
			CartItem item = sessionData.getCartItem();
			SizeDao sizeDao = new SizeDao();
			ArrayList<String> sizeList = sizeDao.getAllSizesForItem(item.getMenuItem());
			ArrayList<Size> listOfSizes = sizeDao.getAllSizes(item.getMenuItem());
			log.debug(sizeList);
			if (sizeList.contains(usermessage)) {
				log.debug("************************ 15 ************************");
				log.debug("Size Found ");
				item.setSize(CommonUtils.getSizeByName(listOfSizes, usermessage));
				item.setQuantity(1);
				sessionData.addItemToCart(item);
				String message = "Item added ..  Whats else ?";
				String msgid = "ITEM_COMPLETED";
				ArrayList<String> options = new ArrayList<String>();
				options = menuItemsForCafe.getReviewOrder();
				BotUtils.checkValidStatus(options);
				sessionData.setItemStatus(StatusMessages.ITEM_COMPLETE);
				sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);
				writer.println(BotUtils.quickReplyTest(message, options, msgid));
			} else {
				log.debug("************************ 16 ************************");
				log.debug("SIZE NOT IDENTIFIED ... HANDLE IT");
			}
		}

		// If the message received is a product or menu item
		else if (menuItemsForCafe.hasItem(usermessage)) {
			log.debug("************************ 17 ************************");
			MenuItem menuItem = new MenuItem();
			menuItem = menuItemsForCafe.getMenuItems(usermessage);
			sessionData.setItemStatus(StatusMessages.ITEM_IN_PROGRESS);
			sessionData.setOrderStatus(StatusMessages.ORDER_IN_PROGRESS);
			String msgid = "SetQuantity";
			if (menuItemsForCafe.checkMenuItemToOrder(menuItem)) {
				log.debug("************************ 18 ************************");
				ArrayList<String> options = new ArrayList<String>();
				options = Quantity.getQuantity();
				BotUtils.checkValidStatus(options);
				String message = "Please specify quantity of " + usermessage;
				sessionData.setItemStatus(StatusMessages.ITEM_QUANTITY);
				sessionData.setCartItem(new CartItem(menuItem));
				writer.println(BotUtils.quickReplyTest(message, options, msgid));

			} else {
				log.debug("************************ 19 ************************");
				// This is a parent menu
				// Will send the child product as response
				ArrayList<String> options = new ArrayList<String>();
				options = menuItemsForCafe.getNextOptions(menuItem);
				BotUtils.checkValidStatus(options);
				String message = "Which " + usermessage + " you like to order ?";
				msgid = "ParentProduct";
				writer.println(BotUtils.quickReplyTest(message, options, msgid));
			}

		} else {
			String message = "Sorry I didn't get you ! Please confirm Which cafe would you like to order? ";
			CafeDao cafeDao = new CafeDao();
			ArrayList<String> options = new ArrayList<String>();
			options = cafeDao.getAllCafeList();
			BotUtils.checkValidStatus(options);
			String msgid = "CafeMessage";
			sessionData.setCafeDefine(StatusMessages.CAFE_DEFINE_START);
			writer.println(BotUtils.quickReplyTest(message, options, msgid));
		}

		// log.debug(sessionData);

		log.debug(sessionData.getSizesList());
		log.debug(sessionData.getOrderList());

		writer.flush();
		writer.close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
