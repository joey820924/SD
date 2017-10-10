package 測試專用;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class MLB_PLAYER_STATS_ONETABLE {
	public static void main(String args[]) throws SQLException,
			ClassNotFoundException, IOException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_player?useunicode=true&characterencoding=utf 8";
		// 本機
		// String jdbcurl =
		// "jdbc:mysql://163.13.201.32/test?useunicode=true&characterencoding=utf 8";
		// 遠端
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from mlb_player_link");
		String link[] = new String[30];
		CreateTable("MLB_PLAYER");
		int k = 0;
		while (rs.next()) {
			link[k] = rs.getString(1);
			k++;
		}

		for (int i = 0; i < link.length; i++) {

			System.out.println(CrawlerTeamName(link[i]));
			System.out.println(CrawlerName(link[i]));
			System.out.print(CrawlerStats(link[i]));

			PreparedStatement pstmt = null;
			store("MLB_PLAYER", CrawlerName(link[i]).split("&"),
					CrawlerTeamName(link[i]).split("&"), CrawlerStats(link[i])
							.split("&"), conn, stmt, pstmt);
		}
	}

	public static String CrawlerName(String target) throws IOException {
		URL url = new URL(target);
		Document doc = Jsoup.parse(url, 30000);
		Elements player = doc.select("div.data-container th");
		StringBuilder sb = new StringBuilder();
		int read = 0;
		try {
			while (read != -1) {
				if (read < 17)
					read++;

				else {
					sb.append(player.get(read).text() + "&");
					read++;
				}
			}

		} catch (Exception e) {
			read = -1;
			System.out
					.println("------------------------------------------------------------------------");
		}
		return sb.toString();

	}

	public static String CrawlerTeamName(String link) throws IOException {
		URL url = new URL(link);
		Document doc = Jsoup.parse(url, 30000);
		Elements team = doc.select("div.data-container th");
		StringBuilder sb = new StringBuilder();
		
		int read = 0;
		try {
			String after = link.replace("/", "").replace(":", "").replace(".", "")
					.replace("httpsportsyahoocommlbteams", "").replace("stats", "").replace(" ","");
				while (read != -1) {
					if (read < 17)
						read++;
					else {
						team.get(read);
						sb.append(after+"&");
						read++;
					}
			}
		} catch (Exception e) {
			read = -1;
			System.out
					.println("------------------------------------------------------------------------");
		}
		return sb.toString();


		 
	}

	public static String CrawlerStats(String target) throws IOException {
		URL url = new URL(target);
		Document doc = Jsoup.parse(url, 30000);
		Elements player = doc.select("div.data-container td");
		StringBuilder sb = new StringBuilder();
		int read = 0;
		String tmp;
		try {
			while (read != -1) {
				tmp=player.get(read).text().replace(" ","");
				sb.append(tmp+"&");
				read++;
			}
		} catch (Exception e) {
			read = -1;
			System.out
					.println("------------------------------------------------------------------------");
		}
		return sb.toString();
	}

	public static void store(String name, String Pname[], String Team[],
			String Pstats[], Connection conn, Statement stmt,
			PreparedStatement pstmt) throws SQLException {
		int j = 0, k = 0, o = 0;

		for (int n = 0; n < Pname.length; n++) { 
			pstmt = (PreparedStatement) conn.prepareStatement("insert into "
					+ name + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
			for (int i = 1; i < 19; i++) {
				if (i == 1) { // 第一筆資料跟第二筆資料存在name.txt所以用if , else if拉出
					pstmt.setString(1, Pname[j]);// pstmt.setString(資料位置,資料內容)
					j++;
				}
				else if (i == 2) {
					pstmt.setString(2, Team[o]);
					o++;
				} 
				else {
					pstmt.setString(i, Pstats[k]);
					k++;

				}

			}
			pstmt.executeUpdate();// 更新資料到資料庫裡面
		}
	}

	public static void CreateTable(String name) throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_player?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();

		String teamname = name.replace("/", "").replace(":", "")
				.replace(".", "").replace("httpsportsyahoocommlbteams", "")
				.replace("stats", "");

		String sql = "CREATE TABLE " + name + " (NAME VARCHAR(255),"
				+ "`TEAM` VARCHAR(255)," + "`G` VARCHAR(255),"
				+ "`AB` VARCHAR(255)," + "`R` VARCHAR(255),"
				+ "`H` VARCHAR(255)," + "`2B` VARCHAR(255),"
				+ "`3B` VARCHAR(255)," + "`HR` VARCHAR(255),"
				+ "`RBI` VARCHAR(255)," + "`BB` VARCHAR(255),"
				+ "`K` VARCHAR(255)," + "`SB` VARCHAR(255),"
				+ "`CS` VARCHAR(255)," + "`AVG` VARCHAR(255),"
				+ "`OBP` VARCHAR(255)," + "`SLG` VARCHAR(255),"
				+ "`OPS` VARCHAR(255))";
		stmt.executeUpdate(sql);
	}



}
