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
	 * { "access_token":
	 * "24.124708e8e73bcf1776f34b1d992395a0.2592000.1518147609.282335-10666288",
	 * "session_key":
	 * "9mzdCragJfXUiuZ0bgiO6F114nYRvnOMhVjt4Z0K9BDoNCsqg8pNHClk/N9z1zcJa3CPo2ARNaQL18kZXMEd41+fB96hPQ==",
	 * "scope":
	 * "public vis-ocr_ocr brain_ocr_scope brain_ocr_general brain_ocr_general_basic brain_ocr_general_enhanced vis-ocr_business_license brain_ocr_webimage brain_all_scope brain_ocr_idcard brain_ocr_driving_license brain_ocr_vehicle_license vis-ocr_plate_number brain_solution brain_ocr_plate_number brain_ocr_accurate brain_ocr_accurate_basic brain_ocr_receipt brain_ocr_business_license wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower bnstest_fasf lpq_开放 cop_helloScope ApsMis_fangdi_permission"
	 * , "refresh_token":
	 * "25.ae44dc5bc63070fc5b9328fa9de1e7e9.315360000.1830915609.282335-10666288",
	 * "session_secret": "03b4767023f4aaf193f3dd587e8ccf96", "expires_in":
	 * 2592000 }
	 * 
	 * 百度百科 百度一下 百度知道 bing
	 * 
	 * 回车 重置运行 排版 修改
	 * 
	 * http://htpmsg.jiecaojingxuan.com/msg/current,timeout=5).text
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public static final String APP_ID = "10666288";
	public static final String API_KEY = "8WMShO94hGT7qHpC3BIQZn4d";
	public static final String SECRET_KEY = "Oop0dnWzMgkcukyXUgw63uzTrpKA9IpU";
	public static String access_token = "";

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

	public static String current_act = "adb shell \"dumpsys activity | grep mFocusedActivity\"";

	public static String cmd_screenshot = "adb shell screencap /sdcard/screenshot.png";

	public static String cmd_pull = "adb pull /sdcard/screenshot.png  D:/Users/Administrator/Desktop/cddh";

	public static long startTime = 0L;
	public static OkHttpClient client;
	public static void main(String[] args) {

		try {
			
			/**/
			System.out.println(" start.. ");
			// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
			// 调用接口
			 client = new OkHttpClient.Builder().connectTimeout(1000 * 2, TimeUnit.MILLISECONDS)
					.readTimeout(1000 * 3, TimeUnit.MILLISECONDS).writeTimeout(1000 * 3, TimeUnit.MILLISECONDS).build();
			String devices = "adb devices";

			String exeCmd = exeCmd(devices);

			System.out.println("查看当前连接的设备 ： " + exeCmd);

			// String currentact = exeCmd(current_act);
			// System.out.println("查看当前ACT ： " + currentact);
			while (true) {
				startTime = System.currentTimeMillis();
				String jt = exeCmd(cmd_screenshot);

				System.out.println("截图已完成 " );
				String jt_pull = exeCmd(cmd_pull);
				//System.out.println("截图  pull ： " + jt_pull);

				ImageUtils.cutPNG(new FileInputStream("D:\\Users\\Administrator\\Desktop\\cddh\\screenshot.png"),
						new FileOutputStream("D:\\Users\\Administrator\\Desktop\\cddh\\screenshot_2.png"), 0, 230, 1080,
						980);
				System.out.println("图片裁剪完毕");
				getTokenAndToOcr();

				Scanner scanner = new Scanner(System.in);
				String nextLine = scanner.nextLine();
				if (nextLine.equals("100")) {
					break;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void getTokenAndToOcr() {

		

		Request request = new Request.Builder()
				.url("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&" + "client_id=" + API_KEY
						+ "&client_secret=" + SECRET_KEY + "&")
				.addHeader("content-type", "application/json;charset:utf-8").build();

		
		if(access_token.length() > 0){
	System.out.println("-1");
			toOcr();
	
		}else{
			System.out.println("-2");
			client.newCall(request).enqueue(new okhttp3.Callback() {

				@Override
				public void onResponse(Call arg0, okhttp3.Response arg1) throws IOException {
					// stub
					ResponseBody body = arg1.body();
					String string = body.string();
				//	System.out.println(string);
					JSONObject jsonObject = new JSONObject(string);
					access_token = jsonObject.getString("access_token");

					System.out.println(access_token);
					toOcr();
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					//arg1.printStackTrace();
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
					String result = stringBuffer.toString();
					
					System.out.println(result);

					if (issue.length() <= 0) {
						System.out.println("-------------------- 问题有误 ----------------------\n");
					} else {
						BaiDuZhiDaoRun baiDuZhiDaoRun = new BaiDuZhiDaoRun(issue, arrayList);
						baiDuZhiDaoRun.run();
					}

				} catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				//arg1.printStackTrace();
			}
		});
	}

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

}
