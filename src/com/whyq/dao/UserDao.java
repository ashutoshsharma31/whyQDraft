package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.whyq.controller.WorkServlet;
import com.whyq.model.Cafe;
import com.whyq.model.CartItem;
import com.whyq.model.MenuItem;
import com.whyq.model.OrderInformation;
import com.whyq.model.Size;
import com.whyq.model.User;
import com.whyq.session.SessionData;
import com.whyq.util.DbUtil;

public class UserDao {
	private Connection connection;
	static Logger log = Logger.getLogger(UserDao.class.getName());
	public UserDao() {
		connection = DbUtil.getConnection();
	}

	public boolean isUserPresent(String sender) {
		boolean flag = false;
		try {
			String query = " select username from user where sender = '"+sender+"'";
			log.info("isUserPresent " +query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				flag = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public void saveUser(User user) {
		try {
			String query = " INSERT INTO botdb.user   "+
					
					" (username,                "+
					" sender,                  "+
					" senderobj,               "+
					" contextobj,              "+
					" channel,                 "+
					" lastmsgdttm)             "+
					" VALUES                   "+
					//" ?,?,?,?,?,now());          ";
					" (?,?,?,?,?,now());          ";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getSender());
			preparedStatement.setString(3, user.getSenderObj());
			preparedStatement.setString(4, user.getContextObj());
			preparedStatement.setString(5, user.getChannel());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void saveOrderLines(OrderInformation orderInformation) {
		List<CartItem> cartItemList = orderInformation.getOrderList();
		try {
			for (CartItem cartItem : cartItemList) {
				String query = "insert into orderline(orderid,itemid,size,unitprice,quantity,totalprice,createdtmt) values (?,?,?,?,?,?,now())";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, orderInformation.getOrderNum());
				preparedStatement.setInt(2, cartItem.getMenuItem().getItemid());
				if (cartItem.getMenuItem().getSizeable().equals("Y")) {
					preparedStatement.setInt(3, cartItem.getSize().getSizeid());
				} else {
					preparedStatement.setNull(3, Types.INTEGER);
				}

				preparedStatement.setFloat(4, cartItem.getUnitPrice());
				preparedStatement.setInt(5, cartItem.getQuantity());
				preparedStatement.setFloat(6, cartItem.getTotalPrice());
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void updateLastMessageDtTm(User user) {
		try {
			String query = "update user set lastmsgdttm = now() where sender = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getSender());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getOrderLineId(String orderid, int itemid, String size) {
		int max = 0;
		try {

			String query = "select orderlineid from orderline L, orderdata I " + " where " + " L.orderid = I.orderid"
					+ " and I.orderid = '" + orderid + "'" + " and L.itemid = " + itemid;
			if ("Y".equals(size)) {
				query += " and size = '" + size + "'";
			}
			System.out.println(query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				max = rs.getInt("orderlineid");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max;
	}

	public int getMenuItemId(int itemid) {
		int max = 0;
		try {

			String query = "select itemid from orderline where orderlineid in (select orderlineid from token  where tokenid = "
					+ itemid + ")";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				max = rs.getInt("itemid");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max;
	}

}
