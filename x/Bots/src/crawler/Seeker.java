
package crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Seeker extends Thread {
	
	private int storeId;
	private String crawlerName;
	
	public Seeker(int storeId, String crawlerName) {
		this.storeId = storeId;
		this.crawlerName = crawlerName;
	}

    public void run() {
    	
    	Spider bot = new Spider(this.storeId);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(bot);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
}