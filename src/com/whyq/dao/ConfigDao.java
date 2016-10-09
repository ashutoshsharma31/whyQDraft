package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import com.whyq.model.Cafe;
import com.whyq.model.Config;
import com.whyq.util.DbUtil;

public class ConfigDao {
	private Connection connection;

	public ConfigDao() {
		connection = DbUtil.getConnection();
	}



	public Config getConfigDetails(String configname, String value) {
		Config config = new Config();
		PreparedStatement preparedStatement = null;
		try {
			
			String query = "SELECT id, configname, value, status FROM config where configname=? and value=?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, configname);
			preparedStatement.setString(2, value);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				config.setId(rs.getInt("id"));
				config.setConfigname(rs.getString("configname"));
				config.setValue(rs.getString("value"));
				config.setStatus(rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return config;
	}
	
	public boolean isConfigPresent(String configname, String value) {
		boolean flag = false;
		PreparedStatement preparedStatement = null;
		try {
			
			String query = "SELECT id, configname, value, status FROM config where configname=? and value=? and status = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, configname);
			preparedStatement.setString(2, value);
			preparedStatement.setString(3, "A");
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()) {
				 flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

}
