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

public class NBA_REFEREE {
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://163.13.201.32/cdcol?useunicode=true&characterencoding=utf 8";
		String user = "test";
		String password = "1234";
		URL url = new URL(
				"http://www.nba.com/gameline/20141225/");
		Document doc = Jsoup.parse(url, 30000);
		Elements stat = doc.select("#Referee td");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		String data;
		
		try {

			
				PreparedStatement pstmt = null;
				while (i != -1) {
					System.out.println(stat.get(i).text());
					//System.out.println(stat.get(i).text());
						if(i%12!=0){
						sb.append(stat.get(i).text()+"&");	
						}
						i += 1;
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
			
			store("2012_13_Regular_Referee" ,sb.toString().split("&") , conn ,stmt, pstmt );
			conn.close();
			//pstmt.close();
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
		String sql = "CREATE TABLE " + name + " (`Referee` VARCHAR(255), " + "`Type` VARCHAR(255), " 
				+ "`Game_Officiated` VARCHAR(255), " + "`Home_Team_Winn` VARCHAR(255), "
				+ "`Home_Team_Pts_Diff` VARCHAR(255), " + "`Tot_Points_Per_Game` VARCHAR(255), "
				+ "`Called_Fouls_Per_Game` VARCHAR(255), " + "`Road_Team_Foul_Pct` VARCHAR(255), "
				+ "`Home_Team_Foul_Pct` VARCHAR(255), " + "`Road_Tech_Per_Game` VARCHAR(255), "
				+ "`Home_Tech_Per_Game` VARCHAR(255)) "
				+ "ENGINE = MyISAM DEFAULT CHARSET = utf8 COLLATE = utf8_bin";

		// ///////建立UTF8編碼資料表

		stmt.executeUpdate(sql);
		int j = 0;
		while (j < data.length) {
			pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO "
					+ name + " VALUES(?,?,?,?,?,?,?,?,?,?,?)");

			for (int i = 0; i < 11; i++) {
				pstmt.setString(i + 1, data[j]);
				j++;
			}
			pstmt.executeUpdate();
		}

	}
}
