package WC;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class mlbgameday {
	public static void main(String args[]) throws Exception {
		// System.out.println(CrawlerAwayTeam());
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_gameday?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "joey820924";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		PreparedStatement pstmt = null;
		String a[]=CrawlerAwayTeam().split("&");
		System.out.println(a.length);
		//CreateTable("B");
		store("B",CrawlerAwayTeam().split("&"),CrawlerHomeTeam().split("&"),
			conn,stmt,  pstmt);
	}

	public static String CrawlerAwayTeam() throws Exception {
		URL url = new URL("http://www.cbssports.com/mlb/scoreboard/20150723");
		Document xmlDoc = Jsoup.parse(url, 3000);
		Elements rank = xmlDoc.select("tr.teamInfo.awayTeam td");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		try {
			while (i != -1) {
				sb.append(rank.get(i).text() + "&");
				i++;
			}
		} catch (Exception e) {
			i = -1;
		}
		return sb.toString();
	}

	public static String CrawlerHomeTeam() throws Exception {
		URL url = new URL("http://www.cbssports.com/mlb/scoreboard/20150723");
		Document xmlDoc = Jsoup.parse(url, 3000);
		Elements rank = xmlDoc.select("tr.teamInfo.homeTeam td");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		try {
			while (i != -1) {
				sb.append(rank.get(i).text() + "&");
				i++;
			}
		} catch (Exception e) {
			i = -1;
		}
		return sb.toString();
	}

	public static void CreateTable(String table) throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_gameday?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "joey820924";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();

		String sql = "CREATE TABLE " + table + " (`AWAY` VARCHAR(255), "+ "`HOME` VARCHAR(255)," 
				+ "`1` VARCHAR(255)," + "`2` VARCHAR(255),"
				+ "`3` VARCHAR(255), " + "`4` VARCHAR(255) , "
				+ "`5` VARCHAR(255) , " + "`6` VARCHAR(255) , "
				+ "`7` VARCHAR(255) , " + "`8` VARCHAR(255) , "
				+ "`9` VARCHAR(255), " + "`R` VARCHAR(255), "
				+ "`H` VARCHAR(255), " + "`E` VARCHAR(255))";
		stmt.execute(sql);
	}

	public static void store(String name, String away[], String home[],
			Connection conn, Statement stmt, PreparedStatement pstmt)
			throws SQLException {
		int count = 0;
		int j = -1;
		int k = -1;
		while (count < 23) {
			if (count % 2 == 0) {
				pstmt = (PreparedStatement) conn
						.prepareStatement("insert into " + name
								+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				count++;
				for (int i = 0; i < 14; i++) {
					if (i == 1) {
						pstmt.setString(2, null);
						
					} else {
						j++;
						pstmt.setString(i+1, away[j]);
						
					}
				}
				
				pstmt.executeUpdate();
			} else {
				pstmt = (PreparedStatement) conn
						.prepareStatement("insert into " + name
								+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				for (int i = 0; i < 14; i++) {
					if (i == 0) {
						pstmt.setString(1, null);
						count++;
					}
					else {
						k++;
						pstmt.setString(i+1, home[k]);
						
					}
				}
				count++;	
			}
		
		pstmt.executeUpdate();// 更新資料到資料庫裡面
	}
	}
}