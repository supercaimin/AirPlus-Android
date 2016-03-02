/**  
* @Package net.zhomi.negotiation.view.utils 
* @Description: TODO(��һ�仰�������ļ���ʲô) 
* @author yangshouzhi
* @date 2015-7-15 ����3:13:29 
*/ 
package cn.homecaught.airplus.util;

import android.os.Handler;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/** 
 * @ClassName: HttpUtils 
 * @Description: 网络请求
 * @author yangshouzhi
 * @date 2015-7-15 
 */
public class HttpUtils {

	private static final int TIMEOUT_CONNECTION = 5000;
	private static final int TIMEOUT_SO = 10000;

	public static final String CONNECTION_ERROR_JSON = "{\"count\":0,\"state\":0,\"message\":\"%s\"}";
	public static final String CONNECTION_ERROR_URL = "{\"status\":0,\"error\":\"请求地址无效!\"}";
	
	public static InputStream doGetInputStream(String strUrl) {
		HttpResponse response = doConnection(strUrl);
		if (isResponseAvailable(response)) {
			try {
				return response.getEntity().getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String doGetString(String strUrl)
			throws UnsupportedEncodingException {
		String strResult = null;
		HttpResponse response = doConnection(strUrl);
		if (isResponseAvailable(response)) {
			try {
				strResult = EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
				strResult = CONNECTION_ERROR_JSON;
			}
		} else {
			strResult = CONNECTION_ERROR_JSON;
		}
		return strResult;
	}

	public static String doPost(String strUrl,
			List<NameValuePair> listNameValuePair) {
		return doPost(strUrl, listNameValuePair, null, null, null);
	}

	public static String doPost(String strUrl,
			List<NameValuePair> listNameValuePair, List<String> mListImagePath,
			ProgressRunnable pr, Handler handler) {

		if (mListImagePath != null) {
			final int count = mListImagePath.size();
			final FormFile[] formFiles = new FormFile[count];
			for (int i = 0; i < count; i++) {
				final File file = new File(mListImagePath.get(i));
				formFiles[i] = new FormFile(file.getName() + ".jpg", file,
						"file", "application/octet-stream");
			}
			final int nIndex = listNameValuePair.size();
			final HashMap<String, String> params = new HashMap<String, String>();
			for (int i = 0; i < nIndex; i++) {
				params.put(listNameValuePair.get(i).getName(),
						listNameValuePair.get(i).getValue());
			}

			return post(strUrl, listNameValuePair, mListImagePath);
		}

		return doPost(strUrl, listNameValuePair, null, null);
	}

	public static String post2(String url,
			List<NameValuePair> listNameValuePair) {
		String strResult = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT,
					TIMEOUT_CONNECTION);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SO);
			HttpPost post = new HttpPost(url);

			post.setEntity(new UrlEncodedFormEntity(listNameValuePair,
					HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(post);
			if (isResponseAvailable(httpResponse)) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = String.format(CONNECTION_ERROR_JSON, "连接失败，错误码" + httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			strResult = String.format(CONNECTION_ERROR_JSON, e.getMessage());
		}
		SystemUtils.print("result:" + strResult);
		return strResult;
	}

	public static String doPost(String strUrl,
			List<NameValuePair> listNameValuePair, ProgressRunnable pr,
			Handler handler) {
		String strResult = null;
		HttpResponse response = null;
		try {
			final URI uri = new URI(strUrl);
			HttpPost httpPost = new HttpPost(uri);
			httpPost.addHeader("charset", HTTP.UTF_8);
			httpPost.setEntity(new UrlEncodedFormEntity(listNameValuePair,
					HTTP.UTF_8));
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params,
					TIMEOUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(params, TIMEOUT_SO);

			response = httpClient.execute(httpPost);

			if (isResponseAvailable(response)) {
				strResult = EntityUtils.toString(response.getEntity());
			} else {
				strResult = CONNECTION_ERROR_JSON;
			}
		} catch (Exception e) {
			strResult = String.format(CONNECTION_ERROR_JSON, e.getMessage());
		}
		return strResult;
	}

	public static String post(String url, List<NameValuePair> nameValuePairs,
			List<String> images) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(url);

		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (int index = 0; index < images.size(); index++) {
				File file = new File(images.get(index));
				entity.addPart("file", new FileBody(file));
			}
			for (NameValuePair nameValuePair : nameValuePairs) {
				entity.addPart(nameValuePair.getName(), new StringBody(
						nameValuePair.getValue()));
			}

			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CONNECTION_ERROR_JSON;
	}

	public static String doUpload(String strUrl, String strFilePath,
			ProgressRunnable progress, Handler handler) {
		InputStream is = null;
		OutputStream os = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();

			File file = new File(strFilePath);
			String strFilename = file.getName();

			StringBuffer sbPrefix = new StringBuffer();
			sbPrefix.append("\r\n")
					.append("Content-Disposition: form-data; name=\"pic_file\"; filename=\""
							+ strFilename + "\"\r\n")
					.append("Content-Type: " + "application/octet-stream"
							+ "\r\n").append("\r\n");

			StringBuffer sbSuffix = new StringBuffer();

			byte bytePrefix[] = sbPrefix.toString().getBytes("UTF-8");
			byte byteSuffix[] = sbSuffix.toString().getBytes("UTF-8");

			final long lContentLength = bytePrefix.length + file.length()
					+ byteSuffix.length;

			conn.setRequestMethod("POST");

			conn.setRequestProperty("Content-Length",
					String.valueOf(lContentLength));
			conn.setDoOutput(true);

			os = conn.getOutputStream();
			is = new FileInputStream(file);

			os.write(bytePrefix);

			byte[] buf = new byte[1024];
			int nReadBytes = 0;

			if (progress == null) {
				while ((nReadBytes = is.read(buf)) != -1) {
					os.write(buf, 0, nReadBytes);
				}

				os.write(byteSuffix);
			} else {
				long lUploadBytes = bytePrefix.length;
				while ((nReadBytes = is.read(buf)) != -1) {
					os.write(buf, 0, nReadBytes);
					lUploadBytes += nReadBytes;

					progress.mPercentage = (int) (lUploadBytes * 100 / lContentLength);
					handler.post(progress);
				}

				os.write(byteSuffix);

				progress.mPercentage = 100;
				handler.post(progress);
			}

		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					conn = null;
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					conn = null;
				}
			}
		}

		String strRet = null;

		if (conn != null) {
			try {
				InputStream isResponse = (InputStream) conn.getContent();
				if (isResponse != null) {
					int nRead = 0;
					byte buf[] = new byte[128];
					CharArrayBuffer bab = new CharArrayBuffer(128);
					while ((nRead = isResponse.read(buf)) != -1) {
						bab.append(buf, 0, nRead);
					}
					strRet = bab.substring(0, bab.length());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return strRet;
	}

	public static String doUpload(String strUrl, String strFilePath) {
		return doUpload(strUrl, strFilePath, null, null);
	}

	public static boolean doDownload(String strUrl, String strSavePath,
			ProgressRunnable progress, Handler handler, AtomicBoolean bCancel) {
		HttpResponse response = doConnection(strUrl);
		if (isResponseAvailable(response)) {
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = response.getEntity().getContent();
				fos = FileHelper.createFileOutputStream(strSavePath);
				if (fos != null) {
					final byte buf[] = new byte[1024];
					if (progress == null) {
						int lReadLength = 0;
						while ((lReadLength = is.read(buf)) != -1) {
							fos.write(buf, 0, lReadLength);
						}
					} else {
						long lDownloadLength = 0;
						int lReadLength = 0;
						final long lTotalLength = response.getEntity()
								.getContentLength();
						while (true) {
							if (bCancel != null && bCancel.get()) {
								File file = new File(strSavePath);
								file.delete();
								is.close();
								fos.close();
								return false;
							} else if ((lReadLength = is.read(buf)) != -1) {
								fos.write(buf, 0, lReadLength);
								lDownloadLength += lReadLength;
								progress.mPercentage = (int) (lDownloadLength * 100 / lTotalLength);
								handler.post(progress);
							} else {
								break;
							}
						}
					}
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				FileHelper.deleteFile(strSavePath);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
					if (fos != null) {
						fos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	private static boolean isResponseAvailable(HttpResponse response) {
		if (response != null
				&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return true;
		}
		return false;
	}

	private static HttpResponse doConnection(String strUrl) {
		HttpResponse response = null;
		try {
			final URI uri = new URI(strUrl);
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params,
					TIMEOUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(params, TIMEOUT_SO);

			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return response;
	}

	@SuppressWarnings("unused")
	private static class MultipartEntityEx extends MultipartEntity {
		public long mTransferredSize;

		public long mTotalSize;

		public ProgressRunnable mRunnable;
		public Handler mHandler;

		public MultipartEntityEx(HttpMultipartMode mode, ProgressRunnable run,
				Handler handler) {
			super(mode);
			mRunnable = run;
			mHandler = handler;
		}

		@Override
		public void writeTo(OutputStream outstream) throws IOException {
			super.writeTo(new CustomOutputStream(outstream));
		}

		private class CustomOutputStream extends FilterOutputStream {

			public CustomOutputStream(OutputStream out) {
				super(out);
			}

			@Override
			public void write(byte[] buffer, int offset, int length)
					throws IOException {
				super.write(buffer, offset, length);
				mTransferredSize += length;
				notifyProgress();
			}

			@Override
			public void write(int oneByte) throws IOException {
				super.write(oneByte);
				++mTransferredSize;
				notifyProgress();
			}

			protected void notifyProgress() {
				if (mHandler != null && mRunnable != null) {
					final int nPer = (int) (mTransferredSize * 100 / mTotalSize);
					if (mRunnable.mPercentage != nPer) {
						mRunnable.mPercentage = nPer;
						mHandler.post(mRunnable);
					}
				}
			}
		}
	}

	public static abstract class ProgressRunnable implements Runnable {
		private int mPercentage;

		public int getPercentage() {
			return mPercentage;
		}
	}
}
