package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.whyq.model.Receipt;
import com.whyq.model.ReceiptElement;
import com.whyq.util.DbUtil;

public class ReceiptDao {
	private Connection connection;

	public ReceiptDao() {
		connection = DbUtil.getConnection();
	}

	public ArrayList<ReceiptElement> getReceiptElement(String orderid) {
		ArrayList<ReceiptElement> receiptElementList = new ArrayList<ReceiptElement>();
		try {
			String query = "select t.tokenid, t.orderid, t.orderlineid, o.itemid, o.quantity,o.totalprice, o.createdtmt, m.name "
					+ " from token t, orderline o , menuitem m  where "
					+ " t.orderid = o.orderid and t.orderlineid = o.orderlineid"
					+ " and o.orderid = ? and m.itemid = o.itemid";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, orderid);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReceiptElement receiptElement = new ReceiptElement();
				receiptElement.setTitle(rs.getString("name"));
				receiptElement.setPrice(rs.getInt("totalprice"));
				receiptElement.setSubtitle(rs.getString("tokenid"));
				receiptElement.setQuantity(rs.getString("quantity"));
				receiptElement.setItemid(rs.getString("itemid"));
				receiptElementList.add(receiptElement);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return receiptElementList;
	}

	public Receipt getReceipt(String orderid) {
		Receipt receipt = new Receipt();
		try {
			String query = "select c.cafeid,c.cafename,o.ordernum ,o.totalamount ,o.username, o.paymethod, unix_timestamp(o.createdttm) as createdttm "
					+ "from orderdata o, cafe c " + " where  c.cafeid = o.cafeid and ordernum = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, orderid);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {

				receipt.setCafename(rs.getString("cafename"));
				receipt.setOrderNumber(rs.getString("ordernum"));
				receipt.setCurrency("INR");
				receipt.setRecipientName(rs.getString("username"));
				receipt.setPaymentMethod("CASH");
				receipt.setTotal_cost(rs.getInt("totalamount"));
				receipt.setTimestamp(rs.getString("createdttm"));
				receipt.setCafeid(rs.getInt("cafeid"));
			}
			receipt.setReceiptElementList(getReceiptElement(orderid));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return receipt;
	}

}
