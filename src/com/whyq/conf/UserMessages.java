package com.whyq.conf;

import java.util.Arrays;

public class UserMessages {
	private static String[] startEvent = { "hi", "hello", "wassup", "how are you", "good morning", "good evening",
			"good afternoon","startchattingevent" };
	public static String[] mainEvent = { "Main Menu", "Back" };

	public static boolean isStartEvent(String usermessage) {
		return Arrays.asList(startEvent).contains(usermessage.toLowerCase());
	}

	public static boolean isMainEvent(String usermessage) {
		return Arrays.asList(mainEvent).contains(usermessage.toLowerCase());
	}

}
