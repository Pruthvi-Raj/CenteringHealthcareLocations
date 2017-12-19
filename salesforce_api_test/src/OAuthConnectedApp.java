

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;  
import org.apache.http.HttpEntity;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.message.BasicNameValuePair;  

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import chi.bean.Site;
import chi.bean.SiteList;
import chi.bean.State;


	@WebServlet(name = "oauth", urlPatterns = { "/oauth/*", "/oauth" }, initParams = {
		// clientId is 'Consumer Key' in the Remote Access UI
//**Update with your own Client ID
	@WebInitParam(name = "clientId", value = "3MVG9Nc1qcZ7BbZ3G0XGahIoMx2K.TmsGHYxLxzurJVr5ytotx28IbH.1EwejkRmcRDS.ii.IswsurVgzmnYp"),
		// clientSecret is 'Consumer Secret' in the Remote Access UI
//**Update with your own Client Secret
	@WebInitParam(name = "clientSecret", value = "3811034279376601461"),
		// This must be identical to 'Callback URL' in the Remote Access UI
	@WebInitParam(name = "location", value = "A"),
//**Update with your own URI
	@WebInitParam(name = "redirectUri", value = "http://localhost:8080/centeringhealthcare.org_locations/oauth/_callback"),
	@WebInitParam(name = "environment", value = "https://test.salesforce.com"), })
	
/**
 * Servlet parameters
 * @author itintern
 *
 */
public class OAuthConnectedApp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String INSTANCE_URL = "INSTANCE_URL";
	private static final String STATE = "STATE";
	
	
	
	private String clientId = null;
	private String clientSecret = null;
	private String redirectUri = null;
	private String environment = null;
	private String authUrl = null;
	private String tokenUrl = null;

	public void init() throws ServletException {
		clientId = this.getInitParameter("clientId");
		clientSecret = this.getInitParameter("clientSecret");
		redirectUri = this.getInitParameter("redirectUri");
		environment = this.getInitParameter("environment");

		try {
			authUrl = environment
					+ "/services/oauth2/authorize?response_type=code&client_id="
					+ clientId + "&redirect_uri="
					+ URLEncoder.encode(redirectUri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServletException(e);
		}

		tokenUrl = environment + "/services/oauth2/token";
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	  throws ServletException, IOException {
		String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);
		
		
		String state = request.getParameter("myField");
		
		System.out.println("Hi there"+state);
		
		if (accessToken == null) {
			String instanceUrl = null;

			if (request.getRequestURI().endsWith("oauth")) {

				// we need to send the user to authorize
				response.sendRedirect(authUrl);
				return;
			} else {
				System.out.println("Auth successfull - got callback");

				String code = request.getParameter("code");


				// Create an instance of HttpClient.
				CloseableHttpClient httpclient = HttpClients.createDefault();  

				try{
					// Create an instance of HttpPost.  
					HttpPost httpost = new HttpPost(tokenUrl);  

					// Adding all form parameters in a List of type NameValuePair  

					List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
					nvps.add(new BasicNameValuePair("code", code));  
					nvps.add(new BasicNameValuePair("grant_type","authorization_code")); 
					nvps.add(new BasicNameValuePair("client_id", clientId)); 
					nvps.add(new BasicNameValuePair("client_secret", clientSecret)); 
					nvps.add(new BasicNameValuePair("redirect_uri", redirectUri)); 

					httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));


					// Execute the request.  
					CloseableHttpResponse closeableresponse = httpclient.execute(httpost);  
					System.out.println("Response Status line :" + closeableresponse.getStatusLine());  
					try {  
						// Do the needful with entity.  
						HttpEntity entity = closeableresponse.getEntity(); 
						InputStream rstream = entity.getContent();
						JSONObject authResponse = new JSONObject(
								new JSONTokener(rstream));
						
						accessToken = authResponse.getString("access_token");
						instanceUrl = authResponse.getString("instance_url");
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {  
						// Closing the response  
						closeableresponse.close();  
					}  
				} finally {  
					httpclient.close();  
				}  


			}

			
			
			
			
			
			// Set a session attribute so that other servlets can get the access
			// token
			request.getSession().setAttribute(ACCESS_TOKEN, accessToken);
			
			
			
			// We also get the instance URL from the OAuth response, so set it
			// in the session too
			request.getSession().setAttribute(INSTANCE_URL, instanceUrl);
		}
		State s = new State();
		Site st = new Site();
		SiteList stList = new SiteList();

		if(state != null){
			
			
			s.setName(state);
			System.out.println("This is after setting attribute" + s.getName());
			request.setAttribute("State", s);
		}else{
			request.setAttribute("State", null
					
					
					);
		}
		
		
		
		RequestDispatcher rd = request.getRequestDispatcher("/SiteDetails");
		rd.forward(request, response);
		
		//response.sendRedirect(request.getContextPath() + "/SiteDetails");
	}
}