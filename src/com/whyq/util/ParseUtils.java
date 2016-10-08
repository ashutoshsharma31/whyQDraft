package com.whyq.util;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.whyq.model.GupshupObject;

public class ParseUtils {
	public static GupshupObject parseRequestToGupsupObject(HttpServletRequest request) throws JSONException {
		String messageObj = request.getParameter("messageobj");
		String contextObj = request.getParameter("contextobj");
		String senderObj = request.getParameter("senderobj");
		JSONObject messageObjeObject = new JSONObject(messageObj);
		JSONObject senderObjeObject = new JSONObject(senderObj);
		String messageType = messageObjeObject.getString("type");
		String userMessage = messageObjeObject.getString("text");
		String channelType = senderObjeObject.getString("channeltype");
		String sender = senderObjeObject.getString("channelid");
		System.out.println("SENDER :: "+sender);
		String channelid = null;
		if (channelType.equalsIgnoreCase("telegram")) {
			long id = senderObjeObject.getLong("channelid");
			channelid = String.valueOf(id);
		} else {
			channelid = senderObjeObject.getString("channelid");
		}

		GupshupObject gs = new GupshupObject();
		try {
			gs.setChannelId(channelid);
			gs.setChannelType(channelType);
			gs.setMessageType(messageType);
			gs.setUserMessage(userMessage);
			gs.setContextObj(contextObj);
			gs.setSender(sender);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gs;
	}
	public static boolean isInteger(String str) {
		return str.matches("^[0-9]+$");
	}
}
