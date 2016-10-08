package com.whyq.model;


public class GupshupObject
{
	private String messageType;
	private String userMessage;
	private String channelId;
	private String channelType;
	private String contextObj;
	private String sender;

	public String getMessageType()
	{
		return messageType;
	}

	public void setMessageType(String messageType)
	{
		this.messageType = messageType;
	}

	public String getUserMessage()
	{
		return userMessage;
	}

	public void setUserMessage(String userMessage)
	{
		this.userMessage = userMessage;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getChannelType()
	{
		return channelType;
	}

	public void setChannelType(String channelType)
	{
		this.channelType = channelType;
	}

	public String getContextObj()
	{
		return contextObj;
	}

	public void setContextObj(String contextObj)
	{
		this.contextObj = contextObj;
	}
	
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		return "GupshupObject [messageType=" + messageType + ", userMessage=" + userMessage + ", channelId=" + channelId
				+ ", channelType=" + channelType + ", contextObj=" + contextObj + ", sender=" + sender + "]";
	}

}
