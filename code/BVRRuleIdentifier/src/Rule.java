package neu.proposal.rules;

import java.util.ArrayList;

public class Rule {
	
	String name;
	ArrayList<Triple> head;
	ArrayList<Triple> body;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Triple> getHead() {
		return head;
	}
	public void setHead(ArrayList<Triple> head) {
		this.head = head;
	}
	public ArrayList<Triple> getBody() {
		return body;
	}
	public void setBody(ArrayList<Triple> body) {
		this.body = body;
	}
	
	

}
