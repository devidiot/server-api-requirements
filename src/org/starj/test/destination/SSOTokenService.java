package org.starj.test.destination;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * class TokenIssuingServlet
 * 
 * http://frontend-server.com:8080/test/token
 */
public class SSOTokenService extends HttpServlet {

	private static final long serialVersionUID = 1674952757931664537L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int maxAge = 1000 * 60 * 60 * 24;
		String token = "1674952757931664537L";
		response.setHeader("Set-Cookie", "token="+token+";max-age="+maxAge+";path=/;HttpOnly=true;SameSite=Strict;");
		
//		response.addHeader("Set-Cookie", "token1=newToken1;max-age=2592000;path=/;HttpOnly=true;SameSite=None;");
//		response.addHeader("Set-Cookie", "token2=newToken2;max-age=2592000;path=/;HttpOnly=true;SameSite=Lax;");
//		
//		Cookie cookie = new Cookie("token3", "newToken");
//		cookie.setDomain("a.com");
//		cookie.setSecure(true);
//		cookie.setMaxAge(maxAge);
//		cookie.setPath("/");
//		cookie.setHttpOnly(true);
//		response.addCookie(cookie);
		
		response.getWriter().print("OK");
	}

}
