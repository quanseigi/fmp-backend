package com.fmp.backend.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class JwtTokenUtil {	
	public static String getToken() {
		String header = getAuthorizationHeader();
		if (header != null)
			return getAuthorizationHeader().split(" ")[1];
		return null;
	}

	public static String getAuthorizationHeader() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		return request.getHeader("Authorization");
	}
}
