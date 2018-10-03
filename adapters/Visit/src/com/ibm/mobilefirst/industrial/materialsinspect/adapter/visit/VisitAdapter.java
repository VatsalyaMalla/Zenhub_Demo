package com.ibm.mobilefirst.industrial.materialsinspect.adapter.visit;

import javax.ws.rs.core.Response;

public interface VisitAdapter {

	Response getSupportTeamMembersByVisitId(String contentType, String acceptLanguage, String userAgent, String loggedInUser,
			String apiVersion, Integer offset, Integer limit, String locationId,String token);


}
