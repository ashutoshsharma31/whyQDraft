package com.whyq.util;

import java.util.ArrayList;
import java.util.List;

import com.whyq.conf.UserMessages;
import com.whyq.model.Size;

public class CommonUtils {
	public static String getEvent(String usermessage) {
		String eventName = "NOT_START";
		if (UserMessages.isStartEvent(usermessage)) {
			eventName = "START";
		} else if (UserMessages.isMainEvent(usermessage)) {
			eventName = "MAIN";
		}
		return eventName;
	}

	public static Size getSizeByName(ArrayList<Size> sizeList, String strSize) {
		for (Size size : sizeList) {
			if (size.getDesc().equals(strSize)) {
				return size;
			}
		}
		return null;

	}

}
