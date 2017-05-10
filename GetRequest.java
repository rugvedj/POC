package com.enterprise.MyPOCs;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class GetRequest {
	public void verifyReservation() throws ClientProtocolException, IOException, JSONException {
		// Creates a reference to CloseableHttpClient, which is thread safe
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet("https://api.ghostinspector.com/v1/tests/586e67dd663c5645456e8092/execute/?apiKey=268140cd27b3a35106ab298cc685cd33e041831a");

			System.out.println("Triggering request " + httpget.getRequestLine());

			// Custom Response Handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			String responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			
			// build a JSON object
		    JSONObject obj = new JSONObject(responseBody);	 
		    // get the first result
		   String[] successMessage = JSONObject.getNames(obj);
		    for(int i=0; i<successMessage.length; i++){
		    	printLog(successMessage[i]);
		    }
//		    JSONObject dataArray = obj.getJSONArray("data");
		    for(int index=0; index<obj.length(); index++){
		    	printLog("ALL DATA OBJ ARE :: "+obj.getString("data").toString());
		    }
//		    System.out.println(res.getString("formatted_address"));
		    /*JSONObject loc =
		        res.getJSONObject("geometry").getJSONObject("location");
		    System.out.println("lat: " + loc.getDouble("lat") +
		                        ", lng: " + loc.getDouble("lng"));*/
			
		} finally {
			// close the connection. Make Sure you do this.
			httpclient.close();
		}
	}
	//Helper method to print Log
	public void printLog(String text){
		Logger logger =  LogManager.getLogger(getClass().getName());
		logger.info(text);
	}

}
