package consumindowebservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpExemplo {
	private final String USER_AGENT = "Mozilla/5.0";
	private static HttpExemplo http = new HttpExemplo();

	public static void main(String[] args) throws Exception {
		LoginOptions lp = getSession("sisint@yaman.com.br", "sisint2001");
		Project p = new Project();
		p.setSysId("P-54002");
		criaDespesaParaPlanilha(lp, p);
	}

	private static void criaDespesaParaPlanilha(LoginOptions lp, Project p) throws Exception {
		Expense despesa = new Expense();
		despesa.setDataQueOcorreu("2020-02-19");
		despesa.setValor(1000.0);
		despesa.setCodigoMoeda("BRL");
		String url = "/data/createAndRetrieve";
		http.sendPost(lp.getUrlBase() + url,
				"{'entity':{"
		+ "'Id':" + despesa.getId() + ","
						+ "'Description':" + despesa.getDescription() + ","
						+ "'DateIncurred':'" + despesa.getDataQueOcorreu() + "',"
						+ "'LocalAmount':{"
						+ "'currency':'" + despesa.getCodigoMoeda() + "',"
						+ "'value':'" + despesa.getValor() + "'}},"
						+ "'ExpenseSheet':'" + criaPlanilhaDeDespesasDoProjeto(p, lp).getExternalId() + "',"
						+ "'fields':['CreatedOn','ExternalID']}",
				"POST", lp);
	}

	private static ExpenseSheet criaPlanilhaDeDespesasDoProjeto(Project projeto, LoginOptions loginOptions)
			throws Exception {
		ExpenseSheet planilha = new ExpenseSheet();
		String url = "/data/createAndRetrieve";
		StringBuffer response = http.sendPost(loginOptions.getUrlBase() + url,
				"{'entity':{" + "'Id':" + planilha.getId() + "," + "'Description':" + planilha.getDescription() + ","
						+ "'Date':" + planilha.getDate() + "," + "'Project':'"
						+ consultaProjetoPor(projeto.getSysId(), loginOptions).getExternalId() + "'},"
						+ "'fields':['CreatedOn'," + "'SYSID']}",
				"POST", loginOptions);
		response.toString().lines().map(linha -> linha.split("\"")).forEach(str -> planilha.setExternalId(str[5]));
		return planilha;
	}

	private static Project consultaProjetoPor(String sysId, LoginOptions loginOptions) throws Exception {
		Project projeto = new Project();
		projeto.setSysId(sysId);
		String url = "/data/EntityQuery";
//		String url = u.getUrlDataCenterCorreto() + "/data/Query?q=SELECT * FROM Project WHERE SYSID = \"P-54001\"";
		StringBuffer response = http.sendPost(loginOptions.getUrlBase() + url,
				"{'typeName':'Project'," + "'fields':['Name','ExternalId','SYSID']," + "'where': {"
						+ "'leftExpression': { 'fieldName': 'SYSID' }," + "'operator': 'Equal',"
						+ "'rightExpression': { 'value' : '" + projeto.getSysId() + "'" + "}}}",
				"POST", loginOptions);
		response.toString().lines().map(linha -> linha.split("\"")).forEach(str -> projeto.setExternalId(str[5]));
		return projeto;
	}

	private static User defineUrlBase(User u) throws Exception {
		String url = "https://api2.clarizen.com/v2.0/services/authentication/getServerDefinition";
		StringBuffer response = http.sendPost(url, http.geraJson(new Gson(), u), "GET", null);
		response.toString().lines().map(linha -> linha.split("\"")).map(str -> new String(str[3]))
				.forEach(str -> u.setUrlDataCenterCorreto(str));
		return u;
	}

	private static String geraJson(Gson g, Object o) {
		Type tipo = new TypeToken<Object>() {
		}.getType();
		String json = g.toJson(o, tipo);
		return json;
	}

	private static LoginOptions getSession(String userName, String password) throws Exception {
		User u = new User();
		LoginOptions loginOptions = new LoginOptions();
		Gson g = new Gson();
		u.setUserName(userName);
		u.setPassword(password);
		String json = geraJson(g, u);
		defineUrlBase(u);
		String url = u.getUrlDataCenterCorreto() + "/authentication/login";
		StringBuffer response = http.sendPost(url, json, "GET", loginOptions);
		loginOptions = separaDadosDeSession(response);
		loginOptions.setUrlBase(u.getUrlDataCenterCorreto());
		return loginOptions;
	}

	private static LoginOptions separaDadosDeSession(StringBuffer response) {
		LoginOptions loginOptions = new LoginOptions();
		response.toString().lines().map(linha -> linha.split("\"")).map(str -> new String(str[3]))
				.forEach(str -> loginOptions.setSessionID(str));
		response.toString().lines().map(linha -> linha.split("\"")).map(str -> new String(str[7]))
				.forEach(str -> loginOptions.setUserId(str));
		response.toString().lines().map(linha -> linha.split("\"")).map(str -> new String(str[11]))
				.forEach(str -> loginOptions.setUserId(str));
		response.toString().lines().map(linha -> linha.split("\"")).map(str -> new String(str[19]))
				.forEach(str -> loginOptions.setLicenseType(str));
		return loginOptions;
	}

	// HTTP GET request
	private StringBuffer sendGet(String url, String method, LoginOptions loginOptions) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod(method);
		// add request header
		if (loginOptions != null) {
			con.setRequestProperty("Authorization", "Session " + loginOptions.getSessionID());
		} else {
			con.setRequestProperty("Authorization", "Session " + USER_AGENT);
		}
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		System.out.println("Response message: " + con.getResponseMessage());
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		return response;
	}

	private StringBuffer sendPost(String url, String urlParameters, String method, LoginOptions loginOptions)
			throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// add reuqest header
		con.setRequestMethod(method);
		con.setRequestProperty("Content-Type", "application/json");
//		con.setRequestProperty("User-Agent", USER_AGENT);
		if (loginOptions != null) {
			con.setRequestProperty("Authorization", "Session " + loginOptions.getSessionID());
		} else {
			con.setRequestProperty("Authorization", "Session " + USER_AGENT);
		}
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		System.out.println(con.getResponseMessage());

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
		return response;
	}

}