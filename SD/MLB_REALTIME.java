import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class MLB_REALTIME {
	public static void main(String[]args) throws ClassNotFoundException, SQLException, IOException{
		ScheduledExecutorService s=Executors.newSingleThreadScheduledExecutor();
		int a=1;
		s.scheduleWithFixedDelay(new Runnable(){
			
			public void run(){
				try {
					
					Crawler("20150707","20150707比數");
				
				} catch (ClassNotFoundException | SQLException
						| IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 200, 300, TimeUnit.MICROSECONDS);
		
	}
	
	public static void Crawler(String a, String n) throws SQLException,ClassNotFoundException, IOException {		
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mytest?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "joey820924";
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		URL url = new URL("http://tslc.stats.com/mlb/scoreboard.asp?day="+a+"&meta=true");
		Document doc = Jsoup.parse(url, 30000);
		Elements stat = doc.select("td.shsTotD");
		Elements name = doc.select("a.teamName");

		int i1 = 0;
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
		int i=0;
		int k1=21;
		try {
			PreparedStatement pstmt = null;
			while (i1 != -1) {
				if(i1%1000==k1){
				sb.append(stat.get(i1).text()+"&");
				k1++;
				i1++;
				if(k1==24){
					k1=33;
				}
				if(k1==36){
					k1=57;
				}
				if(k1==60){
					k1=69;
				}
				if(k1==72){
					k1=93;
				}
				if(k1==96){
					k1=105;
				}
				if(k1==108){
					k1=129;
				}
				if(k1==132){
					k1=141;
				}
				if(k1==144){
					k1=165;
				}
				if(k1==168){
					k1=177;
				}
				if(k1==180){
					k1=201;
				}
				if(k1==204){
					k1=213;
				}
				if(k1==216){
					k1=237;
				}
				if(k1==240){
					k1=249;
				}
				if(k1==252){
					k1=273;
				}
				if(k1==276){
					k1=285;
				}
				if(k1==288){
					k1=309;
				}
				if(k1==312){
					k1=321;
				}
				if(k1==324){
					k1=345;
				}
				if(k1==348){
					k1=357;
				}
				if(k1==360){
					k1=381;
				}
				if(k1==384){
					k1=393;
				}
				if(k1==396){
					k1=417;
				}
				if(k1==420){
					k1=429;
				}
				if(k1==432){
					k1=453;
				}
				if(k1==456){
					k1=465;
				}
				if(k1==468){
					k1=489;
				}
				if(k1==492){
					k1=501;
				}
				if(k1==504){
					k1=525;
				}
				if(k1==528){
					k1=537;
				}
				if(k1==540){
					k1=561;
				}
				if(k1==564){
					k1=573;
				}
				if(k1==576){
					k1=597;
				}
				if(k1==600){
					k1=609;
				}
				}else{
					i1++;
				}
				
			}

		} catch (Exception e) {
			i1 = -1;

		}
			try (Connection conn = DriverManager.getConnection(jdbcurl, user,
					password)) {
				System.out.printf("資料庫已%s %n", conn.isClosed() ? "關閉" : "開-起");
				Statement stmt = conn.createStatement();
				
				try {
					PreparedStatement pstmt = null;	
						String NBApoint[] = sb.toString().split("&");
						String NBAname[] = sb1.toString().split("&");
						Connection conn1 = DriverManager.getConnection(jdbcurl, user,password);
						Statement stmt1 = conn1.createStatement();
						stmt1.executeUpdate("DROP TABLE "+n);
						store(n, conn, stmt);
						int k = 0;
						int l = 0;
						int x = 0;
						
						while (k < 34) {
							if (k % 2 == 0) {
								pstmt = conn1.prepareStatement("INSERT INTO " + n
										+ " VALUES(?,?,?,?,?)");
								for (int i11 = 0; i11 < 5; i11++) {
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
								pstmt = conn1.prepareStatement("INSERT INTO " + n
										+ " VALUES(?,?,?,?,?)");
								for (int i11 = 0; i11 < 5; i11++) {
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
								}catch(Exception ex){
									
								}
							
			
					
				conn.close();
				} catch (Exception ex) {

				}
			
		
	}

	public static void store(String name, Connection conn, Statement stmt)
			throws SQLException {
		String sql = "CREATE TABLE " + name + " (`Away` VARCHAR(255), "
				+ "`Home` VARCHAR(255), " 
				+ "`Score` VARCHAR(255), " + "`Hit` VARCHAR(255), "
				+ "`Turnover` VARCHAR(255)) " 
				+ "ENGINE = MyISAM DEFAULT CHARSET = utf8 COLLATE = utf8_bin";

		// ///////建立UTF8編碼資料表

		stmt.executeUpdate(sql);

	}
}
