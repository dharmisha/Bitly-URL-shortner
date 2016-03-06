package com.mkyong.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseConnection {
	public static String selectData(String shortUrlHash) {
		DateFormat timeStampvalue = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		String timeStamp = timeStampvalue.format(calobj.getTime());
		String longURL = "";

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			// conn =
			// DriverManager.getConnection("com.mysql.jdbc.Driver://cmpe281-team1-mysql.ckeca33m2obn.us-east-1.rds.amazonaws.com:3306/CMPE281_NANO?user=team1&password=cmpe281-team1-mysql");
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(
					"jdbc:mysql://cmpe281-team1-mysql.ckeca33m2obn.us-east-1.rds.amazonaws.com:3306/CMPE281_NANO",
					"team1", "cmpe281-team1-mysql");
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement("select nanourl from URLDATA where hashcode=?;");
			preparedStatement.setString(1, shortUrlHash);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				longURL = rs.getString("nanourl");
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.commit();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return longURL;
	}
}
