package com.nattguld.http.sec.impl;

import java.util.Objects;

import javax.script.ScriptException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.nattguld.http.HttpClient;
import com.nattguld.http.js.Javascript;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.http.sec.ConnectionSecurity;
import com.nattguld.util.Misc;

/**
 * 
 * @author randqm
 *
 */

public class Cloudfare implements ConnectionSecurity {
	
	/**
	 * The cloudfare http response code.
	 */
	public static final int CLOUDFARE_RESPONSE_CODE = 503;

	
	@Override
	public boolean encountered(HttpClient c, Request request, RequestResponse r) throws Exception {
		return r.validate(CLOUDFARE_RESPONSE_CODE);
	}

	@Override
	public boolean bypass(HttpClient c, Request request, RequestResponse r) throws Exception {
		String url = request.getUrl();
		String baseUrl = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
		String domainName = baseUrl.split("/")[2];
		
		String resolveUrl = resolveUrl(r.getAsDoc(), domainName, baseUrl);
		
		if (Objects.isNull(resolveUrl)) {
			return false;
		}
		Misc.sleep(5500);
		
		r = c.dispatchRequest(new GetRequest(resolveUrl));
		
		if (r.validate(200)) {
			return true;
		}
		if (r.getCode() == 403) {
			//String captcha = new CaptchaService().recaptchaV2(c, "6LfBixYUAAAAABhdHynFUIMA_sa4s-XsJvnjtgB0", resolveUrl);
			//String url = "https://" + domainName + "/cdn-cgi/l/chk_captcha";
			
			System.err.println("CAPTCHA ON CLOUDFARE!");
			return false;
		}
		System.err.println("Unexpected response (" + r.getCode() + ")");
		return false;
	}


	@Override
	public String getName() {
		return "Cloudfare";
	}

	/**
	 * Resolves the cloudfare answer and retrieves the get request url to bypass.
	 * 
	 * @param doc The response document.
	 * 
	 * @param domainName The domain name.
	 * 
	 * @param baseUrl The base url.
	 * 
	 * @return The bypass url.
	 */
	private static String resolveUrl(Document doc, String domainName, String baseUrl) {
		try {
			Element jschlEl = doc.selectFirst("[name=jschl_vc]");
		
			if (jschlEl == null) {
				System.err.println("Failed to extract jsch element");
				return null;
			}
			String jschl_vc = jschlEl.attr("value");
			String pass = doc.selectFirst("[name=pass]").attr("value");
		
			Element scriptEl = doc.getElementsByTag("script").first();
		
			if (scriptEl == null) {
				System.err.println("Failed to extract script element");
				return null;
			}
			String scriptHtml = scriptEl.html();
			String content = scriptHtml.substring(scriptHtml.indexOf("setTimeout(function(){") + 22, scriptHtml.length()).trim();
			
			String part1 = content.substring(31, content.indexOf("};") + 2); //@INFO: The first equation initially added to the builder var
			
			String[] part1Split = part1.split("=");
			String objName = part1Split[0];
			String objKey = part1Split[1].substring(2, part1Split[1].indexOf(":") - 1);
			String objVar = objName + "." + objKey; //@INFO: The var of the object to use (name.key)
			
			String equationBlock = content.substring(content.indexOf(";" + objVar), content.indexOf("a.value")); //The equation block
			
			Javascript js = new Javascript("function test() { " + part1 + "" + equationBlock + " var aval = +" + objVar + ".toFixed(10); return aval;}");
			double equationResult = (double)js.executeFunction("test");
			//System.out.println(equationResult);
			double result = equationResult + domainName.length();
			//System.out.println(result);
			String formatResult = String.format("%.10f", result).replace(",", ".");

			return baseUrl + "/cdn-cgi/l/chk_jschl?jschl_vc=" + jschl_vc + "&pass=" + pass + "&jschl_answer=" + formatResult;
		
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			System.err.println("No such method (JS)");
			
		} catch (ScriptException ex) {
			ex.printStackTrace();
			System.err.println("Script (JS) exception");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Unknown exception occurred");
		}
		return null;
	}

}
