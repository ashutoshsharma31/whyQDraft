package com.whyq.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.whyq.model.MenuItem;
import com.whyq.util.DbUtil;

public class MenuItemsForCafe {
	private HashMap<String, MenuItem> menuMap = new HashMap<String, MenuItem>();
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();
	private Connection connection;

	public MenuItemsForCafe() {
		
	}

	public boolean hasItem(String key) {
		if (menuMap.get(key) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	

	public MenuItem getMenuItems(String key) {

		return menuMap.get(key);

	}

	public void printAllMenuItems() {
		for (String menuName : menuMap.keySet()) {
			MenuItem menuItem = menuMap.get(menuName);
			System.out.println(menuItem);

		}
	}

	public boolean checkMenuItemToOrder(MenuItem menuItem) {
		return menuItem.getType().equals("P");
	}

	public boolean isSizeable(MenuItem menuItem) {
		return menuItem.getSizeable().equals("Y");
	}

	public ArrayList<String> getStartOptions() {
		ArrayList<String> startArr = new ArrayList<String>();
		for (MenuItem menuObj : menuItems) {
			if (menuObj.getParentid() == 0) {
				startArr.add(menuObj.getName());
			}
		}
		return startArr;
	}
	public ArrayList<String> getNextOptions(MenuItem menuItem) {
		ArrayList<String> nextArr = new ArrayList<String>();
		for (MenuItem menuObj : menuItems) {
			if (menuObj.getParentid() == menuItem.getItemid()) {
				nextArr.add(menuObj.getName());
			}
		}
		return nextArr;
	}

	public ArrayList<String> getReviewOrder() {
		ArrayList<String> confirm = new ArrayList<String>();
		confirm.add("Confirm Order");
		confirm.add("Review Order");
		confirm.addAll(getStartOptions());
		return confirm;
	}
	
	
	public void loadMenuItems(int cafeid){
		try {
			connection = DbUtil.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from menuitem where cafeid='"+cafeid+"'");
			while (rs.next()) {
				MenuItem menuItem = new MenuItem();
				menuItem.setItemid(rs.getInt("itemid"));
				menuItem.setCafeid(rs.getInt("cafeid"));
				menuItem.setName(rs.getString("name"));
				menuItem.setSizeable(rs.getString("sizeable"));
				menuItem.setParentid(rs.getInt("parentid"));
				menuItem.setType(rs.getString("type"));
				menuMap.put(menuItem.getName(), menuItem);
				menuItems.add(menuItem);
			}
		} catch (SQLException e) {
			System.out.println("--------------- ERROR IN LOADING MENU ITEM .. 1 -----");
			e.printStackTrace();
		}
		catch (Exception e){
			System.out.println("--------------- ERROR IN LOADING MENU ITEM .. 2 -----");
			e.printStackTrace();
		}
		System.out.println("MenuMap  "+ menuMap);
		System.out.println("MenuItems  "+ menuItems);
	}
	
	public void addMenuItemToCafe(MenuItem menuItem){
		menuMap.put(menuItem.getName(), menuItem);
		menuItems.add(menuItem);
	}
	
	
}
