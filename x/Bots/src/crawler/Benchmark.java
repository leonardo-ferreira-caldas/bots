package crawler;

import java.util.ArrayList;

public class Benchmark implements Runnable {
	
	public void run () {
		
		ArrayList<String> arr = new ArrayList<String>();
    	arr.add("www.google.com");arr.add("www.yahoo.com.br");arr.add("www.americanas.com.br");arr.add("www.submarino.com.br");arr.add("www.saraivalivraria.com.br");arr.add("www.bing.com");arr.add("www.globo.com");arr.add("www.google.com");arr.add("www.yahoo.com.br");arr.add("www.americanas.com.br");arr.add("www.submarino.com.br");arr.add("www.saraivalivraria.com.br");arr.add("www.bing.com");arr.add("www.globo.com");arr.add("www.google.com");arr.add("www.yahoo.com.br");arr.add("www.americanas.com.br");arr.add("www.submarino.com.br");arr.add("www.saraivalivraria.com.br");arr.add("www.bing.com");arr.add("www.globo.com");arr.add("www.google.com");arr.add("www.yahoo.com.br");arr.add("www.americanas.com.br");arr.add("www.submarino.com.br");arr.add("www.saraivalivraria.com.br");arr.add("www.bing.com");arr.add("www.globo.com");arr.add("www.google.com");arr.add("www.yahoo.com.br");arr.add("www.americanas.com.br");arr.add("www.submarino.com.br");arr.add("www.saraivalivraria.com.br");arr.add("www.bing.com");arr.add("www.globo.com");arr.add("www.google.com");arr.add("www.yahoo.com.br");arr.add("www.americanas.com.br");arr.add("www.submarino.com.br");arr.add("www.saraivalivraria.com.br");arr.add("www.bing.com");arr.add("www.globo.com");arr.add("www.google.com");arr.add("www.yahoo.com.br");arr.add("www.americanas.com.br");arr.add("www.submarino.com.br");arr.add("www.saraivalivraria.com.br");arr.add("www.bing.com");arr.add("www.globo.com");
    	
    	long time = System.currentTimeMillis();
    	
    	for (String url : arr) {
    		WebUrl wb = new WebUrl("http://" + url);
    		try {
				HttpRequest http = wb.sendHttpRequest();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		System.out.println("Url:" + url + " loaded in " + ((System.currentTimeMillis() - time) / 1000) + " seconds.");
    	}
		
	}
	
}