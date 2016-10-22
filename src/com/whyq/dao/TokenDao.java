package com.whyq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whyq.controller.WorkServlet;
import com.whyq.model.CartItem;
import com.whyq.model.OrderInformation;
import com.whyq.model.Token;
import com.whyq.util.DbUtil;
import com.whyq.util.ResultSetConverterUtil;

public class TokenDao {
	static Logger log = Logger.getLogger(TokenDao.class.getName());

	private Connection connection;

	public TokenDao() {
		connection = DbUtil.getConnection();
	}

	public void saveToken(Token token) {
		log.info("Method saveToken Start "+token);
		int records = 0;
		try {
			String query = "insert into token(orderid,orderlineid, status) values (?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, token.getOrderId());
			preparedStatement.setInt(2, token.getOrderLineId());
			preparedStatement.setString(3, token.getStatus());
			records = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		log.info("Method saveToken End "+records +" inserted in token");
	}

	public void updateToken(Token token) {
		log.info("Method updateToken Start "+token);
		int records = 0;
		try {
			String query = "update token set orderid= ? , status = ? where tokenid = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, token.getOrderId());
			preparedStatement.setString(2, token.getStatus());
			preparedStatement.setInt(3, token.getId());
			records  = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		log.info("Method updateToken End "+records +" updated in token");
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
		log.info("Method getMaxTokenNum Max Token Number returned is "+max);
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
		log.info("Method getRecieptNTokenDataForOrder Start "+orderId);
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
	
	public void createTokens(OrderInformation orderInformation) {
		log.info("Method createTokens Start "+orderInformation);
		OrderDao orderDao = new OrderDao();
		List<CartItem> cartList = orderInformation.getOrderList();
		for (CartItem cartItem : cartList) {
			TokenDao tokenDao = new TokenDao();
			int maxToken = tokenDao.getMaxTokenNum() + 1;
			Token token = new Token();
			token.setOrderId(orderInformation.getOrderNum());
			token.setOrderLineId(orderDao.getOrderLineId(orderInformation.getOrderNum(),cartItem));
			token.setStatus("PENDING");
			tokenDao.saveToken(token);
			log.info("TOKEN " + token.toString());
		}
		log.info("Method createTokens END ");
	}
	
	
	public JSONArray getAllOpenTokensForCafe(int cafeid) {

		ResultSet rs = null;
		Statement stmt =null;
		JSONArray tokenJsonArray = new JSONArray();
		try {
			
			String query = " SELECT tokenid,                  "+
					"        mi.name,                  "+
					"        ol.orderid,               "+
					"        ol.quantity,              "+
					"        od.username,              "+
					"        t.status                    "+
					" FROM token t,                    "+
					"      orderline ol,               "+
					"      menuitem mi,                "+
					"      orderdata od                "+
					" WHERE t.orderlineid = ol.orderlineid "+
					"   AND mi.itemid = ol.itemid      "+
					"   AND od.ordernum=ol.orderid     "+
					"   AND t.status <> 'COMPLETE'     "+
					"   AND od.cafeid="+cafeid+"";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			
			try {
				tokenJsonArray = ResultSetConverterUtil.convert(rs);
			} catch (JSONException e) { 
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tokenJsonArray;
	}
	
	public void updateTokenToComplete(int tokenid) {
		log.info("Method updateToken Start "+tokenid);
		int records = 0;
		try {
			String query = "update token set status = ? where tokenid = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "COMPLETE");
			preparedStatement.setInt(2, tokenid);
			records  = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		log.info("Method updateToken End "+records +" updated in token");
	}	
	
	public JSONArray getCountPendingTokens(int cafeid) {
		ResultSet rs = null;
		PreparedStatement preparedStatement =null;
		JSONArray pendingTokensJsonArray = new JSONArray();
		
		try {
			String query = 
					" SELECT mi.name, count(t.tokenid) as pendingtokens      "+
					" FROM token t,                                                      "+
					"      orderline ol,                                                 "+
					"      menuitem mi,                                                  "+
					"      orderdata od                                                  "+
					" WHERE t.tokenid = ol.orderlineid                                   "+
					"   AND mi.itemid = ol.itemid                                        "+
					"   AND od.ordernum = ol.orderid                                     "+
					"   AND od.cafeid = "+cafeid+"  AND t.status <> 'COMPLETE' group by name   ";
			preparedStatement = connection.prepareStatement(query);
			
			
			rs = preparedStatement.executeQuery(query);
			try {
				//System.out.println(rs.next());
				//pendingTokensJsonArray = ResultSetConverterUtil.convert(rs);
				 while(rs.next()) {
					 JSONObject jsonObject =  new JSONObject();
					 jsonObject.put("name",rs.getString("name"));
					 jsonObject.put("pendingtokens",rs.getString("pendingtokens"));
					 pendingTokensJsonArray.put(jsonObject);
				 }
				
			} catch (JSONException e) { 
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(preparedStatement!=null){
					preparedStatement.close();
				}
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pendingTokensJsonArray;
	}	

}
