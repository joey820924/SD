package 測試專用2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class MLB_TEAM_SCHEDULE_ID {
	public static void main(String args[]) throws IOException,
			ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/testsch?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		PreparedStatement pstmt = null;
		String link[] = new String[30];
		ResultSet rs = stmt.executeQuery("select * from link");
		int k = 0;
		while (rs.next()) {
			link[k] = rs.getString(1);
			k++;
		}

		for (int i = 0; i < link.length; i++) {
			drop(link[i], stmt);
		}

		CrawlerSch(link[1]);
		 System.out.print(CrawlerSch(link[0]));
//		 for (int i = 0; i < link.length; i++) {
//		 System.out.println(link[i]);
//		 CreateTable(link[i]);
//		 store(filter(link[i]), CrawlerSch(link[i]).replace("POSTPONED", "")
//		 .split("&"), conn, stmt, pstmt);
//		 }
		 int i=31;
		CreateTable(link[i]);
		 store(filter(link[i]), CrawlerSch(link[i]).replace("POSTPONED", "")
				 .split("&"), conn, stmt, pstmt);
	}

	public static String CrawlerSch(String link) throws IOException {
		URL url = new URL(link);
		Document doc = Jsoup.parse(url, 30000);
		Elements team = doc.select("tr[class~=(?i)row(?i)] td");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		try {
			while (i != -1) {
				if (team.get(i).text().equals("POSTPONED")) {
					for (int k = 0; k < 6; k++) {
						sb.append("&");
					}
					i++;
				}			
					sb.append(team.get(i).text() + "&");		
				i++;
			}

		} catch (Exception e) {
			System.out.println("-----------");
			i=i;
		}
		return sb.toString();
	}

	public static void drop(String name, Statement stmt) throws SQLException {
		try {
			String sql = "DROP TABLE " + ("mlb" + name);
			stmt.executeUpdate(sql);
		} catch (Exception e) {
		}
	}

	public static String getNow() {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, -1);
		Date newDate = calendar.getTime();
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMdd");
		String strDate = sdfDate.format(newDate);
		return strDate;
	}

	public static void CreateTable(String table) throws ClassNotFoundException,
			SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/testsch?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		String filter = table.replace("/", "").replace(":", "")
				.replace(".", "").replace("httpespngocommlbteamschedule", "")
				.replace("_", "").replace("name", "");
		// Month Oppenent Result WL Win Loss Save Att
		String sql4 = "CREATE TABLE " + filter + " (`ID` INT, "
				+ "`DATE1` VARCHAR(255)," + "`DATE2` VARCHAR(255), "+ "`OPPENENT` VARCHAR(255), "
				+ "`RESULT` VARCHAR(255) , " + "`WL` VARCHAR(255) , "
				+ "`WIN` VARCHAR(255) , " + "`LOSS` VARCHAR(255) , "
				+ "`SAVE` VARCHAR(255) , " + "`ATT` VARCHAR(255)) "
				+ "ENGINE = MyISAM DEFAULT CHARSET = utf8 COLLATE = utf8_bin";
		stmt.execute(sql4);
	}

	public static String filter(String name) {
		String filter = name.replace("/", "").replace(":", "").replace(".", "")
				.replace("httpespngocommlbteamschedule", "").replace("_", "")
				.replace("name", "");
		return filter;
	}

	public static void store(String name, String Date[], Connection conn,
			Statement stmt, PreparedStatement pstmt) throws SQLException {
		int j = 0, k = 1;

		while (j < Date.length) {
			pstmt = (PreparedStatement) conn.prepareStatement("insert into "
					+ name + " values(?,?,?,?,?,?,?,?,?,?)");

			for (int i = 1; i < 11; i++) {
				if (i == 1) {
					pstmt.setInt(1, k);
					k++;
				} 
				else if (i==2) {
					pstmt.setString(2,toDate(Date[j]));
				}
				else {
					pstmt.setString(i, Date[j]);
					j++;
				}
			}
			pstmt.executeUpdate();// 更新資料到資料庫裡面
		}
	}

	public static String toDate(String b) {
		String month = "00";
		String date = "00";
		String q = "";
		String tmp = b.replace("Mon,", "").replace("Tue,", "")
				.replace("Wed,", "").replace("Thu,", "").replace("Fri,", "")
				.replace("Sat,", "").replace("Sun,", "");
		String a[] = tmp.split(" ");

		if (a[1].equals("Jan")) {
			a[1] = "01";
		}
		if (a[1].equals("Feb")) {
			a[1] = "02";
		}
		if (a[1].equals("Mar")) {
			a[1] = "03";
		}
		if (a[1].equals("Apr")) {
			a[1] = "04";
		}
		if (a[1].equals("May")) {
			a[1] = "05";
		}
		if (a[1].equals("Jun")) {
			a[1] = "06";
		}
		if (a[1].equals("Jul")) {
			a[1] = "07";
		}
		if (a[1].equals("Aug")) {
			a[1] = "08";
		}
		if (a[1].equals("Sep")) {
			a[1] = "09";
		}
		if (a[1].equals("Oct")) {
			a[1] = "10";
		}
		if (a[2].equals("1")) {
			a[2] = "01";
		}
		if (a[2].equals("2")) {
			a[2] = "02";
		}
		if (a[2].equals("3")) {
			a[2] = "03";
		}
		if (a[2].equals("4")) {
			a[2] = "04";
		}
		if (a[2].equals("5")) {
			a[2] = "05";
		}
		if (a[2].equals("6")) {
			a[2] = "06";
		}
		if (a[2].equals("7")) {
			a[2] = "07";
		}
		if (a[2].equals("8")) {
			a[2] = "08";
		}
		if (a[2].equals("9")) {
			a[2] = "09";
		}
		String date1 = "2015"+a[1] + a[2];
		return date1;
	}

}
