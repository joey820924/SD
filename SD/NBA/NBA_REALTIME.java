package NBA;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NBA_REALTIME {
		public static void main(String[]args) throws ClassNotFoundException, SQLException, IOException {
			ScheduledExecutorService s=Executors.newSingleThreadScheduledExecutor();
			int a=1;
			s.scheduleWithFixedDelay(new Runnable(){
				
				public void run(){
					try {
						
						Crawler("20150715","20150715比數");
					
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
	URL url = new URL("http://www.nba.com/gameline/"+a+"/");
	Document doc = Jsoup.parse(url, 30000);
	Elements stat = doc.select("div.nbaModTopTeamNum");
	Elements name = doc.select("div.nbaModTopTeamName");

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
	try {
		PreparedStatement pstmt = null;
		while (i1 != -1) {
			//System.out.println(stat.get(i1).text());
			sb.append(stat.get(i1).text()+"&");
			
			i1++;
		}

	} catch (Exception e) {
		i1 = -1;

	}
	
	
	
		try (Connection conn = DriverManager.getConnection(jdbcurl, user,
				password)) {
			System.out.printf("資料庫已%s %n", conn.isClosed() ? "關閉" : "開-起");
			Statement stmt = conn.createStatement();
			//System.out.print(sb.toString());
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
					
					while (k < 16) {
						if (k % 2 == 0) {
							pstmt = conn1.prepareStatement("INSERT INTO " + n
									+ " VALUES(?,?,?)");
							for (int i11 = 0; i11 < 3; i11++) {
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
									+ " VALUES(?,?,?)");
							for (int i11 = 0; i11 < 3; i11++) {
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
			+ "`Home` VARCHAR(255), " + "`Point` VARCHAR(255)) "
			+ "ENGINE = MyISAM DEFAULT CHARSET = utf8 COLLATE = utf8_bin";

	// ///////建立UTF8編碼資料表

	stmt.executeUpdate(sql);

}

}


