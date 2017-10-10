import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class MLB_RANK {
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_rank?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		PreparedStatement pstmt = null;

		String table[] = { "al_east", "al_central", "al_west", "nl_east",
				"nl_central", "nl_west" };
		int count = 0;
		int k = 0;
		int o = 0;
		int t = 0;
		String team[] = CrawlerTeamRank().split("&");
	String rec[] = CrawlerRec().split("&");
//		for (int i = 0; i < rec.length; i++)
//			System.out.print(rec[i] + " ");
		//store(table[count], team, rec, conn, stmt, pstmt);
		
		System.out.println(rec.length/55);
		
	
	}

	public static String CrawlerTeamRank() throws Exception {
		URL url = new URL("http://sports.yahoo.com/mlb/standings/");
		Document xmlDoc = Jsoup.parse(url, 3000);
		Elements rank = xmlDoc.select("[summary] tr [href]");
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

	public static String CrawlerRec() throws Exception {
		URL url = new URL("http://sports.yahoo.com/mlb/standings/");
		Document xmlDoc = Jsoup.parse(url, 3000);
		Elements rank = xmlDoc.select("[summary] tr td");
		StringBuilder sb = new StringBuilder();
		int i = 0;
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

	public static void store(String table, String team[], String rec[],
			 Connection conn, Statement stmt,
			PreparedStatement pstmt) throws SQLException {

		int k = 0;
		int o = 0;
		while (k < rec.length) {
			pstmt = (PreparedStatement) conn.prepareStatement("insert into "
					+ table + " values(?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 1; i < 13; i++) {
				if (i == 1) {
					pstmt.setString(1, team[o]);
					o++;
				} else {
					pstmt.setString(i, rec[k]);
					k++;
				}
			}
			pstmt.executeUpdate();
		}	
	}
	public static void CreateTable(String string)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_rank?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();

		String sql = "CREATE TABLE " + string + " (`TEAM` VARCHAR(255), "
				+ "`W` VARCHAR(255)," + "`L` VARCHAR(255),"
				+ "`PCT` VARCHAR(255)," + "`GB` VARCHAR(255), "
				+ "`HOME` VARCHAR(255) , " + "`AWAY` VARCHAR(255) , "
				+ "`STREAK` VARCHAR(255) , " + "`RS` VARCHAR(255) , "
				+ "`RA` VARCHAR(255) , " + "`DIFF` VARCHAR(255), "
				+ "`L10` VARCHAR(255))";
		stmt.execute(sql);
		//TEAM	W	L	PCT	GB	HOME	AWAY	STREAK	RS	RA	DIFF	L10
	}
}
