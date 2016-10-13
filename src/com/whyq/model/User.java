package com.whyq.model;

import java.util.Date;

public class User {

	private int userid;
	private String username;
	private String sender;
	private String senderObj;
	private String contextObj;
	private String channel;
	private String lastmsgdttm;
	private String channelType;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderObj() {
		return senderObj;
	}

	public void setSenderObj(String senderObj) {
		this.senderObj = senderObj;
	}

	public String getContextObj() {
		return contextObj;
	}

	public void setContextObj(String contextObj) {
		this.contextObj = contextObj;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLastmsgdttm() {
		return lastmsgdttm;
	}

	public void setLastmsgdttm(String lastmsgdttm) {
		this.lastmsgdttm = lastmsgdttm;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	@Override
	public String toString() {
		return "User [userid=" + userid + ", username=" + username + ", sender=" + sender + ", senderObj=" + senderObj
				+ ", contextObj=" + contextObj + ", channel=" + channel + ", lastmsgdttm=" + lastmsgdttm
				+ ", channelType=" + channelType + "]";
	}

}