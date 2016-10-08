package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import com.whyq.model.Cafe;

import com.whyq.util.DbUtil;

public class CafeDao {
	private Connection connection;

	public CafeDao() {
		connection = DbUtil.getConnection();
	}

	public ArrayList<String> getAllCafeList() {
		ArrayList<String> cafe = new ArrayList<String>();
		try {
			String query = "select  cafeid, cafename, cafeaddress from cafe";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				cafe.add(rs.getString("cafename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Cafe " + cafe);
		return cafe;
	}

	public Cafe getCafeDetails(String cafename) {
		Cafe cafe = new Cafe();
		try {
			String query = "select  cafeid, cafename, cafeaddress, code from cafe where cafename=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, cafename);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				cafe.setName(rs.getString("cafename"));
				cafe.setCafeid(rs.getInt("cafeid"));
				cafe.setAddress(rs.getString("cafeaddress"));
				cafe.setCode(rs.getString("cafeaddress"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cafe;
	}

	public String getCafeCode(int cafeid) {
		String code = "";
		try {
			String query = "select  code from cafe where cafeid=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, cafeid);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				code = rs.getString("code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return code;
	}

}
