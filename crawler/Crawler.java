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

import java.sql.Timestamp;
import java.util.ArrayList;


public class Crawler extends Thread {

	public ArrayList<String> provider() {
		// method must be override
		return new ArrayList<String>();
	}
	
	public void beforeVisit(String url) {
		// method must be override
	}
	
	public void afterVisit(HttpRequest request) {
		// method must be override
	}
	
	public void onStart() {
		// method must be override
	}
	
	public void onFinish() {
		// method must be override
	}
	
	public void unableToAcess(String url) {
		// method must be override
	}
	
	public void run() {
		
		try {

			onStart();
		
			ArrayList<String> linksToCrawl = provider();
			
			if (linksToCrawl.size() == 0) {
				Thread.currentThread().interrupt();
			}
			
			for (String url : linksToCrawl) {
				
				beforeVisit(url);
				
				try {
					WebUrl webUrl = new WebUrl(url);
					HttpRequest request = webUrl.sendHttpRequest();
					
					afterVisit(request);
					
				} catch (Exception e) {
					
					unableToAcess(url);
					
				}

			}
			
			onFinish();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
	
}