package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class WebUrl {
	
	private String url;
	
	public WebUrl(String url) {
		this.url = url;
	}
    
    /**
     * Makes a HTTP request to a URL and receive response
     * @param requestUrl the URL address
     * @param method Indicates the request method, "POST" or "GET"
     * @param params a map of parameters send along with the request
     * @return An array of String containing text lines in response
     * @throws Exception 
     * @throws IOException
     */
    public HttpRequest sendHttpRequest() throws Exception {
       
       URL url = new URL(this.url);
       URLConnection urlConn = url.openConnection();
       urlConn.setUseCaches(false);
        
       // the request will return a response
       urlConn.setDoInput(true);
       
       // set request method to GET
       urlConn.setDoOutput(false);
       
       try {
       
	       // reads response, store line by line in an array of Strings
	       BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
	       
       
	       StringBuilder response = new StringBuilder();
	       String line = new String();
	       
	       while ((line = reader.readLine()) != null) {
	    	   response.append(line);
	       }
	        
	       reader.close();
	       
	       Pattern regex = Pattern.compile("(?<=href=[\"|\'])([\\w\\/:;\\.\\-\\?%=+&#!, ]+)(?=[\"|\'])");
	       Matcher matcher = regex.matcher(response);
	       ArrayList<String> links = new ArrayList<String>();
	       
	       while (matcher.find()) {
	    	   links.add(matcher.group());
	       }
	 
	       HttpRequest http = new HttpRequest(this.url, 200, response.toString(), links);
	       
	       return http;
       
      } catch (IOException e) {
    	   
    	   throw new Exception("Unable to acess URL: " + this.url);
    	   
       }
    
    }
}