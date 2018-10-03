package com.ibm.mobilefirst.industrial.materialsinspect.adapter.User;

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
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.demo.data.UserDataAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.utility.URLUtilityTest;

@RunWith(MockitoJUnitRunner.class)
public class UserAdapterTest {

	static Logger logger = Logger.getLogger(UserAdapterTest.class.getName());

	@InjectMocks
	private UserDataAdapter user = new UserDataAdapter();
	
	private String locationId = "443";
	private String apiVersion = "1.0.0";
	private String userId = "9";
	private String visitId = "29";
	private String userName = "Dinesh";
	private String paswd = "Dinesh";
	private Response response;
	
	@Test
	public void testUserById() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = user.getUserById("", "","",apiVersion,0,0, userId, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testUserByIdForEmptyUserId() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = user.getUserById("", "","",apiVersion,0,0, "", token);
		assertEquals(500, response.getStatus());
	}
	
	@Test
	public void testRequestById() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = user.getRequestById("", "","","",apiVersion,0,0, userId, token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testVisitSkeleton() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = user.getVisitSkeleton("", "","","",apiVersion,userId,"", token);
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(200, response.getStatus());
		assertNotEquals(500, response.getStatus());
	}
	
	@Test
	public void testVisitSkeletonWithEmptyToken() {
		String token = URLUtilityTest.getSORToken(userName, paswd);
		response = user.getVisitSkeleton("", "","","",apiVersion,userId,"", "");
		JSONObject responseObj = (JSONObject) response.getEntity();
		assertEquals(403, response.getStatus());
	}
}
