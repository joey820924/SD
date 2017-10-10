import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class mlb_gameday_2015 {
	public static void main(String args[]) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_gameday_2015?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		PreparedStatement pstmt = null;

		String table[] = CreateDate("2015")
				.replace("http://www.cbssports.com/mlb/scoreboard/", "")
				.toString().split("@");
		String link[] = CreateDate("2015").toString().split("@");

	//	for (int j = 193; j < link.length; j++) {
//			drop(table[j], stmt);
//			System.out.println(link[j]);
//			System.out.println(CrawlerAwayTeam(link[j]));
//			System.out.println(CrawlerHomeTeam(link[j]));
//			System.out.println(CrawlerAwayScore(link[j]).length());
//			System.out.println(CrawlerHomeScore(link[j]).length());
//			CreateTable("mlb" + table[j]);
//			store("mlb" + table[j], CrawlerAwayTeam(link[j]).split("&"),
//					CrawlerHomeTeam(link[j]).split("&"),
//					CrawlerAwayScore(link[j]).split("&"),
//					CrawlerHomeScore(link[j]).split("&"), conn, stmt, pstmt);
	//	}
		for (int i=84;i<97;i++) {
			drop(table[i], stmt);
			CreateTable2("mlb" + table[i]);
			store2("mlb" + table[i], CrawlerAwayTeam(link[i]).split("&"),
					CrawlerHomeTeam(link[i]).split("&"),
					CrawlerAwayScore(link[i]).split("&"),
					CrawlerHomeScore(link[i]).split("&"), conn, stmt, pstmt);
		}
	}

	public static void drop(String name, Statement stmt) throws SQLException {
		try {
			String sql = "DROP TABLE " + ("mlb" + name);
			stmt.executeUpdate(sql);
		} catch (Exception e) {

		}
	}

	public static String CreateDate(String a) {
		String month = "00";
		String date = "00";
		String url = "";
		int k;

		for (int j = 0; j < 12; j++) {
			int m = Integer.parseInt(month) + 1;
			if (m < 10)
				month = "0" + m;
			else
				month = "" + m;
			date = "00";
			for (int i = 0; i < 31; i++) {

				k = Integer.parseInt(date) + 1;
				if (k < 10)
					date = "0" + k;
				else
					date = "" + k;
				url += "http://www.cbssports.com/mlb/scoreboard/" + a + month
						+ date + "@";
			}

		}

		return url;

	}

	public static String CrawlerHomeTeam(String go) throws Exception {
		URL url = new URL(go);
		Document xmlDoc = Jsoup.parse(url, 30000);
		Elements link = xmlDoc
				.select("tr.teamInfo.homeTeam div.teamLocation a");
		Elements cancel = xmlDoc.select("tr.gameInfo");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		try {
			while (i != -1) {
				String tmp = link.get(i).attr("href").toString()
						.replace("/mlb/teams/page/", "").replace("/", "")
						.replace("new-york-yankees", "")
						.replace("pittsburgh-pirates", "")
						.replace("detroit-tigers", "")
						.replace("new-york-mets", "")
						.replace("los-angeles-angels", "")
						.replace("st-louis-cardinals", "")
						.replace("oakland-athletics", "")
						.replace("arizona-diamondbacks", "")
						.replace("cleveland-indians", "")
						.replace("san-diego-padres", "")
						.replace("houston-astros", "")
						.replace("kansas-city-royals", "")
						.replace("toronto-blue-jays", "")
						.replace("milwaukee-brewers", "")
						.replace("chicago-white-sox", "")
						.replace("baltimore-orioles", "")
						.replace("washington-nationals", "")
						.replace("seattle-mariners", "")
						.replace("los-angeles-dodgers", "")
						.replace("minnesota-twins", "")
						.replace("miami-marlins", "")
						.replace("boston-red-sox", "")
						.replace("chicago-cub", "")
						.replace("colorado-rockies", "")
						.replace("san-francisco-giants", "")
						.replace("tampa-bay-rays", "")
						.replace("philadelphia-phillies", "")
						.replace("atlanta-braves", "")
						.replace("cincinnati-reds", "")
						.replace("texas-rangers", "");
				if (!cancel.get(i).text().equals("Cancelled")&&!cancel.get(i).text().equals("Postponed")) {
					sb.append(tmp + "&");
				}
				i++;
			}

		} catch (Exception e) {
			i = -1;
		}
		return sb.toString().toLowerCase();
	}

	public static String CrawlerAwayTeam(String go) throws Exception {
		URL url = new URL(go);
		Document xmlDoc = Jsoup.parse(url, 30000);
		Elements link = xmlDoc
				.select("tr.teamInfo.awayTeam div.teamLocation a");
		Elements cancel = xmlDoc.select("tr.gameInfo");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		try {
			while (i != -1) {
				String tmp = link.get(i).attr("href").toString()
						.replace("/mlb/teams/page/", "").replace("/", "")
						.replace("new-york-yankees", "")
						.replace("pittsburgh-pirates", "")
						.replace("detroit-tigers", "")
						.replace("new-york-mets", "")
						.replace("los-angeles-angels", "")
						.replace("st-louis-cardinals", "")
						.replace("oakland-athletics", "")
						.replace("arizona-diamondbacks", "")
						.replace("cleveland-indians", "")
						.replace("san-diego-padres", "")
						.replace("houston-astros", "")
						.replace("kansas-city-royals", "")
						.replace("toronto-blue-jays", "")
						.replace("milwaukee-brewers", "")
						.replace("chicago-white-sox", "")
						.replace("baltimore-orioles", "")
						.replace("washington-nationals", "")
						.replace("seattle-mariners", "")
						.replace("los-angeles-dodgers", "")
						.replace("minnesota-twins", "")
						.replace("miami-marlins", "")
						.replace("boston-red-sox", "")
						.replace("chicago-cub", "")
						.replace("colorado-rockies", "")
						.replace("san-francisco-giants", "")
						.replace("tampa-bay-rays", "")
						.replace("philadelphia-phillies", "")
						.replace("atlanta-braves", "")
						.replace("cincinnati-reds", "")
						.replace("texas-rangers", "");
				if (!cancel.get(i).text().equals("Cancelled")&&!cancel.get(i).text().equals("Postponed")) {
					sb.append(tmp + "&");
				}
				i++;
			}
		} catch (Exception e) {
			i = -1;
		}
		return sb.toString().toLowerCase();
	}

	public static String CrawlerHomeScore(String go) throws Exception {
		URL url = new URL(go);
		Document xmlDoc = Jsoup.parse(url, 30000);
		Elements data = xmlDoc
				.select("tr.teamInfo.homeTeam td[class~=(?i)Score]");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		try {
			while (i != -1) {
				String tmp = data.get(i).text();
				sb.append(tmp + "&");
				i++;
			}
		} catch (Exception e) {
			i = -1;
		}
		return sb.toString();
	}

	public static String CrawlerAwayScore(String go) throws Exception {
		URL url = new URL(go);
		Document xmlDoc = Jsoup.parse(url, 30000);
		Elements data = xmlDoc
				.select("tr.teamInfo.awayTeam td[class~=(?i)Score]");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		try {
			while (i != -1) {
				String tmp = data.get(i).text();
				sb.append(tmp + "&");

				i++;
			}
		} catch (Exception e) {
			i = -1;
		}
		return sb.toString();
	}

	public static void CreateTable(String string)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_gameday_2015?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();

		String sql = "CREATE TABLE " + string + " (`AWAY` VARCHAR(255), "
				+ "`HOME` VARCHAR(255)," + "`1` VARCHAR(255),"
				+ "`2` VARCHAR(255)," + "`3` VARCHAR(255), "
				+ "`4` VARCHAR(255) , " + "`5` VARCHAR(255) , "
				+ "`6` VARCHAR(255) , " + "`7` VARCHAR(255) , "
				+ "`8` VARCHAR(255) , " + "`9` VARCHAR(255), "
				+ "`R` VARCHAR(255), " + "`H` VARCHAR(255), "
				+ "`E` VARCHAR(255))";
		stmt.execute(sql);
	}

	public static void CreateTable2(String string)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_gameday_2015?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();

		String sql = "CREATE TABLE " + string + " (`AWAY` VARCHAR(255), "
				+ "`HOME` VARCHAR(255)," + "`1` VARCHAR(255),"
				+ "`2` VARCHAR(255)," + "`3` VARCHAR(255), "
				+ "`4` VARCHAR(255) , " + "`5` VARCHAR(255) , "
				+ "`6` VARCHAR(255) , " + "`7` VARCHAR(255) , "
				+ "`8` VARCHAR(255) , " + "`9` VARCHAR(255), "
				+ "`R` VARCHAR(255))";
		stmt.execute(sql);
	}
	
	public static void store2(String name, String away[], String home[],
			String awaydata[], String homedata[], Connection conn,
			Statement stmt, PreparedStatement pstmt) throws SQLException {

		int count = 0;
		int j = 0;
		int k = 0;
		int y = 0;
		int z = 0;
		try {
			while (count < (away.length + home.length)) {
				if (count % 2 == 0) {
					pstmt = (PreparedStatement) conn
							.prepareStatement("insert into " + name
									+ " values(?,?,?,?,?,?,?,?,?,?,?,?)");
					for (int i = 1; i < 13; i++) {
						if (i == 1) {
							pstmt.setString(1, away[j]);
							j++;
						} else if (i == 2) {
							pstmt.setString(2, null);
						} else {
							pstmt.setString(i, awaydata[k]);
							k++;
						}
					}
				} else {
					pstmt = (PreparedStatement) conn
							.prepareStatement("insert into " + name
									+ " values(?,?,?,?,?,?,?,?,?,?,?,?)");
					for (int i = 1; i < 13; i++) {
						if (i == 1) {
							pstmt.setString(1, null);
						} else if (i == 2) {
							pstmt.setString(2, home[y]);
							y++;
						} else {
							pstmt.setString(i, homedata[z]);
							z++;
						}
					}
				}
				count++;
				pstmt.executeUpdate();
			}

		} catch (Exception e) {

		}
	}
	
	
	
	
	
	

	public static void store(String name, String away[], String home[],
			String awaydata[], String homedata[], Connection conn,
			Statement stmt, PreparedStatement pstmt) throws SQLException {

		int count = 0;
		int j = 0;
		int k = 0;
		int y = 0;
		int z = 0;
		try {
			while (count < (away.length + home.length)) {
				if (count % 2 == 0) {
					pstmt = (PreparedStatement) conn
							.prepareStatement("insert into " + name
									+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					for (int i = 1; i < 15; i++) {
						if (i == 1) {
							pstmt.setString(1, away[j]);
							j++;
						} else if (i == 2) {
							pstmt.setString(2, null);
						} else {
							pstmt.setString(i, awaydata[k]);
							k++;
						}
					}
				} else {
					pstmt = (PreparedStatement) conn
							.prepareStatement("insert into " + name
									+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					for (int i = 1; i < 15; i++) {
						if (i == 1) {
							pstmt.setString(1, null);
						} else if (i == 2) {
							pstmt.setString(2, home[y]);
							y++;
						} else {
							pstmt.setString(i, homedata[z]);
							z++;
						}
					}
				}
				count++;
				pstmt.executeUpdate();
			}

		} catch (Exception e) {

		}
	}
}
