
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NBAscore2014 {
	public static void main(String[] args) throws Exception {
				String a[]=null;
				String b[]=null;
				String year = "2014";
				String month = "00";
				String date = "00";
				String url = "";
				int k;
				//0 6
				
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
						url += year + month + date + "@";
					}

				}
				a = url.toString().split("@");
				
				String month1 = "00";
				String date1 = "00";
				String url1 = "";
				int k1;

				for (int j = 0; j < 12; j++) {
					int m = Integer.parseInt(month1) + 1;
					if (m < 10)
						month1 = "0" + m;
					else
						month1 = "" + m;
					date1 = "00";
					for (int i = 0; i < 31; i++) {

						k1 = Integer.parseInt(date1) + 1;
						if (k1 < 10)
							date1 = "0" + k1;
						else
							date1 = "" + k1;
						url1 += "`"+year + month1 + date1 + "`@";
					}
				}
				b=url1.toString().split("@");
				for(int i=0;i<a.length;i++)
						Crawler(a[i],b[i]);
			}

	public static void Crawler(String a, String n) throws SQLException,
			ClassNotFoundException, IOException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mytest?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "joey820924";
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();

		URL url = new URL("http://www.nba.com/gameline/"+a+"/");
		Document doc = Jsoup.parse(url, 30000);
		Elements stat = doc.select("div.nbaMnQuScores td");
		Elements name = doc.select("div.nbaModTopTeamName");
		int i1 = 0;

		String data;
		int j = 0;

		try {
			PreparedStatement pstmt = null;
			while (j != -1) {
					sb1.append(name.get(j).text() + "&");
				j++;
			}

		} catch (Exception e) {
			j = -1;
		}
		try {
			
			int k = 0;
			PreparedStatement pstmt = null;
			while (i1 != -1) {
				if(i1%1000==k){
					sb.append(stat.get(i1).text()+"&");
					k++;
				}
				i1++;
				if(k==9){
					k=18;
				}
				if(k==36){
					k=45;
				}
				if(k==63){
					k=72;
				}
				if(k==90){
					k=99;
				}
				if(k==117){
					k=126;
				}
				if(k==144){
					k=153;
				}
				if(k==171){
					k=180;
				}
				if(k==198){
					k=207;
				}
				if(k==225){
					k=234;
				}
				if(k==252){
					k=261;
				}
				if(k==279){
					k=288;
				}
				if(k==306){
					k=315;
				}
				if(k==333){
					k=342;
				}

			}

		} catch (Exception e) {
			i1 = -1;

		}

		
		while (sb.length() != 0) {
			try (Connection conn = DriverManager.getConnection(jdbcurl, user,
					password)) {
				System.out.printf("資料庫已%s %n", conn.isClosed() ? "關閉" : "開-起");
				Statement stmt = conn.createStatement();

				try {
					 
					String NBApoint[] = sb.toString().split("&");
					String NBAname[] = sb1.toString().split("&");
					
					int jj = 0;
					

					store(n, conn, stmt);
					int k = 0;
					int l = 0;
					int x = 0;
					PreparedStatement pstmt = null;
					while (k < 26) {
						if (k % 2 == 0) {
							pstmt = conn.prepareStatement("INSERT INTO " + n
									+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
							for (int i11 = 0; i11 < 11; i11++) {

								if (i11 == 0) {
									pstmt.setString(1, NBAname[k]);
									k++;
								} else if (i11 == 1) {
									pstmt.setString(2, null);
								} else {
									pstmt.setString(i11 + 1, NBApoint[l]);
									l++;

								}

							}
						} else {
							pstmt = conn.prepareStatement("INSERT INTO " + n
									+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
							for (int i11 = 0; i11 < 11; i11++) {
								if (i11 == 0) {
									pstmt.setString(1, null);

								} else if (i11 == 1) {
									pstmt.setString(2, NBAname[k]);
									k++;
								} else {

									pstmt.setString(i11 + 1, NBApoint[l]);
									l++;
								}

							}
						}

						pstmt.executeUpdate();

					}
				} catch (Exception ex) {

				}
				conn.close();
				break;
			}
		}

	}

	public static void store(String name, Connection conn, Statement stmt)
			throws SQLException {
		String sql = "CREATE TABLE " + name + " (`Away` VARCHAR(255), "
				+ "`Home` VARCHAR(255), " + "`Q1` VARCHAR(255) , "
				+ "`Q2` VARCHAR(255) , " + "`Q3` VARCHAR(255) , "
				+ "`Q4` VARCHAR(255) , " + "`OT1` VARCHAR(255) , "
				+ "`OT2` VARCHAR(255) , " + "`OT3` VARCHAR(255) , "
				+ "`OT4` VARCHAR(255) , " + "`F` VARCHAR(255)) "
				+ "ENGINE = MyISAM DEFAULT CHARSET = utf8 COLLATE = utf8_bin";

		// ///////建立UTF8編碼資料表

		stmt.executeUpdate(sql);
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

	}

	

	
}