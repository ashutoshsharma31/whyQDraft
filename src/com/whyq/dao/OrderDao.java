package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.whyq.model.CartItem;
import com.whyq.model.MenuItem;
import com.whyq.model.OrderInformation;
import com.whyq.model.Size;
import com.whyq.model.User;
import com.whyq.session.SessionData;
import com.whyq.util.DbUtil;

public class OrderDao {
	private Connection connection;

	public OrderDao() {
		connection = DbUtil.getConnection();
	}

	public void getMaxOrderNumb() {
		try {
			String query = "seelct * from orderinfo";

	        Statement stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery(query);

			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void saveOrder(OrderInformation orderInformation) {

		try {
			String query = "insert into orderinfo(orderid,username,cafeid,numofitem,totalamount, paymethod,paystatus,createdttm) values (?,?,?,?,?,?,?,now())";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, orderInformation.getOrderNum());
			preparedStatement.setString(2, orderInformation.getGupshupObject().getSender());
			preparedStatement.setInt(3, orderInformation.getCafeid());
			preparedStatement.setInt(4, orderInformation.getOrderList().size());
			preparedStatement.setFloat(5, orderInformation.getTotalamount());
			preparedStatement.setString(6, orderInformation.getPayMethod());
			preparedStatement.setString(7, orderInformation.getPayStatus());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void saveOrderLines(OrderInformation orderInformation) {
		List<CartItem> cartItemList = orderInformation.getOrderList();
		try {
			for (CartItem cartItem : cartItemList) {
				String query = "insert into orderline(orderid,itemid,size,unitprice,quantity,totalprice) values (?,?,?,?,?,?)";
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

	public void updateOrderStatus(OrderInformation orderInformation) {
		try {
			String query = "update orderinfo set paystatus = ? , paymentdttm=now() where orderid = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "COMPLETE");
			preparedStatement.setString(2, orderInformation.getOrderNum());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getOrderLineId(String orderid, int itemid, String size) {
		int max = 0;
		try {

			String query = "select orderlineid from orderline L, orderdata I " + " where " + " L.orderid = I.ordernum"
					+ " and I.ordernum = '" + orderid + "'" + " and L.itemid = " + itemid;
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
