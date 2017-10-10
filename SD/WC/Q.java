package WC;

import java.util.LinkedList;

public class Q {
private LinkedList<Object> queue = new LinkedList<Object>();
	
	public void enque(Object t){
		queue.addLast(t); 
	}
	public Object deQueue() {
		return queue.removeFirst();
	}
	public boolean isQueueEmpty() { 
		return queue.isEmpty();
	}
	 public boolean contians(Object t) {
		 return queue.contains(t);
	}
	 public boolean empty() {
		 return queue.isEmpty(); 
	 }
}
