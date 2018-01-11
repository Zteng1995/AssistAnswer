package com.zteng.assist.answer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;
import javax.xml.ws.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.ImageUtil;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class CDDemo {

	/*
	 * 百度百科 百度一下 百度知道 bing
	 * 
	 * 回车 重置运行 排版 修改
	 * 
	 * http://htpmsg.jiecaojingxuan.com/msg/current,timeout=5).text
	 */

	public static final String ADB_BASE = "adb";// mac 请换成 adb 的全路径

	// 这里请自己去申请 百度OCR 每天免费500次
	public static final String APP_ID = "10666288";
	public static final String API_KEY = "8WMShO94hGT7qHpC3BIQZn4d";
	public static final String SECRET_KEY = "Oop0dnWzMgkcukyXUgw63uzTrpKA9IpU";

	public static String access_token = "";

	public static String current_act = ADB_BASE + " shell \"dumpsys activity | grep mFocusedActivity\"";

	public static String cmd_screenshot = ADB_BASE + " shell screencap /sdcard/screenshot.png";

	public static String cmd_pull = ADB_BASE + " pull /sdcard/screenshot.png  D:/Users/Administrator/Desktop/cddh";

	public static String devices = ADB_BASE + " devices";

	public static long startTime = 0L;
	public static OkHttpClient client;

	public static void main(String[] args) {

		try {

			/**/
			System.out.println(" start.. ");

			client = new OkHttpClient.Builder().connectTimeout(1000 * 2, TimeUnit.MILLISECONDS)
					.readTimeout(1000 * 3, TimeUnit.MILLISECONDS).writeTimeout(1000 * 3, TimeUnit.MILLISECONDS).build();
			String exeCmd = exeCmd(devices);

			System.out.println("查看当前连接的设备 ： " + exeCmd);

			// String currentact = exeCmd(current_act);
			// System.out.println("查看当前ACT ： " + currentact);

			while (true) {
				System.out.println("请稍后.. ");
				startTime = System.currentTimeMillis();
				 String jt = exeCmd(cmd_screenshot);
				 String jt_pull = exeCmd(cmd_pull);
				// System.out.println("截图 pull ： " + jt_pull);
				System.out.println("---------------------开始请求--------------------- ");
				ImageUtils.cutPNG(new FileInputStream("D:\\Users\\Administrator\\Desktop\\cddh\\screenshot.png"),
						new FileOutputStream("D:\\Users\\Administrator\\Desktop\\cddh\\screenshot_2.png"), 0, 230, 1080,
						980);
				System.out.println("图片裁剪完毕");
				getTokenAndToOcr();
				Scanner scanner = new Scanner(System.in);
				String nextLine = scanner.nextLine();
				if (nextLine.equals("exit")) {
					break;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void getTokenAndToOcr() {

		if (access_token.length() > 0) {
			System.out.println("已有 token ");
			toOcr();

		} else {
			System.out.println("获取最新的 token ");
			Request request = new Request.Builder()
					.url("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&" + "client_id="
							+ API_KEY + "&client_secret=" + SECRET_KEY + "&")
					.addHeader("content-type", "application/json;charset:utf-8").build();
			client.newCall(request).enqueue(new okhttp3.Callback() {

				@Override
				public void onResponse(Call arg0, okhttp3.Response arg1) throws IOException {
					// stub
					ResponseBody body = arg1.body();
					String string = body.string();
					// System.out.println(string);
					JSONObject jsonObject = new JSONObject(string);
					access_token = jsonObject.getString("access_token");

					System.out.println(access_token);
					toOcr();
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					// arg1.printStackTrace();
				}
			});

		}
	}

	public static void toOcr() {

		String imageStr = getImageStr("D:\\Users\\Administrator\\Desktop\\cddh\\screenshot_2.png");

		// System.out.println(imageStr);

		// System.out.println("encode " + URLEncoder.encode(imageStr));

		RequestBody create = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
				"image=" + URLEncoder.encode(imageStr));

		Request request = new Request.Builder()
				.url("https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?" + "access_token=" + access_token + "&")
				.addHeader("content-type", "application/x-www-form-urlencoded").post(create).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, okhttp3.Response arg1) throws IOException {
				// TODO Auto-generated method stub
				try {
					String issue = "";
					ArrayList<String> arrayList = new ArrayList<String>();

					ResponseBody body = arg1.body();
					String string = body.string();
					System.out.println(string);
					JSONObject jsonObject = new JSONObject(string);
					JSONArray jsonArray = jsonObject.getJSONArray("words_result");
					StringBuffer stringBuffer = new StringBuffer();
					boolean iswh = false;
					int itemCount = 0;
					for (int x = 0; x < jsonArray.length(); x++) {
						JSONObject jsonObjectitem = (JSONObject) jsonArray.get(x);
						String string2 = jsonObjectitem.getString("words");
						if (iswh) {
							stringBuffer.append(itemCount + ".");
							arrayList.add(string2);
							itemCount++;
						}
						stringBuffer.append(string2);

						if (string2.contains("?")) {
							iswh = true;
							issue = stringBuffer.toString();
							itemCount++;
						}
					}

					// TODO 整理下数据

					System.out.println("-------------------- 百度OCR 结果 ----------------------\n");
					// String result = stringBuffer.toString();

					System.out.println(issue);
					System.out.println(arrayList.toString());

					if (issue.length() <= 0) {
						System.out.println("-------------------- 问题有误 ----------------------\n");
					} else {
						
						if(baiDuZhiDaoRun == null){
							 baiDuZhiDaoRun = new BaiDuZhiDaoRun(issue, arrayList);
						}else{
							baiDuZhiDaoRun.reSet(issue, arrayList);
						}
						
						if(bingRun == null){
							bingRun = new BingRun(issue, arrayList);
						}else{
							bingRun.reSet(issue, arrayList);
						}
						
						if(baiDuSSRun == null){
							baiDuSSRun = new BaiDuSSRun(issue, arrayList);
						}else{
							baiDuSSRun.reSet(issue, arrayList);
						}
						
					
						baiDuZhiDaoRun.run();
						baiDuSSRun.run();
						
						bingRun.run();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				// arg1.printStackTrace();
			}
		});
	}
	static BaiDuZhiDaoRun baiDuZhiDaoRun;
	static	BingRun bingRun;
	static	BaiDuSSRun baiDuSSRun;
	public static String getImageStr(String imgFile) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(imgFile);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加密
		return Base64Util.encode(data);
	}

	public static String exeCmd(String commandStr) {
		String reStr = "";
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec(commandStr);
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			reStr = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return reStr;
	}
}
