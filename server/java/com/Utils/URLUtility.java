package com.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.Utils.ServiceConstants;

public class URLUtility {
	
	static Logger logger = Logger.getLogger(URLUtility.class.getName());
	static Properties properties = null;
	
	public static String getSORDataWithHeaderParams(String url,String token){
		String output;
		URL obj;
		HttpURLConnection con=null;
		StringBuilder sb = new StringBuilder();
		try {
			obj = new URL(url);
			
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("language","US");
			con.setRequestProperty("Authorization",token);
			con.setRequestProperty("Cache-Control","no-cache,no-store");
			con.setRequestProperty("Pragma","no-cache");
			con.setDoOutput(true);
			con.connect();
			int responseCode = con.getResponseCode();
			logger.severe("Sending 'GET' request to URL : " + url);
			logger.severe("Response Code : " + responseCode);
			if(responseCode ==200){
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(con.getInputStream())));
			    while ((output = br.readLine()) != null)
		          {
		              sb.append(output + '\n');
		             logger.severe("line " + output);
		          }
			    br.close();
			}else{
				logger.info("Response code is other than 200 for getSORDataWithHeaderParams for the url" + url);
			}
		} catch (MalformedURLException e) {
			printException(e,"getSORDataWithHeaderParams");
		}
		catch (IOException e) {
			printException(e,"getSORDataWithHeaderParams");
		}finally{
			if (con != null) {
		        con.disconnect();
		    }
		}
        return sb.toString();
	}
	
	public static String getSORDataWithHeaderParamsForPost(String url,String requestObj,String authToken){
		String output;
		String response;
		String res ;
		JSONObject result = new JSONObject();
		URL obj;
		HttpURLConnection con=null;
		StringBuilder sb = new StringBuilder();
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
		
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization",authToken);
			con.setRequestProperty("language","US");
			con.setRequestProperty("Cache-Control","no-cache,no-store");
			con.setRequestProperty("Pragma","no-cache");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(
					con.getOutputStream());
			wr.write(requestObj);
			wr.flush();
			wr.close();
			
			response = con.getResponseMessage();
			int responseCode = con.getResponseCode();
			Object code = con.getResponseCode();
			logger.severe("Sending 'POST' request to URL : " + url);
			logger.severe("Response Code : " + responseCode);
			
			if(200==responseCode){
				res = "Successfully updated the SOR service.";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(con.getInputStream())));

	          while ((output = br.readLine()) != null)
	          {
	              sb.append(output + '\n');
	              logger.severe("line " + output);
	          }
	          br.close();
			}
			else if(403==responseCode){
				res = "Authorization is not valid.Please login again.";
			}else{
				res = "Updating the SOR service failed.";
		}
			result.put("message", res);
			result.put("id", code.toString());
		} catch (MalformedURLException e) {
			printException(e,"getSORDataWithHeaderParamsForPost");
		}
		catch (IOException e) {
			printException(e,"getSORDataWithHeaderParamsForPost");
		}finally{
			if (con != null) {
		        con.disconnect();
		    }
		}
        return result.toString();
	}
	
	
	public static String getSORDataWithHeaderParamsForLogout(String url,String requestObj,String authToken){
		String output;
		URL obj;
		String response;
		HttpURLConnection con=null;
		StringBuilder sb = new StringBuilder();
		JSONObject result = new JSONObject();
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
		
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization",authToken);
			con.setRequestProperty("language","US");
			con.setRequestProperty("Cache-Control","no-cache,no-store");
			con.setRequestProperty("Pragma","no-cache");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(
					con.getOutputStream());
			wr.write(requestObj);
			wr.flush();
			wr.close();
			
			response = con.getResponseMessage();
			int responseCode = con.getResponseCode();
			logger.severe("Sending 'POST' request to URL : " + url);
			logger.severe("Response Code : " + responseCode);
			
			if(200==responseCode){
				result.put("id", "200");
			}else{
				String code = Integer.toString(responseCode);
				result.put("id", code);
				logger.info("Response code is other than 200.");
		}
		} catch (MalformedURLException e) {
			printException(e,"getSORDataWithHeaderParamsForLogout");
		}
		catch (IOException e) {
			printException(e,"getSORDataWithHeaderParamsForLogout");
		}finally{
			if (con != null) {
		        con.disconnect();
		    }
		}
        return result.toString();
	}
	
	public static String getSORDataWithHeaderParamsForUpdateNCR(String url,String requestObj,String token){
		String output;
		String response="";
		URL obj;
		HttpURLConnection con=null;
		StringBuilder sb = new StringBuilder();
		JSONObject res = new JSONObject();
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("language","US");
		con.setRequestProperty("Authorization",token);
		con.setRequestProperty("Cache-Control","no-cache,no-store");
		con.setRequestProperty("Pragma","no-cache");
		con.setDoOutput(true);
		con.connect();
		OutputStreamWriter wr = new OutputStreamWriter(
				con.getOutputStream());
		wr.write(requestObj);
		wr.flush();
		wr.close();
		response = con.getResponseMessage();
		
		int responseCode = con.getResponseCode();
		Object code = con.getResponseCode();
		logger.severe("Sending 'PUT' request to URL : " + url);
		logger.severe("Response Code : " + responseCode);
		
		if(200==responseCode){
			response = "SOR service successfully updated.";
			
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(con.getInputStream())));
		
          while ((output = br.readLine()) != null)
          {
              sb.append(output + '\n');
             logger.severe("line " + output);

          }
          br.close();
		}else{
			response = "SOR service not updated.";
			logger.info("Response code is other than 200.");
		}
		res.put("message", response);
        res.put("id", code.toString());
		} catch (MalformedURLException e) {
			printException(e,"getSORDataWithHeaderParamsForUpdateNCR");
		}
		catch (IOException e) {
			printException(e,"getSORDataWithHeaderParamsForUpdateNCR");
		}finally{
			if (con != null) {
		        con.disconnect();
		    }
		}
        return res.toString();
	}
	
	public static String getSORDataWithHeaderParamsWithLanguage(String url,String token){
		String output;
		
		URL obj;
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con=null;
		try {
			obj = new URL(url);
		
			con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		con.setRequestProperty("language","US");
		con.setRequestProperty("Authorization",token);
		con.setRequestProperty("Cache-Control","no-cache,no-store");
		con.setRequestProperty("Pragma","no-cache");
		con.setDoOutput(true);
		
		int responseCode = con.getResponseCode();
		logger.severe("\nSending 'GET' request to URL : " + url);
		logger.severe("Response Code : " + responseCode);
		
		if(responseCode==200){
			
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(con.getInputStream())));
		
          while ((output = br.readLine()) != null)
          {
              sb.append(output + '\n');
              logger.severe("line " + output);

          }
          br.close();
		}else{
			logger.info("SOR service returned response code other than 200.");
		}
		} catch (MalformedURLException e) {
			printException(e,"getSORDataWithHeaderParamsWithLanguage");
		}
		catch (IOException e) {
			printException(e,"getSORDataWithHeaderParamsWithLanguage");
		}finally{
			if (con != null) {
		        con.disconnect();
		    }
		}
        return sb.toString();
	}
	
	public static String getSORDataWithHeaderParamsForReschedulePUT(String url,String requestObj,String authToken){
		String output;
		String response;
		String res ;
		JSONObject result = new JSONObject();
		URL obj;
		HttpURLConnection con=null;
		StringBuilder sb = new StringBuilder();
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization",authToken);
			con.setRequestProperty("language","US");
			con.setRequestProperty("Cache-Control","no-cache,no-store");
			con.setRequestProperty("Pragma","no-cache");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(
					con.getOutputStream());
			wr.write(requestObj);
			wr.flush();
			wr.close();

			response = con.getResponseMessage();
			int responseCode = con.getResponseCode();
			Object code = con.getResponseCode();
			logger.severe("Sending 'POST' request to URL : " + url);
			logger.severe("Response Code : " + responseCode);

			if(200==responseCode){
				res = "Successfully updated the SOR service.";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(con.getInputStream())));

	          while ((output = br.readLine()) != null)
	          {
	              sb.append(output + '\n');
	              logger.severe("line " + output);
	          }
	          br.close();
			}
			else if(403==responseCode){
				res = "Authorization is not valid.Please login again.";
			}else{
				res = "Updating the SOR service failed.";
		}
			result.put("message", res);
			result.put("id", code.toString());
		} catch (MalformedURLException e) {
			printException(e,"getSORDataWithHeaderParamsForPost");
		}
		catch (IOException e) {
			printException(e,"getSORDataWithHeaderParamsForPost");
		}finally{
			if (con != null) {
		        con.disconnect();
		    }
		}
	    return result.toString();
	}  
	
	public static JSONObject getSORDataWithHeaderParamsForUploadMedia(String url,String requestObj,String token){
		String output;
		URL obj;
		String res ;
		JSONObject result = new JSONObject();
		HttpURLConnection con=null;
		StringBuilder sb = new StringBuilder();
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod(ServiceConstants.POST);
		con.setRequestProperty(ServiceConstants.CONTENT_TYPE, ServiceConstants.APPLICATIONJSON);
		con.setRequestProperty(ServiceConstants.LANGUAGE,ServiceConstants.US);
		con.setRequestProperty(ServiceConstants.AUTHORIZATION,token);
		con.setRequestProperty("Cache-Control","no-cache,no-store");
		con.setRequestProperty("Pragma","no-cache");
		con.setDoOutput(true);
		con.connect();
		OutputStreamWriter wr = new OutputStreamWriter(
				con.getOutputStream());
		wr.write(requestObj);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		Object code = con.getResponseCode();
		logger.severe("Sending 'POST' request to URL : " + url);
		logger.severe("Response Code : " + responseCode);
		
		if(200==responseCode){
		res = "Successfully updated the SOR service.";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(con.getInputStream())));
		
          while ((output = br.readLine()) != null)
          {
              sb.append(output + '\n');
             logger.severe("line " + output);
          }
          result.put("value", sb.toString());
          br.close();
		}else{
			res = "Updating the SOR service failed.";
			result.put("value", "");
			logger.info("Response code is other than 200.");
		}
		result.put("message", res);
		result.put("id", code.toString());
		} catch (MalformedURLException e) {
			printExceptions(e,"getSORDataWithHeaderParamsForUploadMedia");
		}
		catch (IOException e) {
			printExceptions(e,"getSORDataWithHeaderParamsForUploadMedia");
		}finally{
			if (con != null) {
		        con.disconnect();
		    }
		}
        return result;
	}
	
	static void printExceptions(Exception e,String methodName) {
		logger.severe(ServiceConstants.EXCEPTION_IN+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
	
	public static Date getTime(){
	
	Calendar calobj = Calendar.getInstance();
	return calobj.getTime();
	}
	
	public static String getAuthToken(String token,String userName){
		String finalToken="";
		try{
			if((null!=token && !token.isEmpty()) && (null!=userName && !userName.isEmpty())){
				String authToken = userName+":"+token;
			    finalToken = "Basic"+" "+new String(new Base64().encode(authToken.getBytes("utf-8")));
			}
		}catch(Exception e){
			printException(e,"getAuthToken");
		}
		return finalToken;
		}
	
	public static Properties getipConfig() {
		return properties;
	}
	
	public static void loadIpProperties(){
		try{
		properties = new Properties();
		File ipConfig = new File("ipConfig.properties");
		FileReader reader = new FileReader(ipConfig);
		properties.load(reader);
		}catch(Exception e){
			printException(e,"loadIpProperties");
		}
	}
	
	public static String getRequiredUrl(String key){
		if(properties == null){
			loadIpProperties();
		}
		return properties.getProperty(key);
	}
	
static void printException(Exception e,String methodName) {
		
		logger.severe("Exception in: "+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
}
