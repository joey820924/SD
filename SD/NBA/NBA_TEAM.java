package NBA;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class NBA_TEAM {
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://163.13.201.32/cdcol?useunicode=true&characterencoding=utf 8";
		String user = "test";
		String password = "1234";
		URL url = new URL("http://www.nba.com/gameline/20131226/");
		Document doc = Jsoup.parse(url, 30000);
		Elements stat = doc.select("div.nbaMnQuScores th");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		String data;

		try {
			PreparedStatement pstmt = null;
			while (i != -1) {
				System.out.println(stat.get(i).text());
				if (i % 12 == 10 || i % 12 == 11)
					sb.append(stat.get(i).text() + "&");
				i++;
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
			sb.toString().split("&");
			for(int i1 = 0 ; i1 < sb.toString().split("&").length ;i1++){
				
					System.out.println();
				System.out.print(sb.toString().split("&")[i1]+" ");
				
			}
			//store("20141225POINT", sb.toString().split("&"), conn, stmt, pstmt);
			conn.close();
			// pstmt.close();
		}
		

	}

	public static void insertIntoDB(String[] data, Connection conn,
			PreparedStatement pstmt) throws SQLException {

		int j = 0;
		while (j < data.length) {
			pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO "
					+ "Playoffs_Advanced_Stats"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
		String sql = "CREATE TABLE " + name + " (`Away` VARCHAR(255), "
				+ "`Home` VARCHAR(255), " + "`Q1` VARCHAR(255) , "
				+ "`Q2` VARCHAR(255) , " + "`Q3` VARCHAR(255) , "
				+ "`Q4` VARCHAR(255) , " + "`OT1` VARCHAR(255) , "
				+ "`OT2` VARCHAR(255) , " + "`OT3` VARCHAR(255) , "
				+ "`OT4` VARCHAR(255) , " + "`F` VARCHAR(255)) "
				+ "ENGINE = MyISAM DEFAULT CHARSET = utf8 COLLATE = utf8_bin";

		// ///////建立UTF8編碼資料表

		stmt.executeUpdate(sql);
		int i = 0;
		int j = 0;
		/*
		 * while (j < data.length) { pstmt = (PreparedStatement)
		 * conn.prepareStatement("INSERT INTO " + name +
		 * " VALUES(?,?,?,?,?,?,?,?,?,?,?)"); for (int i1 = 1; i1 < 12; i1++) {
		 * if (i1 == 1) { pstmt.setString(i1, data[j]); j++; } else if (i1 == 2)
		 * { pstmt.setString(i1, data[j]); j++; } else pstmt.setString(i1,
		 * null); }
		 * 
		 * }
		 */
		int p = 0;
		while (p < data.length) {
			pstmt = conn.prepareStatement("INSERT INTO " + name
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			for (int i1 = 1; i1 < 12; i1++) {
				if (i1 == 1 && p%2==0) {
					pstmt.setString(i1, data[p]);
					
				} else if (i1 == 2 && p%2==1) {
					pstmt.setString(i1, data[p]);
					
				} else
					pstmt.setString(i1, null);
			}
			p++;
			pstmt.executeUpdate();
		}
		
		/*
		
      String sql = "UPDATE Registration " +
                   "SET age = 30 WHERE id in (100, 101)";
      stmt.executeUpdate(sql);
		*/
	}
}

