package com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes;

import javax.ws.rs.core.Response;

public interface NotesAdapter {

	Response getNotesId(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String locationId, String token);

	Response getNotesForLocation(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String locationId, String token);

	Response postNotes(String contentType, String acceptLanguage, String userAgent, String apiVersion, String loggedInUser, String notesData, String token);

	Response notesUpdate(String contentType, String acceptLanguage, String userAgent, String apiVersion, String loggedInUser, String notesData, String authToken);

	Response deleteNotes(String contentType, String acceptLanguage, String userAgent, String apiVersion, String loggedInUser, String idNote, String token);

}