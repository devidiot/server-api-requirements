package org.starj.test.destination;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * class TargetServlet
 * 
 * http://backend-server.com:8080/test/service
 * 
 * web.xml에 구성된 서블릿 FrontendDestinationService의 destination-url로 지정된 서비스
 * 
 */
public class BackendBusinessService extends HttpServlet {
       
	private static final long serialVersionUID = -8021768119143434662L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		Cookie[] cookies = request.getCookies();
		String rs = "";
		if(cookies != null) {
			rs += "[";
			for(int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if(i > 0) rs += ", ";
				rs += String.format("{\"%s\" : \"%s\"}", c.getName(), c.getValue());
			}
			rs += "]";
			out.print(rs);
		}else {
			out.print("{\"result\": \"no cookies\"}");
		}
		
		if(true) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setHeader("Status-Message", "Not Valid Token");
		}
	}

}
