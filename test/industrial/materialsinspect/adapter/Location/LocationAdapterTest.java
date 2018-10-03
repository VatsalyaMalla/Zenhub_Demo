package com.ibm.mobilefirst.industrial.materialsinspect.adapter.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.ibm.mobilefirst.exception.TecnicasException;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.demo.data.LocationDataAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.utility.URLUtilityTest;

public class LocationAdapterTest {

	static Logger logger = Logger.getLogger(LocationAdapterTest.class.getName());

	@InjectMocks
	private LocationDataAdapter location = new LocationDataAdapter();

	private String locationId = "443";
	private String userId = "9";
	private String visitId = "29";
	private String apiVersion = "1.0.0";
	private String userName = "Dinesh";
	private String paswd = "Dinesh";
	private Response response;

	@Test
	public void testUpdateNewNcr() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newNcrData = "";
		try {
			newNcrData = FileUtils.readFileToString(new File("test/data/updateNewNcr.json"));
			response = location.updateNewNcr("", "", apiVersion, "", "", newNcrData, token);
			JSONObject responseObj = (JSONObject) response.getEntity();
			assertNotNull(responseObj);

		} catch (IOException e) {
			printException(e, "testUpdateNewNcr");
		}

	}

	@Test
	public void testUpdateNewNcrEmptyReqBody() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newNcrData = "";
		try {
			response = location.updateNewNcr("", "", apiVersion, "", "", newNcrData, token);
			fail("Exception was expected due to empty request body");

		} catch (TecnicasException te) {
			printException(te, "testUpdateNewNcr");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewNcrInvalidAction() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newNcrData = "";
		try {
			newNcrData = FileUtils.readFileToString(new File("test/data/updateNewNcrInvalidAction.json"));
			response = location.updateNewNcr("", "", apiVersion, "", "", newNcrData, token);
			fail("Exception was expected due to invalid action");

		} catch (IOException e) {
			printException(e, "testUpdateNewNcrInvalidAction");
		} catch (TecnicasException te) {
			printException(te, "testUpdateNewNcrInvalidAction");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewNcrEmptyAction() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newNcrData = "";
		try {
			newNcrData = FileUtils.readFileToString(new File("test/data/updateNewNcrEmptyAction.json"));
			response = location.updateNewNcr("", "", apiVersion, "", "", newNcrData, token);
			fail("Exception was expected due to empty action");

		} catch (IOException e) {
			printException(e, "testUpdateNewNcrEmptyAction");
		} catch (TecnicasException te) {
			printException(te, "testUpdateNewNcrEmptyAction");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewIncident() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newIncidentData = "";
		try {
			newIncidentData = FileUtils.readFileToString(new File("test/data/updateNewIncident.json"));
			response = location.updateNewIncident("", "", apiVersion, "", "", newIncidentData, token);
			JSONObject responseObj = (JSONObject) response.getEntity();
			assertNotNull(responseObj);

		} catch (IOException e) {
			printException(e, "testUpdateNewIncident");
		}
	}

	@Test
	public void testUpdateNewIncidentEmptyReqBody() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newIncidentData = "";
		try {
			response = location.updateNewIncident("", "", apiVersion, "", "", newIncidentData, token);
			fail("Exception was expected due to empty request body");

		} catch (TecnicasException te) {
			printException(te, "testUpdateNewIncidentEmptyReqBody");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewIncidentInvalidAction() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newNcrData = "";
		try {
			newNcrData = FileUtils.readFileToString(new File("test/data/updateNewIncidentInvalidAction.json"));
			response = location.updateNewIncident("", "", apiVersion, "", "", newNcrData, token);
			fail("Exception was expected due to invalid action");

		} catch (IOException e) {
			printException(e, "testUpdateNewIncidentInvalidAction");
		} catch (TecnicasException te) {
			printException(te, "testUpdateNewIncidentInvalidAction");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewIncidentEmptyAction() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newNcrData = "";
		try {
			newNcrData = FileUtils.readFileToString(new File("test/data/updateNewIncidentEmptyAction.json"));
			response = location.updateNewIncident("", "", apiVersion, "", "", newNcrData, token);
			fail("Exception was expected due to empty action");

		} catch (IOException e) {
			printException(e, "testUpdateNewIncidentEmptyAction");
		} catch (TecnicasException te) {
			printException(te, "testUpdateNewIncidentEmptyAction");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewPunchList() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newPunchListData = "";
		try {
			newPunchListData = FileUtils.readFileToString(new File("test/data/updateNewIncident.json"));
			response = location.updateNewPunchListItems("", "", apiVersion, "", "", newPunchListData, token);
			JSONObject responseObj = (JSONObject) response.getEntity();
			assertNotNull(responseObj);

		} catch (IOException e) {
			printException(e, "testUpdateNewPunchList");
		}
	}

	@Test
	public void testUpdateNewPunchListEmptyReqBody() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newPunchListData = "";
		try {
			response = location.updateNewPunchListItems("", "", apiVersion, "", "", newPunchListData, token);
			fail("Exception was expected due to empty request body");

		} catch (TecnicasException te) {
			printException(te, "testUpdateNewPunchListEmptyReqBody");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewPunchListInvalidAction() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newPunchListData = "";
		try {
			newPunchListData = FileUtils.readFileToString(new File("test/data/updateNewIncidentInvalidAction.json"));
			response = location.updateNewPunchListItems("", "", apiVersion, "", "", newPunchListData, token);
			fail("Exception was expected due to invalid action");

		} catch (IOException e) {
			printException(e, "testUpdateNewPunchListInvalidAction");
		} catch (TecnicasException te) {
			printException(te, "testUpdateNewPunchListInvalidAction");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}

	@Test
	public void testUpdateNewPunchListEmptyAction() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String newPunchListData = "";
		try {
			newPunchListData = FileUtils.readFileToString(new File("test/data/updateNewIncidentEmptyAction.json"));
			response = location.updateNewPunchListItems("", "", apiVersion, "", "", newPunchListData, token);
			fail("Exception was expected due to empty action");

		} catch (IOException e) {
			printException(e, "testUpdateNewPunchListEmptyAction");
		} catch (TecnicasException te) {
			printException(te, "testUpdateNewPunchListEmptyAction");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}

	}
	
	@Test
	public void testLocationIdFromSingleSource() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getLocationByIdFromSingleSource("", "", apiVersion, "", "", locationId, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testLocationIdFromSingleSourceWithoutLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getLocationByIdFromSingleSource("", "", apiVersion, "", "", "", token);
		fail("Exception was expected due to empty location id");
		assertEquals(500, response.getStatus());
	}
	
	@Test
	public void testItemsByLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getItemsByLocationId("", "", apiVersion, "", "", locationId,0,0, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testItemsByLocationWithoutLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getItemsByLocationId("", "", apiVersion, "", "", "", 0,0,token);
		fail("Exception was expected due to empty location id");
		assertEquals(500, response.getStatus());
	}
	
	@Test
	public void testSingleNcrsByLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getSingleNcrsByLocationId("", "", apiVersion, "", "", userId,visitId,locationId,0,0,token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testSingleNcrsWithoutLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getSingleNcrsByLocationId("", "", apiVersion, "", "", "","","", 0,0,token);
		fail("Exception was expected due to empty location id");
		assertEquals(500, response.getStatus());
	}
	
	@Test
	public void testSinglePunchListItemByLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getSinglePunchListItemByLocationId("", "", apiVersion, "", "", userId,visitId,0,0,token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testSingleIncidentByLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getSingleIncidentByLocationId("", "", apiVersion, "", "", userId,visitId,0,0,token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testMediaDescriptionByLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getMediaDescriptionByLocationId("", "","", apiVersion, "", "", locationId,userId,0,0,token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testMediaDescriptionWithoutLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getMediaDescriptionByLocationId("","", "", apiVersion, "", "", "","", 0,0,token);
		fail("Exception was expected due to empty location id");
		assertEquals(500, response.getStatus());
	}
	
	@Test
	public void testReportsDescriptionByLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getReportsDescriptionByLocationId("", "", apiVersion, "", "", locationId,userId,0,0,token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testReportsDescriptionWithoutLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = location.getReportsDescriptionByLocationId("","", apiVersion, "", "", "","", 0,0,token);
		fail("Exception was expected due to empty location id");
		assertEquals(500, response.getStatus());
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
