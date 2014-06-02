
package crawler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Initializer {
	


    public static void main(String[] args) throws Exception {
    	
        /*
         * Starta conexa com o banco de dados
         */
    	Connection conn = Database.get();
    	
    	/*
    	 * Busca todas as lojas ativas
    	 */
    	PreparedStatement stmt = conn.prepareStatement("SELECT * FROM store WHERE is_active = 1");
    	ResultSet result = stmt.executeQuery();
    	
    	while (result.next()) {
    		
    		ExecutorService executor = Executors.newFixedThreadPool(3);
    		
    		for (int i = 0; i < result.getInt("threads"); i++) {
    			
    			Spider bot = new Spider(result.getInt("id_store"), i);
        		
        		executor.submit(bot);
        		
        		Thread.sleep(2000);
        		
    		}
    		
    	}
    	
    	conn.close(); 
        
    }
    
}