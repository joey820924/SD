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
		// ����@�Ӻ����W���챵,filter �ΨӹL�o�챵
			 try{
				 Parser parser = new Parser(url);
				 parser.setEncoding("gb2312"); 
				 // �L�o���Ҫ�filter�A�ΨӴ���frame ���Ҹ̪�src �ݩʩҪ�ܪ��챵
				 NodeFilter frameFilter = new NodeFilter() {
					 private static final long serialVersionUID = 1L; 
					 @Override
						 public boolean accept(Node node) { 
							 if (node.getText().startsWith("frame src="))  
								 return true; 
							 return false;
						 }
						
				 };
				// OrFilter �ӳ]�m�L�o ���ҡA�M ����
				 OrFilter linkFilter = new OrFilter(new NodeClassFilter(  LinkTag.class), frameFilter);  
				// �o��Ҧ��g�L�L�o������  
				 NodeList list = parser.extractAllNodesThatMatch(linkFilter);
				 for (int i = 0; i < list.size(); i++) {  
					 Node tag = list.elementAt(i);  
					 if (tag instanceof LinkTag){// ����
						 LinkTag link = (LinkTag) tag;  
						 String linkUrl = link.getLink();// url  
						 
						 
						 if (filter.accept(linkUrl))  
							 links.add(linkUrl);
						 
					 
					 }
					
					 else{
						// ����frame ��src �ݩʪ��챵�p  
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
