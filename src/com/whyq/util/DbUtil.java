package com.whyq.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {
	private static Connection connection = null;
	public static Connection getConnection() {
		if (connection != null)
			return connection;
		else {
			try {
				Properties prop = new Properties();
				
				InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream("./db.properties");
				//prop.load(inputStream);
				//String driver = prop.getProperty("driver");
				//String url = prop.getProperty("url");
				//String user = prop.getProperty("user");
				//String password = prop.getProperty("password");
				//System.out.println("DB Details :"+driver+" "+url+" "+user+" "+password);
				//Class.forName(driver);
				//connection = DriverManager.getConnection(url, user, password);
				System.out.println("IN CONNECTION START");
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://redvelvetcluster.cluster-czvyaawmcxuy.ap-south-1.rds.amazonaws.com:3306/botdb","awsadmin","awsadmin");
				System.out.println("IN CONNECTION END");
			} catch (ClassNotFoundException e) {
				System.out.println("-------------- DB ERROR ----------- 1 "+e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("-------------- DB ERROR ----------- 2 "+e.getMessage());
				e.printStackTrace();
			}
			return connection;
		}
	}
}