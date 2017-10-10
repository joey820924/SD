package WC;


	import java.util.HashSet; 
	import java.util.Set; 

	import org.htmlparser.Node; 
	import org.htmlparser.NodeFilter; 
	import org.htmlparser.Parser; 
	import org.htmlparser.filters.NodeClassFilter; 
	import org.htmlparser.filters.OrFilter;
	import org.htmlparser.tags.LinkTag; 
	import org.htmlparser.tags.ImageTag; 
	import org.htmlparser.util.NodeList;
	import org.htmlparser.util.ParserException; 
	public class  HtmlParser {
		 public static Set<String> extracLinks(String url, LinkFilter filter) { 
			 Set<String> links = new HashSet<String>();
		// 獲取一個網站上的鏈接,filter 用來過濾鏈接
			 try{
				 Parser parser = new Parser(url);
				 parser.setEncoding("gb2312"); 
				 // 過濾標籤的filter，用來提取frame 標籤裡的src 屬性所表示的鏈接
				 NodeFilter frameFilter = new NodeFilter() {
					 private static final long serialVersionUID = 1L; 
					 @Override
						 public boolean accept(Node node) { 
							 if (node.getText().startsWith("frame src="))  
								 return true; 
							 return false;
						 }
						
				 };
				// OrFilter 來設置過濾 標籤，和 標籤
				 OrFilter linkFilter = new OrFilter(new NodeClassFilter(  LinkTag.class), frameFilter);  
				// 得到所有經過過濾的標籤  
				 NodeList list = parser.extractAllNodesThatMatch(linkFilter);
				 for (int i = 0; i < list.size(); i++) {  
					 Node tag = list.elementAt(i);  
					 if (tag instanceof LinkTag){// 標籤
						 LinkTag link = (LinkTag) tag;  
						 String linkUrl = link.getLink();// url  
						 
						 
						 if (filter.accept(linkUrl))  
							 links.add(linkUrl);
						 
					 
					 }
					
					 else{
						// 提取frame 裡src 屬性的鏈接如  
						 String frame = tag.getText();  
						 int start = frame.indexOf("src="); 
						 
						 frame = frame.substring(start);  
						 int end = frame.indexOf(" ");  
						 if (end == -1)  
							 end = frame.indexOf(">");  
						 String frameUrl = frame.substring(5, end - 1);
						
						 if (filter==null || filter.accept(frameUrl))  
							 links.add(frameUrl);  
						 
					 }
						 
				 }
				 
			 }catch (ParserException e) {  
				 e.printStackTrace();  
			 }
			 return links;  
		 }
	
}
