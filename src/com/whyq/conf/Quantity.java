package com.whyq.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Quantity {

	private static List quantity = new ArrayList();
	private static String arr[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	static {

		quantity = Arrays.asList(arr);
	}
	public static ArrayList<String> getQuantity() {
		ArrayList<String> arrList = new ArrayList<String>();
		for (String string : arr) {
			arrList.add(string);
		}
		return arrList;

	}
	public static boolean checkMenuItemToOrder(String item) {
		return quantity.contains(item);
	}
	
	public static ArrayList<String> getQuantityFromZero(int max){
		ArrayList<String> arrQty = new ArrayList<String>();
		for (int i = 0; i <= max; i++) {
			arrQty.add(Integer.toString(i));
		}
		return arrQty;
	}
}
