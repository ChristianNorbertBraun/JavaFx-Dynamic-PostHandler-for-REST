package backendCommunication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class GPPDHandler {

	
	

	@SafeVarargs
	public static HttpURLConnection sendGET(String url, HashMap<String,String>... headers){
		if(headers.length >=1){
			return send("GET",url,"",headers[0]);
		}
		else{
			return send("GET",url,"",null);
		}
	}
	
	@SafeVarargs
	public static HttpURLConnection sendPUT(String url,String body, HashMap<String,String>... headers){
		if(headers.length >=1){
			return send("PUT",url,body,headers[0]);
		}
		else{
			return send("PUT",url,body,null);
		}
	}
	
	@SafeVarargs
	public static HttpURLConnection sendPOST(String url, String body, HashMap<String,String>... headers){
		if(headers.length >=1){
			return send("POST",url,body,headers[0]);
		}
		else{
			return send("POST",url,body,null);
		}
	}
	
	
	private static HttpURLConnection send(String requestType ,String url, String body, HashMap<String, String> headers) {
		
	
		try {
			HttpURLConnection con = createURLCon(url, requestType, headers);
			
			if(!requestType.equalsIgnoreCase("get")){
			// Writing body to backend
				BufferedWriter outStream = createOutputStream(con); 
				outStream.write(body);
				outStream.flush();
				outStream.close();
			}
			
			return con;
			
		} catch (Exception e) {
			return null;
		}

	}

	private static HttpURLConnection createURLCon(String url, String requestType, HashMap<String, String> headers) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(requestType.toUpperCase());
			con.setDoOutput(true);
			con.setDoInput(true);
			addHeaders(con, headers);
			con.setUseCaches( false );
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static HttpURLConnection addHeaders(HttpURLConnection con, HashMap<String, String> headers){
		if(headers !=null){
			Set<String> temp = headers.keySet();
			
			for(String key: temp){
				con.setRequestProperty(key, headers.get(key));
			}
			return con;
		}
		else{
			return con;
		}
	}
		
	
	private static BufferedReader createInputStream(HttpURLConnection con){
		try{
			return new BufferedReader(new InputStreamReader(con.getInputStream()));
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
	
	private static BufferedWriter createOutputStream(HttpURLConnection con){
		try{
			return new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
	
	public static String fetchingResponse(HttpURLConnection con){
		try{
			BufferedReader inStream = createInputStream(con);
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = inStream.readLine()) != null) {
				response.append(inputLine);
			}
			inStream.close();
			return response.toString();
		}
		catch(Exception e){
			try {
				throw new Exception("Check your url. It may be wrong!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
	}
	
	
	

}
