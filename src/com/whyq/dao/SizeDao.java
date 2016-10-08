package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.whyq.model.MenuItem;
import com.whyq.model.Size;
import com.whyq.model.User;
import com.whyq.util.DbUtil;

public class SizeDao {
	private Connection connection;

	public SizeDao() {
		connection = DbUtil.getConnection();
	}

	public ArrayList<String> getAllSizesForItem(MenuItem menuItem) {
		ArrayList<String> sizes = new ArrayList<String>();
		try {
			String query = "select B.desc from itemsize A , size B where A.sizeid = B.sizeid and B.status = 'A' and itemid = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, menuItem.getItemid());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				sizes.add(rs.getString("desc"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sizes;
	}
	
	public ArrayList<Size> getAllSizes(MenuItem menuItem) {
		ArrayList<Size> sizes = new ArrayList<Size>();
		try {
			String query = "select B.sizeid, B.status, B.desc from itemsize A , size B where A.sizeid = B.sizeid and B.status = 'A' and itemid = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, menuItem.getItemid());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Size size = new Size();
				size.setDesc(rs.getString("desc"));
				size.setSizeid(rs.getInt("sizeid"));
				size.setStatus(rs.getString("status"));
				sizes.add(size);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sizes;
	}
}
