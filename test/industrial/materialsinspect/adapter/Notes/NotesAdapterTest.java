package com.ibm.mobilefirst.industrial.materialsinspect.adapter.Notes;

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
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.demo.data.NotesDataAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.utility.URLUtilityTest;

@RunWith(MockitoJUnitRunner.class)
public class NotesAdapterTest {

	static Logger logger = Logger.getLogger(NotesAdapterTest.class.getName());

	@InjectMocks
	private NotesDataAdapter notes = new NotesDataAdapter();

	private String locationId = "443";
	private String apiVersion = "1.0.0";
	private String userName = "Dinesh";
	private String paswd = "Dinesh";
	private Response response;

	@Test
	public void testGetValidUsersByLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = notes.getNotesId("", "", apiVersion, "", "", locationId, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testGetValidUsersForEmptyLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = notes.getNotesId("", "", apiVersion, "", "", "", token);
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testPostNotes() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String notesData = "";
		try {
			notesData = FileUtils.readFileToString(new File("test/data/postNotes.json"));
		} catch (IOException e) {
			printException(e, "testPostNotes");
		}
		response = notes.postNotes("", "", "", apiVersion, "", notesData, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testPostNotesForEmptyRequest() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = notes.postNotes("", "", "", apiVersion, "", "", token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		
	}

	@Test
	public void testPutNotes() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String notesData = "";
		try {
			notesData = FileUtils.readFileToString(new File("test/data/putNotes.json"));
		} catch (IOException e) {
			printException(e, "testPutNotes");
		}
		response = notes.notesUpdate("", "", "", apiVersion, "", notesData, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testPutNotesForEmptyRequest() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = notes.notesUpdate("", "", "", apiVersion, "", "", token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
	}
	

	@Test
	public void testGetNotesForLocation() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = notes.getNotesForLocation("", "", apiVersion, "", "", locationId, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertNotNull(responseObj);
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());

	}

	@Test
	public void testGetNotesForLocationEmptyLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		try {
			response = notes.getNotesForLocation("", "", apiVersion, "", "", "", token);
			fail("Exception was expected due to empty location id");

		} catch (TecnicasException te) {
			printException(te, "testGetNotesForLocationEmptyLocationId");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}
	}

	@Test
	public void testGetNotesForLocationInvalidLocationId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		try {
			response = notes.getNotesForLocation("", "", apiVersion, "", "", "abc", token);
			fail("Exception was expected due to invalid location id");

		} catch (TecnicasException te) {
			printException(te, "testGetNotesForLocationInvalidLocationId");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}
	}

	@Test
	public void testDeleteNotes() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		String idNote = "1";
		response = notes.deleteNotes("", "", "", "", apiVersion, idNote, token);
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}

	@Test
	public void testDeleteNotesEmptyNoteId() {
		try {
			String token = URLUtilityTest.getSORToken(userName, paswd);
			String idNote = "";
			response = notes.deleteNotes("", "", "", "", apiVersion, idNote, token);
			fail("Exception was expected due to empty note id");
		} catch (TecnicasException te) {
			printException(te, "testDeleteNotesEmptyNoteId");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}
	}

	@Test
	public void testDeleteNotesInvalidNoteId() {
		try {
			String token = URLUtilityTest.getSORToken(userName, paswd);
			String idNote = "abc";
			response = notes.deleteNotes("", "", "", "", apiVersion, idNote, token);
			fail("Exception was expected due to invalid note id");
		} catch (TecnicasException te) {
			printException(te, "testDeleteNotesInvalidNoteId");
			assertEquals(te.getStatusCode(), ServiceConstants.ERROR_CODE_OK);
			assertEquals(te.getErrorCode(), ServiceConstants.ERROR_CODE_UNDEFINEDISSUE);
			assertNotNull(te.getErrorDetailedMessage());
		}
	}

	@Test
	public void testGenerateDeleteNotesURL() {
		String idNote = "1,2";
		String generatedIdNote = notes.generateDeleteNotesURL(idNote);
		assertTrue(generatedIdNote.startsWith("idNote[0]="));
		assertEquals(generatedIdNote, "idNote[0]=1&idNote[1]=2");
		idNote = "1";
		generatedIdNote = notes.generateDeleteNotesURL(idNote);
		assertTrue(generatedIdNote.startsWith("idNote[0]="));
		assertEquals(generatedIdNote, "idNote[0]=1");
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
