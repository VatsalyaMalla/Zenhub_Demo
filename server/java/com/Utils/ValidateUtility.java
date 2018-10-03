package com.Utils;


import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;


public class ValidateUtility {
	
	static Logger logger = Logger.getLogger(ValidateUtility.class.getName());

	public static boolean validateIds(String id){
		boolean result= false;
		if(StringUtils.isEmpty(id)){
			result = true;
		}
		return result;
	}
}
