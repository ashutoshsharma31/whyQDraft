package com.whyq.conf;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseOptions {
	private static String[] mainMenu = { "Confirm Order", "Review Order", "Change Cafe", "Restart Order" };

	public static ArrayList<String> getMainMenuOptions() {
		ArrayList<String> 	option = new ArrayList<String>();
		option = (ArrayList<String>) Arrays.asList(mainMenu);
		return option;
	}

}
