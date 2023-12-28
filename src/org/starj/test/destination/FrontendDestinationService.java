package org.starj.test.destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * class DestinationServlet
 * 
 * http://frontend-server.com:8080/test/destination
 * 
 * Frontend 서버에서 Backend 서버로(server to server)로 호출하는 proxy 서비스
 * 
 */
public class FrontendDestinationService extends HttpServlet {
	
	private static final long serialVersionUID = 5427138025784827086L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
				
			boolean isDebugMode = false;
	
			StringBuilder builder = new StringBuilder();
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(int i = 0; i < cookies.length; i++) {
					Cookie c = cookies[i];
					builder.append(String.format("%s=%s;", c.getName(), c.getValue()));
				}
			}
			
			String url = getServletConfig().getInitParameter("destination-url");
			if(url == null)
				throw new ServletException("There are no destination-url for servlet init-param. please check the web.xml");
			
//			new URL("https", url, new sun.net.www.protocol.https.Handler());
	
			HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
//			HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Cookie", builder.toString());
			connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			
//			disableSslVerification();
	
			OutputStream connectionOutputStream = null;
			InputStream connectionInputStream = null;
			OutputStream responseOutputStream = null;
			try {
				
				String postData = toString(request.getInputStream(), "UTF-8");
				byte[] responseBuffer = postData.toString().getBytes("UTF-8");
				
				connectionOutputStream = connection.getOutputStream();
				connectionOutputStream.write(responseBuffer, 0, responseBuffer.length);
				
				response.setContentType("application/json;");
	
				connectionInputStream = connection.getInputStream();
				responseOutputStream = response.getOutputStream();
	
				byte[] buffer = new byte[1024];
				StringBuffer responseData = new StringBuffer();
				for(int r = 0; (r = connectionInputStream.read(buffer)) > -1; ){
					responseOutputStream.write(buffer, 0, r);
					if(isDebugMode){
						responseData.append(new String(buffer, 0, r, "UTF-8"));
					}
				}
				
				if(isDebugMode){
					System.out.println("Response Data ----------------------------------------------------");
					System.out.println(responseData.toString());
					System.out.println("Response Data ----------------------------------------------------");
				}
			} catch(Exception e) {
			    response.setStatus(connection.getResponseCode());
				response.setHeader("Status-Message", connection.getHeaderField("Status-Message"));
			} finally{
				if(connectionOutputStream != null){
					try{ connectionOutputStream.close(); } catch(Exception e){};
				}
				if(connectionInputStream != null){
					try{ connectionInputStream.close(); } catch(Exception e){};
				}
			}
		}catch(Exception e2) {
			response.getWriter().print(e2.getLocalizedMessage());
		}
	}

	private static String toString(InputStream inputStream, String charsetName) throws IOException {
		BufferedReader requestInputStreamBufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
		StringBuffer postData = new StringBuffer();
		String string;
		while((string = requestInputStreamBufferedReader.readLine()) != null){
			postData.append(string);
		}
		return postData.toString();
	}
	
	public void disableSslVerification() throws Exception {
		try {
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {
	        	new X509TrustManager() {
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					
					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
							throws CertificateException {
					}
					
					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
							throws CertificateException {
					}
				}
	        };
	
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session){
	                return true;
	            }
	        };
	
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
	        throw e;
	    } catch (KeyManagementException e) {
	        throw e;
	    }
	}
	
}

