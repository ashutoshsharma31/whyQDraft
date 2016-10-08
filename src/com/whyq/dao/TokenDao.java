package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.whyq.model.Token;
import com.whyq.util.DbUtil;

public class TokenDao {
	private Connection connection;

	public TokenDao() {
		connection = DbUtil.getConnection();
	}

	public void saveToken(Token token) {

		try {
			String query = "insert into token(orderid,orderlineid, status) values (?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, token.getOrderId());
			preparedStatement.setInt(2, token.getOrderLineId());
			preparedStatement.setString(3, token.getStatus());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateToken(Token token) {
		try {
			String query = "update token set orderid= ? , status = ? where tokenid = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, token.getOrderId());
			preparedStatement.setString(2, token.getStatus());
			preparedStatement.setInt(3, token.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getMaxTokenNum() {
		int max = 0;
		try {

			String query = "select max(tokenid) as count from token group by tokenid";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				max = rs.getInt("count");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max;
	}
	
	
	public List<Token> getAllTokensForOrder(String orderId) {

		List<Token> tokenList = new ArrayList<Token>();
		try {
			
			String query = "select tokenid, orderid, orderlineid, status from token where orderid = '"+orderId+"'";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Token token = new Token();
				token.setId(rs.getInt("tokenid"));
				token.setOrderId(rs.getString("orderid"));
				token.setOrderLineId(rs.getInt("orderlineid"));
				token.setStatus(rs.getString("status"));
				tokenList.add(token);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tokenList;
	}
	
	public List<Token> getRecieptNTokenDataForOrder(String orderId) {

		List<Token> tokenList = new ArrayList<Token>();
		try {
			
			String query = "select tokenid, orderid, orderlineid, status from token where orderid = '"+orderId+"'";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Token token = new Token();
				token.setId(rs.getInt("tokenid"));
				token.setOrderId(rs.getString("orderid"));
				token.setOrderLineId(rs.getInt("orderlineid"));
				token.setStatus(rs.getString("status"));
				tokenList.add(token);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tokenList;
	}
	
	

}
