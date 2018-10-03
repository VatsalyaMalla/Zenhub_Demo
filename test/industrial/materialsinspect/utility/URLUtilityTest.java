package com.ibm.mobilefirst.industrial.materialsinspect.utility;

import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.Utils.URLUtility;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.ibm.mobilefirst.BMSSOAuth.BMSSOConstants;
import com.ibm.mobilefirst.authentication.constants.AKAuthenticationConfigConstants;

public class URLUtilityTest {
	static Logger logger = Logger.getLogger(URLUtilityTest.class.getName());

	public static String getSORToken(String userName, String password) {
		JSONObject reqObj = new JSONObject();
		String accessToken = "";
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			String loginUrl = URLUtility.getRequiredUrl(AKAuthenticationConfigConstants.CLIENT_URL);
			logger.severe("Login URL :" + loginUrl + " for user: " + userName);
			HttpPost httpPost = new HttpPost(loginUrl);
			reqObj.put(BMSSOConstants.USERNAME, userName);
			reqObj.put(BMSSOConstants.PASSCODE, password);
			StringEntity input = new StringEntity(reqObj.toString());
			input.setContentType("application/json");
			httpPost.setEntity(input);
			HttpResponse response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				JSONObject object = (JSONObject) new JSONParser().parse(EntityUtils.toString(entity));
				logger.severe("object value from SOR :" + object);
				if (null != object) {
					accessToken = (String) object.get("TOKEN");
					long idUser = (long) object.get("ID_USER");
					accessToken = getAuthToken(accessToken, userName);
					logger.severe("TECNICAS SOR Token: " + accessToken);
					logger.severe("TECNICAS SOR idUser: " + idUser);
				} else {
					throw new NullPointerException();
				}
			}
		} catch (Exception e) {
			logger.severe("Exception " + e);
		}
		return accessToken;
	}

	public static String getAuthToken(String token, String userName) {
		String finalToken = "";
		try {
			if ((null != token && !token.isEmpty()) && (null != userName && !userName.isEmpty())) {
				String authToken = userName + ":" + token;
				finalToken = "Basic" + " " + new String(new Base64().encode(authToken.getBytes("utf-8")));
			}
		} catch (Exception e) {
			logger.severe("Exception " + e);
		}
		return finalToken;
	}
}
