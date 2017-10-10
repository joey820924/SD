package NBA;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.*;

public class NBA_TEAM_PLAYOFFS {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://163.13.201.32/cdcol?useunicode=true&characterencoding=utf 8";
		String user = "test";
		String password = "1234";
		URL url = new URL(
				"http://www.nbastuffer.com/2014_NBA_Playoffs_Advanced_Stats.html");
		Document doc = Jsoup.parse(url, 30000);
		Elements stat = doc.select("#PO-14 tr td");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		String data;
		
		try {

			{
				PreparedStatement pstmt = null;
				while (i != -1) {
					
					System.out.println(stat.get(i).text());
					if(i%20!=0 && i %20!=3)
						sb.append(stat.get(i).text()+"&");
					i += 1;
				}

			}
		} catch (Exception e) {
			i = -1;
		}

		try (Connection conn = DriverManager.getConnection(jdbcurl, user,
				password)) {
			System.out.printf("資料庫已%s %n", conn.isClosed() ? "關閉" : "開-起");
			Statement stmt = conn.createStatement();
			
			System.out.print(sb.toString());
			PreparedStatement pstmt = null;
			
			store("2014_Playoffs" ,sb.toString().split("&") , conn ,stmt, pstmt );
			conn.close();
			pstmt.close();
		}

	}

	public static void insertIntoDB(String[] data, Connection conn,
			PreparedStatement pstmt) throws SQLException {

		int j = 0;
		while (j < data.length) {
			pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO "
					+ "Playoffs_Advanced_Stats" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			for (int i = 0; i < 17; i++) {
				pstmt.setString(i + 1, data[j]);
				j++;
			}
			pstmt.executeUpdate();

		}

		System.out.println("done");
	}
	public static void store(String name, String data[], Connection conn,
			Statement stmt, PreparedStatement pstmt) throws SQLException {
		String sql = "CREATE TABLE " + name + " (Team VARCHAR(255) not NULL, " + "Conference VARCHAR(255), " 
				+ "GP VARCHAR(255), " + "PPG VARCHAR(255), "
				+ "OPPG VARCHAR(255), " + "PDIFF VARCHAR(255), "
				+ "PACE VARCHAR(255), " + "OEFF VARCHAR(255), "
				+ "DEFF VARCHAR(255), " + "EDIFF VARCHAR(255), "
				+ "CONS VARCHAR(255), " + "A4F VARCHAR(255), "
				+ "WINS VARCHAR(255), " + "LOSS VARCHAR(255),"
				+ "WINN VARCHAR(255), " + "pWINN VARCHAR(255), "
				+ "eWINN VARCHAR(255)," + "ACH VARCHAR(255) , "+ " PRIMARY KEY (Team))"
				+ "ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin";

		// ///////建立UTF8編碼資料表

		stmt.executeUpdate(sql);
		int j = 0;
		while (j < data.length) {
			pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO "
					+ name + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			for (int i = 0; i < 18; i++) {
				pstmt.setString(i + 1, data[j]);
				j++;
			}
			pstmt.executeUpdate();
		}

	}

}
