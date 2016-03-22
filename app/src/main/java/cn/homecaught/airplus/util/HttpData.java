/**  
 * @Package net.zhomi.negotiation.view.utils 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yangshouzhi
 * @date 2015-7-15 下午10:58:34 
 */
package cn.homecaught.airplus.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: HttpData
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yangshouzhi
 * @date 2015-7-15 下午10:58:34
 */
public class HttpData {
	private static final int TIMEOUT_CONNECTION = 5000;
	private static final int TIMEOUT_SO = 10000;
	/** 测试url **/ 

	public static final String FAKE_SERVER = "http://139.196.40.169/api/index/";
	public static final String 	OUTDOOR_DATA = "http://www.pm25.com/city/";
	//	 // /** 正式的地址url **/
	public static final String BASE_URL = "http://139.196.40.169/";

	public static final String CONNECTION_ERROR_JSON = "{\"status\":0,\"error\":\"%s\"}";
	public static final String CONNECTION_ERROR_URL = "{\"status\":0,\"error\":\"请求地址无效!\"}";

	/**
	 * HttpPost请求
	 * 
	 * @param url
	 *
	 * @return
	 */
	private static String post(String url, List<NameValuePair> nameValuePairs) {
		SystemUtils.print("POST--URL:" + url);
		SystemUtils.print("entity--:" + nameValuePairs);

		String strResult = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							TIMEOUT_CONNECTION);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SO);
			HttpPost post = new HttpPost(url);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					nameValuePairs, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
			strResult = EntityUtils.toString(httpResponse.getEntity());
		} catch (IllegalArgumentException e) {
			strResult = CONNECTION_ERROR_URL;
		} catch (Exception e) {
			Log.e("111", "result:" + strResult);
			strResult = String.format(CONNECTION_ERROR_JSON,
					e.getMessage() == null ? "" : e.getMessage());
		}
		SystemUtils.print("result:" + strResult);
		return strResult;
	}
	/**
	 * HttpPost请求
	 *
	 * @param url
	 *
	 * @return
	 */
	private static String get(String url, List<NameValuePair> nameValuePairs) {
		SystemUtils.print("POST--URL:" + url);
		SystemUtils.print("entity--:" + nameValuePairs);

		String strResult = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							TIMEOUT_CONNECTION);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SO);
			HttpGet get = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(get);
			strResult = EntityUtils.toString(httpResponse.getEntity());
			SystemUtils.print("code:" + httpResponse.getStatusLine());

		} catch (IllegalArgumentException e) {
			strResult = CONNECTION_ERROR_URL;
		} catch (Exception e) {
			Log.e("111", "result:" + strResult);
			strResult = String.format(CONNECTION_ERROR_JSON,
					e.getMessage() == null ? "" : e.getMessage());
			SystemUtils.print("result err:" + e.getMessage());
			e.printStackTrace();

		}
		SystemUtils.print("result:" + strResult);
		return strResult;
	}
	/**
	 * HttpPost请求
	 * 
	 * @param url
	 *
	 * @return
	 */
	private static String postWithHeader(String url,
			List<NameValuePair> nameValuePairs) {
		SystemUtils.print("POST--URL:" + url);
		SystemUtils.print("entity--:" + nameValuePairs);

		String strResult = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							TIMEOUT_CONNECTION);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SO);
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type",
					"multipart/form-data; boundary=----WebKitFormBoundaryoX42YtuZV3NnqQOy");
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					nameValuePairs, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
			strResult = EntityUtils.toString(httpResponse.getEntity());
		} catch (IllegalArgumentException e) {
			strResult = CONNECTION_ERROR_URL;
		} catch (Exception e) {
			strResult = String.format(CONNECTION_ERROR_JSON,
					e.getMessage() == null ? "" : e.getMessage());
			Log.e("111", "result:" + strResult);
			SystemUtils.print(e.getMessage());

		}
		SystemUtils.print("result:" + strResult);
		return strResult;
	}

	/**
	 * 登陆
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static String register(String email, String password, String schoolId) {
		String url = FAKE_SERVER + "reg";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		BasicNameValuePair lonParam = new BasicNameValuePair("email",
				email);
		BasicNameValuePair latParam = new BasicNameValuePair("password",
				password);

		BasicNameValuePair sParam = new BasicNameValuePair("school_id",
				schoolId);
		nvps.add(lonParam);
		nvps.add(latParam);
		nvps.add(sParam);
		return post(url, nvps);
	}

	/**
	 * 登陆
	 *
	 * @param email
	 * @param password
	 * @return
	 */
	public static String login(String email, String password) {
		String url = FAKE_SERVER + "login";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		BasicNameValuePair lonParam = new BasicNameValuePair("email",
				email);
		BasicNameValuePair latParam = new BasicNameValuePair("password",
				password);
		nvps.add(lonParam);
		nvps.add(latParam);
		return post(url, nvps);
	}

	/**
	 * 获取室外数据
	 *
	 * @return
	 */
	public static String getOutdoorData(String cityKey) {
		String url = OUTDOOR_DATA + cityKey + ".html";
		return get(url, null);

	}

	public  static String getIndoorData(){
		String url = FAKE_SERVER + "get_pm";
		return get(url, null);
	}

	public static String getUserInstruments(String uid) {
		String url = FAKE_SERVER + "get_instruments_by_user/" +uid;
		return get(url, null);
	}

	public static String getSchoolImages(String uid) {
		String url = FAKE_SERVER + "get_school/" +uid;
		return get(url, null);
	}

	public static String getCity() {
		String url = FAKE_SERVER + "get_cites";
		return get(url, null);
	}

	public static String getSchoolsByCity(String cityId) {
		String url = FAKE_SERVER + "get_schools_by_city/" + cityId;
		return get(url, null);
	}
	public static String getDeviceBySchool(String schoolId) {
		String url = FAKE_SERVER + "get_instruments_by_school/" + schoolId;
		return get(url, null);
	}

	/**
	 * 添加设备
	 *
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	public static String addUserDevice(String userId, String deviceId) {
		String url = FAKE_SERVER + "add_user_instrument";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		BasicNameValuePair lonParam = new BasicNameValuePair("user_id",
				userId);
		BasicNameValuePair latParam = new BasicNameValuePair("instrument_id",
				deviceId);
		nvps.add(lonParam);
		nvps.add(latParam);
		return post(url, nvps);
	}

	/**
	 * 删除设备
	 *
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	public static String delUserDevice(String userId, String deviceId) {
		String url = FAKE_SERVER + "del_user_instrument";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		BasicNameValuePair lonParam = new BasicNameValuePair("user_id",
				userId);
		BasicNameValuePair latParam = new BasicNameValuePair("instrument_id",
				deviceId);
		nvps.add(lonParam);
		nvps.add(latParam);
		return post(url, nvps);
	}

	/**
	 * 删除设备
	 *
	 * @param userId
	 * @param installationId
	 * @return
	 */
	public static String setInstallationId(String userId, String installationId) {
		String url = FAKE_SERVER + "set_installation_id";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		BasicNameValuePair lonParam = new BasicNameValuePair("user_id",
				userId);
		BasicNameValuePair latParam = new BasicNameValuePair("installation_id",
				installationId);
		nvps.add(lonParam);
		nvps.add(latParam);
		return post(url, nvps);
	}

}
