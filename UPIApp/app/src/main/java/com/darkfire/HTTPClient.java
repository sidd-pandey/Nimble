package com.darkfire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HTTPClient {

	private String URL = "https://URL/otpService";

	public String callHTTPService() {

		
		String json="";
		JSONObject payload = null;

		try {

			payload = new JSONObject();
			payload.put("txnId", "12332423");
			payload.put("payerAddress", "nikhil@mgat");
			payload.put("payerName", "Nikhil M");
			payload.put("mobileNumber", "8654589562");
			payload.put("geoCode", "123466454 ");
			payload.put("location", "Mumbai,Maharashtra ");
			payload.put("ip", "142.12.26.52");
			payload.put("type", "mob");
			payload.put("id", "154fer53dfdf");
			payload.put("os", "android");

			payload.put("app", "MGSAPP");
			payload.put("capability", "453453d4f5343434df354");
			payload.put("accountAddressType", "ACCOUNT");

			JSONObject arrabj = new JSONObject();
			arrabj.put("ifsc", "MGAT0001865");
			arrabj.put("acType", "");
			arrabj.put("acNum", "");
			arrabj.put("iin", "");
			arrabj.put("uIdNum", "");
			arrabj.put("mmId", "");
			arrabj.put("mobNum", "");
			arrabj.put("cardNum", "");
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(arrabj);
			payload.put("detailsJson", jsonArray);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URL);
			httpPost.setEntity((new StringEntity(payload.toString(), "UTF-8")));
			httpPost.setHeader("Authorization",
					"Basic bWdzdXBpOmFkbWluQDEyMw=="); // +
														// LoginActivity.token.getToken());
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("@Response", json);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String response = json.toString();
		
		return response;

	}
	public static void main(String[] arg){
		HTTPClient client = new HTTPClient();
		client.callHTTPService();
	}

}
