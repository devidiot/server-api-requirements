package org.starj.test.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * CORS 허용하고자 할 경우 Filter를 사용한다.
 * 
 */
public class MyCorsFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
//		System.out.println("-------------------- Request Headers ---------------------");
//		Enumeration<String> headerNames = req.getHeaderNames();
//		while(headerNames.hasMoreElements()) {
//			String name = headerNames.nextElement();
//			System.out.println(String.format("%s: %s", name, req.getHeader(name)));
//		}
//		System.out.println("-------------------- Request Headers ---------------------");
		
		resp.addHeader("Access-Control-Allow-Origin", "http://frontend-server.com:8080");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Allow-Methods", "GET, POST");
		resp.addHeader("Access-Control-Allow-Headers", "Content-Type,Accept,Cookie");

//	     For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
	    if (req.getMethod().equals("OPTIONS")) {
	      resp.setStatus(HttpServletResponse.SC_ACCEPTED);
	      return;
	    }
	    
		chain.doFilter(request, response);
	}

}
