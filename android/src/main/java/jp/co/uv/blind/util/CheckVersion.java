package jp.co.uv.blind.util;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

//import jp.co.uv.common.LOG;
//import jp.co.uv.common.LogType;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class CheckVersion {
	private static final String reg = "<div class=\"content\" itemprop=\"softwareVersion\"> ([0-9].)*[0-9]*  </div>";
	
	public static boolean isPackageInstalled(PackageManager pm, String packageName) {
        try {
            PackageInfo pInfo = pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return false;
        }
        return true;
	}

	public static String getVersion(PackageManager pm, String packageName) {
		try {
			PackageInfo pInfo = pm.getPackageInfo(packageName, 0);
			return pInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static String getOnlineVersion() throws ClientProtocolException, IOException {
//		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000); // 3s max for connection
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000); // 4s max to get data
//		HttpClient httpclient = new DefaultHttpClient(httpParameters);
//		HttpGet httpget = new HttpGet("https://play.google.com/store/apps/details?id=com.google.android.tts"); // Set the action you want to do
//		HttpResponse response = httpclient.execute(httpget); // Execute it
//		HttpEntity entity = response.getEntity();
//		InputStream is = entity.getContent(); // Create an InputStream with the response
//		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//		StringBuilder sb = new StringBuilder();
//		String line = null;
//		while ((line = reader.readLine()) != null) { // Read line by line
//			sb.append(line + "\n");
//		}
//
//		String resString = sb.toString(); // Result is here
//
//		Pattern p = Pattern.compile(reg);
//		Matcher m = p.matcher(resString);
//
//		String tag = "";
//		while (m.find()) {
//		    // Avoids throwing a NullPointerException in the case that you
//		    // Don't have a replacement defined in the map for the match
////			LOG.printLog(null, m.group(0), LogType.DEBUG);
//			tag = m.group(0);
//		}
//
//		is.close();
//		return tag;
//	}
}
