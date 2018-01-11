package com.zteng.assist.answer;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BaiDuZhiDaoRun implements Runnable {

	public String search = "";
	public ArrayList<String> optionItem;

	//
	public BaiDuZhiDaoRun(String search, ArrayList<String> optionItem) {
		super();
		this.search = search;
		this.optionItem = optionItem;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		String encode = URLEncoder.encode(search);
		String url = "https://zhidao.baidu.com/search?lm=0&rn=10&pn=0&fr=search&word=" + encode;

		try {
			Document doc = Jsoup.connect(url)
					// .data("query", "Java") //请求参数
					// .userAgent("I’mjsoup") //设置User-Agent
					// .cookie("auth", "token") //设置cookie
					.timeout(2000) // 设置连接超时时间
					.get();
			Elements elementsByClass = doc.getElementsByClass("list");

			Element element = elementsByClass.get(0);
			Elements elementsByClass2 = element.getElementsByClass("dl");
			int size = elementsByClass2.size();
			System.out.println("\n---------------- 百度知道结果(展示前三条)----------------------\n");

			if (size > 6) {
				size = 6;
			}

			for (int x = 0; x < size; x++) {

				Element element2 = elementsByClass2.get(x);
/*
				Elements dtmb4line = element2.getElementsByClass("dt mb-4 line");
				if (dtmb4line.size() > 0)
					System.out.println(dtmb4line.get(0).text());
*/
			/*	Elements summary = element2.getElementsByClass("dd summary");
				if (summary.size() > 0)
					System.out.println(summary.get(0).text());*/

				Elements answer = element2.getElementsByClass("dd answer");
				if (answer.size() > 0)
					System.out.println(answer.get(0).text() +"\n");

				//System.out.println("\n--------------------------------------------------------\n");
			}
			
			
			long endTime = System.currentTimeMillis() - CDDemo.startTime;
			
			System.out.println("百度知道Thread  所用时间 = "+endTime+" / "+(endTime/1000));
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}

}
