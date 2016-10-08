package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.whyq.model.CartItem;
import com.whyq.model.MenuItem;
import com.whyq.model.Size;
import com.whyq.model.User;
import com.whyq.util.DbUtil;

public class PriceDao {
	private Connection connection;

	public PriceDao() {
		connection = DbUtil.getConnection();
	}

	public CartItem getPriceOfCartItem(CartItem cartItem) {

		try {
			String query = "select amount from price  where menuid = ? and  itemsizeid= ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, cartItem.getMenuItem().getItemid());
			if (cartItem.getMenuItem().getSizeable().equals("Y")) {
				preparedStatement.setInt(2, cartItem.getSize().getSizeid());
			} else {
				//IF Its not sizable the itemsizeid has to be 0
				preparedStatement.setInt(2, 0);
			}
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				cartItem.setUnitPrice(rs.getFloat("amount"));
				cartItem.setTotalPrice(rs.getFloat("amount")*cartItem.getQuantity());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cartItem;
	}
}
