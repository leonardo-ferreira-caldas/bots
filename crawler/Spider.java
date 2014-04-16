/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package crawler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Spider extends Crawler {

	private final static String[] FILTERS = new String[] {"css", "js", "bmp", "gif", "jpg", "jpeg", "png", "tiff", "mid", "mp2", "mp3", "mp4", "wav", "avi", "mov", "mpeg", "ram", "m4v", "pdf" + "", "rm", "smil", "wmv", "swf", "wma", "zip", "rar", "gz"};
//	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
//			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private int storeId;
	private ResultSet store;
	private Connection conn;
	private int threadId;
	private long benchmark;
	
	public Spider(int storeId, int threadId) {
		this.storeId = storeId;
		this.threadId = threadId;
	}

	@Override
	public void onStart() {
		try {
			
			benchmark = System.currentTimeMillis();
			
			conn = Database.get();	
			
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM store WHERE id_store = ?");
			stmt.setInt(1, this.storeId);
        	ResultSet result = stmt.executeQuery();
        	result.first();
        	
        	this.store = result;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onFinish() {
		try {

			Spider bot = new Spider(this.storeId, this.threadId);
			ExecutorService executor = Executors.newSingleThreadExecutor();
    		executor.execute(bot);
			
			conn.close();
			
			benchmark = (System.currentTimeMillis() - benchmark) / 1000;
			System.out.println("Spider " + this.threadId + " executed in " + this.benchmark + " seconds.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void unableToAcess(String url) {
	
		PreparedStatement update;
		
		try {
			
			update = conn.prepareStatement("UPDATE page SET in_use = 0, was_scanned = 0 WHERE url = ? AND fk_store = ?");
			update.setString(1, url);
			update.setInt(2, store.getInt("id_store"));
			update.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public ArrayList<String> provider() {
		
		try {
			
			PreparedStatement exists = conn.prepareStatement("SELECT id_page FROM page WHERE fk_store = ?");
			exists.setInt(1, store.getInt("id_store"));
			ResultSet found = exists.executeQuery();
			
			found.last();
		    int size = found.getRow();
		    found.beforeFirst();
			
		    ArrayList<String> links = new ArrayList<String>();
		    
			if (size == 0) {

				links.add(store.getString("homepage"));

			} else {
				
				PreparedStatement select = conn.prepareStatement("SELECT id_page, url FROM page WHERE fk_store = ? AND was_scanned = 0 AND in_use = 0 LIMIT 0, 10");
				select.setInt(1, store.getInt("id_store"));
				ResultSet rows = select.executeQuery();
				
				String pageIds = new String();
				
				while (rows.next()) {
					if (pageIds.isEmpty()) {
						pageIds = "" + rows.getInt("id_page");
					} else {
						pageIds += "," + rows.getInt("id_page");
					}
					
					links.add(rows.getString("url"));
				}

				PreparedStatement update = conn.prepareStatement("UPDATE page SET in_use = 1 WHERE id_page IN(" + pageIds + ")");
				update.executeUpdate();
				
				
			}
			
			return links;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@Override
	public void beforeVisit(String url) {
		// do nothing
	}
	
	@Override
	public void afterVisit(HttpRequest request) {
		
		try {
			
			for (String url : request.getFoundLinks()) {
					
				//if (FILTERS.matcher(url).matches()) {
				if (verificarExtesao(url)) {
					continue;
				} else if (!url.contains(store.getString("domains"))) {
					continue;
				}
				
				try {

					PreparedStatement stmt = conn.prepareStatement("INSERT INTO page(fk_store, url, was_scanned, is_product, in_use, last_visit) VALUES(?, ?, ?, ?, ?, ?)");
					stmt.setInt(1, store.getInt("id_store"));
					stmt.setString(2, url);	
					stmt.setInt(3, 0);
					
					Pattern isProduct = Pattern.compile(store.getString("product_pattern"));
					
					if (isProduct.matcher(url).matches()) {
						stmt.setInt(4, 1);
					} else { 
						stmt.setInt(4, 0);
					}
					
					stmt.setInt(5, 0);
					stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					stmt.execute();
				
				} catch (Exception e) {
					// dead lock exception, do nothing
				}
				
			}

			PreparedStatement update = conn.prepareStatement("UPDATE page SET in_use = 0, was_scanned = 1 WHERE url = ? AND fk_store = ?");
			update.setString(1, request.getUrl());
			update.setInt(2, store.getInt("id_store"));
			update.executeUpdate();
			

		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private boolean verificarExtesao(String url) {
		
		for (String extensao: FILTERS) {
			if (url.endsWith("." + extensao)) {
				return true;
			}
		}
		
		return false;
		
	}
	
}