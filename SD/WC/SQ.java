package WC;

import java.util.HashSet;
import java.util.Set;

import java.util.HashSet;
import java.util.Set;
public class SQ {
	private static Set<Object> visitedUrl = new HashSet<>();
	public static void addVisitedUrl(String url) {  
		visitedUrl.add(url);
	}
	public static void removeVisitedUrl(String url) {  
		 visitedUrl.remove(url);
	}
	 public static int getVisitedUrlNum() {  
		 return visitedUrl.size();
	}
	 private static Q unVisitedUrl = new Q();
	 public static Q getUnVisitedUrl() {  
		 return unVisitedUrl; 
	}
	 public static Object unVisitedUrlDeQueue() {  
		 return unVisitedUrl.deQueue();
	}
	 public static void addUnvisitedUrl(String url) { 
		 if (url != null && !url.trim().equals("") && !visitedUrl.contains(url)  && !unVisitedUrl.contians(url))  
			 unVisitedUrl.enque(url);	
		}
	public static boolean unVisitedUrlsEmpty() {  
		return unVisitedUrl.empty();  
	}
	
}
