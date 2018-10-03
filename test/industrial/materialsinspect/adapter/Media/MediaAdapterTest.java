package com.ibm.mobilefirst.industrial.materialsinspect.adapter.Media;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.ibm.mobilefirst.exception.TecnicasException;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.demo.data.MediaDataAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.utility.URLUtilityTest;

@RunWith(MockitoJUnitRunner.class)
public class MediaAdapterTest {

	static Logger logger = Logger.getLogger(MediaAdapterTest.class.getName());

	@InjectMocks
	private MediaDataAdapter media = new MediaDataAdapter();

	private String locationId = "443";
	private String apiVersion = "1.0.0";
	private String userName = "Dinesh";
	private String paswd = "Dinesh";
	private Response response;
	
	@Test
	public void testNCRUpdate() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String ncrData = "";
		try {
			ncrData = FileUtils.readFileToString(new File("test/data/ncrUpdate.json"));
		} catch (IOException e) {
			printException(e, "testNCRUpdate");
		}
		response = media.ncrMedia("", "", "", apiVersion, "", "9","11",ncrData, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testNCRUpdateWithEmptyRequest() {
			String token = URLUtilityTest.getSORToken(userName, paswd);
			response = media.ncrMedia("", "", "", apiVersion, "", "9","11","", token);
			JSONObject responseObj = (JSONObject) response.getEntity();
			assertEquals(1000, response.getStatus());
	}
	
	void printException(Exception e, String methodName) {
		logger.severe("Exception in " + methodName);
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		logger.severe(sb.toString());
	}
}
