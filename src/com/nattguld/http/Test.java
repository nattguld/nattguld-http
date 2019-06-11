package com.nattguld.http;

import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;

public class Test {
	
	
	public static void main(String[] args) throws Exception {
		//System.setProperty("javax.net.debug", "ssl,handshake");
		
		NetConfig.getGlobalInstance().setDebug(true);
		
		
		//HttpClient c = new HttpClient(new HttpProxy("127.0.0.1", 8888));
		
		
		
		
		
		//HttpClient c = new HttpClient(new HttpProxy("146.71.87.106", 31373, "K8pLQBbVis", "rHblmnnkju"));
		HttpClient c = new HttpClient();
		//HttpClient c = new HttpClient();
		
		//c.dispatchRequest(new GetRequest("https://www.xvideos.com/"));

		RequestResponse rr = c.dispatchRequest(new GetRequest("https://www.pornhub.com/view_video.php?viewkey=ph5c9934cf3635e"));
		
		
		
		//System.out.println(InetAddress.getByName("www.xvideos.com"));


		
		
		
		/*c.download("test.jar", new GetRequest("http://api.nattguld.com/data/adult_tube_suite/update.jar").setProgressListener(new RequestProgressListener() {

			@Override
			public void onChange(int progress) {
				System.out.println(progress);
			}
			
		}));*/
		
		//System.out.println(doStuff(c));
	

	}
	
}
