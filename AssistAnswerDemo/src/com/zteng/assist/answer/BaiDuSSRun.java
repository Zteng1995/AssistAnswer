package com.zteng.assist.answer;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BaiDuSSRun implements Runnable {

	public static final String mUrl = "https://www.baidu.com/s?wd=";

	public String search = "";
	public ArrayList<String> optionItem;
	//
	public BaiDuSSRun(String search,ArrayList<String> optionItem) {
		super();
		this.search = search;
		this.optionItem = optionItem;
	}

	
	public void reSet(String search,ArrayList<String> optionItem){
		this.search = search;
		this.optionItem = optionItem;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		String url = mUrl + URLEncoder.encode(search);

		try {
			Document doc = Jsoup.connect(url)
					// .data("query", "Java") //请求参数
					// .userAgent("I’mjsoup") //设置User-Agent
					// .cookie("auth", "token") //设置cookie
					.timeout(2000) // 设置连接超时时间
					.get();

			Elements elementsByClass = doc.getElementsByClass("c-abstract");

			int size = elementsByClass.size();
			if (size > 6) size = 6;
			ArrayList<String> arrayList = new ArrayList<String>();
			for (int x = 0; x < size; x++) {

				Element element = elementsByClass.get(x);
		
				if (element != null) {
					String text = element.text();
					arrayList.add(text);
					//System.out.println(text);
				}
			}
			
		
			System.out.println("----------------------------百度搜索----------------------------\n");
			ResultFiltrate.filtrating(arrayList, optionItem);
	
			ResultFiltrate.filtratingV2(arrayList, optionItem);
			
        //      long endTime = System.currentTimeMillis() - CDDemo.startTime;
			
			//System.out.println("百度搜索 Thread  所用时间 = "+endTime+" / "+(endTime/1000) +"\n");
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}

	}

}
