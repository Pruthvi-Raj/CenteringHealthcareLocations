import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import chi.bean.Site;
import chi.bean.SiteList;
import chi.bean.State;
import chi.bean.StateList;

@WebServlet(urlPatterns = { "/SiteDetails" })
/**
 * Demo for Connect App/REST
 * 
 * @author itintern
 *
 */
public class ConnectedAppREST extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String INSTANCE_URL = "INSTANCE_URL";
	// private static final String STATE = "STATE";

	private void showAccounts(String instanceUrl, String accessToken, String state, SiteList siteList,
			PrintWriter writer) throws ServletException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet();

		// add key and value
		httpGet.addHeader("Authorization", "OAuth " + accessToken);

		try {

				System.out.println("Pruthvi you have selected " + state);

				URIBuilder builder = new URIBuilder(instanceUrl + "/services/data/v30.0/query");
				builder.setParameter("q",
						"SELECT Name, Id, Location_Street__c, Location_City__c, Website, Phone, Approval__c,"
								+ " Active_Models__c, Of_CenteringPregnancy_Models__c, "
								+ "Of_CenteringParenting_Models__c, Membership_Status2__c, "
								+ "(SELECT Name FROM Models__r WHERE State__c = 'Active' AND (Name = 'CenteringPregnancy' OR Name = 'CenteringParenting'))  "
								+ "from Account WHERE  Location_State_Province__c='" + state + "'");

				httpGet.setURI(builder.build());

				CloseableHttpResponse closeableresponse = httpclient.execute(httpGet);
				System.out.println("Response Status line showAccounts:" + closeableresponse.getStatusLine());

				if (closeableresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// Now lets use the standard java json classes to work with
					// the
					// results
					try {

						// Do the needful with entity.
						HttpEntity entity = closeableresponse.getEntity();
						InputStream rstream = entity.getContent();
						JSONObject authResponse = new JSONObject(new JSONTokener(rstream));
						// System.out.println("Query response: " +
						// authResponse.toString(2));

						writer.write(authResponse.getInt("totalSize") + " record(s) returned\n\n");

						JSONArray results = authResponse.getJSONArray("records");

						System.out.println(results);

						for (int i = 0; i < results.length(); i++) {
							Site site = siteList.addSite();

							String Id = results.getJSONObject(i).getString("Id");
							String name = results.getJSONObject(i).getString("Name");
							String city = String.valueOf(results.getJSONObject(i).get("Location_City__c"));
							String address = String.valueOf(results.getJSONObject(i).get("Location_Street__c"));
							String url = String.valueOf(results.getJSONObject(i).get("Website"));
							// String address1 =
							// String.valueOf(results.getJSONObject(i).getString("Location_City__c"));
							String phone = String.valueOf(results.getJSONObject(i).get("Phone"));
							String approve = String.valueOf(results.getJSONObject(i).get("Approval__c"));
							String modelName = String.valueOf(results.getJSONObject(i).get("Models__r"));
							String newModelName = modelName.replace("\"", "");
							String newModelName1 = newModelName.replace("}", "");
							String newModelName2 = newModelName1.replace("]", "");
							String newModelName3 = newModelName2.replace("Name:", "");
							String[] words = newModelName3.split(",");

							String finalModelName = "";
							for (String w : words) {
								if (w.equals("CenteringPregnancy")) {
									finalModelName = "CenteringPregnancy";
									System.out.println(finalModelName + name);
								} else {
									finalModelName = finalModelName + " ";
								}
								if (w.equals("CenteringParenting")) {
									finalModelName = finalModelName + "  " + "CenteringParenting";
									System.out.println(finalModelName + name + "\n");
									break;
								}
								System.out.println(
										"Complete Model---" + finalModelName + "       Site Name ---  " + name);
							}

							System.out.println("Ultimate for site  " + name + "   " + finalModelName);

							site.setSiteId(Id);
							site.setSiteName(name);
							site.setAddress(address);
							site.setUrl(url);
							site.setSitePhone(phone);
							site.setApproval(approve);
							site.setFinalModelname(finalModelName);
							// siteList.setArrayOfSite();

							// siteList.setArrayOfSite();
							writer.write(results.getJSONObject(i).getString("Id") + ", "
									+ results.getJSONObject(i).getString("Name") + "\n");
						}
						writer.write("\n");
					} catch (JSONException e) {
						e.printStackTrace();
						throw new ServletException(e);
					}
				}
			
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			httpclient.close();
		}
	}

	// private String createAccount(String name, String instanceUrl,
	// String accessToken, PrintWriter writer) throws ServletException,
	// IOException {
	// String accountId = null;
	//
	// CloseableHttpClient httpclient = HttpClients.createDefault();
	//
	// JSONObject account = new JSONObject();
	//
	// try {
	// account.put("Name", name);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// throw new ServletException(e);
	// }
	//
	// HttpPost httpost = new HttpPost(instanceUrl+
	// "/services/data/v30.0/sobjects/Account/");
	//
	// httpost.addHeader("Authorization", "OAuth " + accessToken);
	//
	//
	// StringEntity messageEntity = new StringEntity( account.toString(),
	// ContentType.create("application/json"));
	//
	// httpost.setEntity(messageEntity);
	//
	// // Execute the request.
	// CloseableHttpResponse closeableresponse = httpclient.execute(httpost);
	// System.out.println("Response Status line create account :" +
	// closeableresponse.getStatusLine());
	//
	//// try {
	////
	//// writer.write("HTTP status " +
	// closeableresponse.getStatusLine().getStatusCode()
	//// + " creating account\n\n");
	////
	//// if (closeableresponse.getStatusLine().getStatusCode() ==
	// HttpStatus.SC_CREATED) {
	//// try {
	////
	//// // Do the needful with entity.
	//// HttpEntity entity = closeableresponse.getEntity();
	//// InputStream rstream = entity.getContent();
	//// JSONObject authResponse = new JSONObject(
	//// new JSONTokener(rstream));
	////
	////
	//// System.out.println("Create response: "
	//// + authResponse.toString(2));
	////
	//// if (authResponse.getBoolean("success")) {
	//// accountId = authResponse.getString("id");
	//// writer.write("New record id " + accountId + "\n\n");
	//// }
	//// } catch (JSONException e) {
	//// e.printStackTrace();
	//// // throw new ServletException(e);
	//// }
	//// }
	//// }
	//// finally {
	//// httpclient.close();
	//// }
	//
	// return accountId;
	// }

	private StateList showAccount(String instanceUrl, String accessToken, StateList stateList, PrintWriter writer)
			throws ServletException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet();

		// add key and value
		httpGet.addHeader("Authorization", "OAuth " + accessToken);

		try {
			URIBuilder builder = new URIBuilder(instanceUrl + "/services/data/v30.0/query");
			builder.setParameter("q",
					"SELECT Account__r.Location_State_Province__c stateName, Name, COUNT(Id) total " + "from Certification__c "
							+ "WHERE State__c = 'Active' AND Name LIKE '%Centering%' AND Account__r.Location_State_Province__c != null "
							+ "GROUP BY Account__r.Location_State_Province__c, Name");

			httpGet.setURI(builder.build());

			CloseableHttpResponse closeableresponse = httpclient.execute(httpGet);
			System.out.println("Response Status line :" + closeableresponse.getStatusLine());
			if (closeableresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try {
					HttpEntity entity = closeableresponse.getEntity();
					InputStream rstream = entity.getContent();
					JSONObject authResponse = new JSONObject(new JSONTokener(rstream));

					System.out.println("Query response: " + authResponse.toString(2));

					writer.write(authResponse.getInt("totalSize") + " record(s) returned\n\n");

					JSONArray results = authResponse.getJSONArray("records");

					for (int i = 0; i < results.length(); i++) {
						State state = stateList.addState();

						String modName = results.getJSONObject(i).getString("Name");
						int total = (int) results.getJSONObject(i).get("total");
						String stName = String.valueOf(results.getJSONObject(i).getString("stateName"));
						

						System.out.println(stName + " has " + modName + " and no of sites "+total );

						state.setName(stName);
						state.setModName(modName);
						state.setTotalSites(total);
						
						writer.write(results.getJSONObject(i).get("total") + ", "
								+ results.getJSONObject(i).getString("Name") + "\n");
					}
					writer.write("\n");
				} catch (JSONException e) {
					e.printStackTrace();
					throw new ServletException(e);
				}
			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			httpclient.close();
		}
		return stateList;
	}

	/*
	 * private void updateAccount(String accountId, String newName, String city,
	 * String instanceUrl, String accessToken, PrintWriter writer) throws
	 * ServletException, IOException { CloseableHttpClient httpclient =
	 * HttpClients.createDefault();
	 * 
	 * JSONObject update = new JSONObject();
	 * 
	 * try { update.put("Name", newName); update.put("BillingCity", city); }
	 * catch (JSONException e) { e.printStackTrace(); throw new
	 * ServletException(e); }
	 * 
	 * HttpPost httpost = new HttpPost(instanceUrl +
	 * "/services/data/v30.0/sobjects/Account/"
	 * +accountId+"?_HttpMethod=PATCH");
	 * 
	 * 
	 * httpost.addHeader("Authorization", "OAuth " + accessToken);
	 * 
	 * 
	 * StringEntity messageEntity = new StringEntity( update.toString(),
	 * ContentType.create("application/json"));
	 * 
	 * httpost.setEntity(messageEntity);
	 * 
	 * 
	 * 
	 * // Execute the request. CloseableHttpResponse closeableresponse =
	 * httpclient.execute(httpost); System.out.println("Response Status line :"
	 * + closeableresponse.getStatusLine());
	 * 
	 * 
	 * try { writer.write("HTTP status " +
	 * closeableresponse.getStatusLine().getStatusCode() + " updating account "
	 * + accountId + "\n\n"); } finally { httpclient.close();
	 * 
	 * } }
	 * 
	 * private void deleteAccount(String accountId, String instanceUrl, String
	 * accessToken, PrintWriter writer) throws IOException { CloseableHttpClient
	 * httpclient = HttpClients.createDefault();
	 * 
	 * HttpDelete delete = new HttpDelete(instanceUrl +
	 * "/services/data/v30.0/sobjects/Account/" + accountId);
	 * 
	 * delete.setHeader("Authorization", "OAuth " + accessToken);
	 * 
	 * // Execute the request. CloseableHttpResponse closeableresponse =
	 * httpclient.execute(delete); System.out.println("Response Status line :" +
	 * closeableresponse.getStatusLine());
	 * 
	 * try { writer.write("HTTP status " +
	 * closeableresponse.getStatusLine().getStatusCode() + " deleting account "
	 * + accountId + "\n\n"); } finally { delete.releaseConnection(); } }
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();

		

		String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);

		String instanceUrl = (String) request.getSession().getAttribute(INSTANCE_URL);

		if (accessToken == null) {
			writer.write("Error - no access token");
			return;
		}

		writer.write("We have an access token: " + accessToken + "\n" + "Using instance " + instanceUrl + "\n\n");

		State s = (State) request.getAttribute("State");
		
		
		if(s == null){
		
		StateList stateList = new StateList();
		StateList stateList1 = showAccount(instanceUrl, accessToken, stateList, writer);

		
		
		for(State stt: stateList1.getStatesList()){
			  System.out.println("This is the array list");
			  System.out.println(stt.getName());
			  System.out.println(stt.getModName()); 
			  }
			  
			  System.out.println("My print statement" +
			  stateList1.getStatesList());
		
		
		request.setAttribute("StateList", stateList1);
			  
		RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
		rd.forward(request, response);
		} 
		
		
		if (s != null) {
			
			

			String state =  s.getName();
 
			//String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);

			//String instanceUrl = (String) request.getSession().getAttribute(INSTANCE_URL);

			writer.write("We have an access token: " + accessToken + "\n" + "Using instance " + instanceUrl + " State "
					+ state + "\n\n");

			SiteList siteList = new SiteList();
			showAccounts(instanceUrl, accessToken, state, siteList, writer);
			/*
			 * for(Site site: siteList.getSitesList()){
			 * System.out.println("This is the array list");
			 * System.out.println(site.getUrl());
			 * System.out.println(site.getSiteName()); }
			 * 
			 * System.out.println("My print statement" +
			 * siteList.getSitesList());
			 */

			request.setAttribute("SiteList", siteList);
			request.setAttribute("State", s);

			RequestDispatcher rd2 = request.getRequestDispatcher("/SiteDetails.jsp");
			rd2.forward(request, response);
		} 
	}

}
