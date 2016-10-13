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
			String query = "select o.orderid,o.itemid, o.quantity,o.totalprice, o.createdtmt, o.size, s.sizeid ,s.desc "
					+ " , t.tokenid , m.name " 
					+ " from orderline o  " 
					+ " left join size s on s.sizeid = o.size "
					+ " inner join token t on t.orderlineid = o.orderlineid "
					+ " inner join menuitem m on m.itemid = o.itemid " 
					+ " where o.orderid = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, orderid);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReceiptElement receiptElement = new ReceiptElement();
				if(rs.getString("desc")!=null){
					receiptElement.setTitle(rs.getString("name") + " - " + rs.getString("desc"));
				}
				else{
					receiptElement.setTitle(rs.getString("name"));
				}
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
			String query = "select c.cafeid,c.cafename,o.ordernum ,o.totalamount "
					+ " ,o.username as userid, o.paymethod "
					+ " ,unix_timestamp(o.createdttm) as createdttm , u.username as recipientname"
					+ " from orderdata o, cafe c, user u "
					+ " where  c.cafeid = o.cafeid"
					+ " and u.sender = o.username "
					+ " and ordernum = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, orderid);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {

				receipt.setCafename(rs.getString("cafename"));
				receipt.setOrderNumber(rs.getString("ordernum"));
				receipt.setCurrency("INR");
				receipt.setRecipientName(rs.getString("recipientname"));
				receipt.setPaymentMethod("CASH");
				receipt.setTotal_cost(rs.getInt("totalamount"));
				receipt.setTimestamp(rs.getString("createdttm"));
				receipt.setCafeid(rs.getInt("cafeid"));
				receipt.setUserid(rs.getString("userid"));
			}
			receipt.setReceiptElementList(getReceiptElement(orderid));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return receipt;
	}

}
