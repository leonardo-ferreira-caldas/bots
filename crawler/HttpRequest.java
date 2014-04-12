package crawler;

import java.util.ArrayList;

public class HttpRequest {

	private String response;
	private int statusCode;
	private String url;
	private ArrayList<String> foundLinks; 
	
	public HttpRequest (String url, int statusCode, String response, ArrayList<String> foundLinks) {
		this.url = url;
		this.statusCode = statusCode;
		this.response = response;
		this.foundLinks = foundLinks;
	}
	
	public String getResponse() {
		return this.response;
	}
	
	public int getResponseStatusCode() {
		return this.statusCode;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public ArrayList<String> getFoundLinks() {
		return this.foundLinks;
	}
	
}
