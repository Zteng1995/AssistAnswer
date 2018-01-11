package com.zteng.assist.answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * 结果的 简单筛选
 * 
 * @author Administrator
 *
 */
public class ResultFiltrate {
	public static final String[] farr = { ",", ".", "的", "得", ";", "。", "，", "”", "“", "《", "》", "、", " ", "：", "）",
			"（", "答", "-" };

	/**
	 * 
	 * @param chooseItem  识别的选项
	 * @param answes 搜索的答案
	 */
	public static void filtrating(ArrayList<String> answes, ArrayList<String> chooseItem) {
		String answesString = answes.toString();
		List<String> asList = Arrays.asList(farr);
		TreeMap<String, String> treeMap = new TreeMap<String, String>();

		for (String chooseItemStr : chooseItem) {
			char[] charArray = chooseItemStr.toCharArray();

			for (int x = 0; x < charArray.length; x++) {
				String key = "" + charArray[x];
				if (asList.contains(key)) {
					continue;
				}
				int subCount = getSubCount(answesString, key);
				treeMap.put(subCount+"_"+key, key);
			}
		}
		
		System.out.println(treeMap.toString()+"\n");
	}
	

	/**
	 * 
	 * @param chooseItem  识别的选项
	 * @param answes 搜索的答案
	 */
	public static void filtratingV2(ArrayList<String> answes, ArrayList<String> chooseItem) {
		String answesString = answes.toString();
		List<String> asList = Arrays.asList(farr);
		TreeMap<String, String> treeMap = new TreeMap<String, String>();

		for (String chooseItemStr : chooseItem) {
			char[] charArray = chooseItemStr.toCharArray();

			for (int x = 0; x < charArray.length-1; x++) {
				String key = "" + charArray[x]+charArray[x+1];
				if (asList.contains(key)) {
					continue;
				}
				int subCount = getSubCount(answesString, key);
				treeMap.put(subCount+"_"+key, key);
			}
		}
		System.out.println(treeMap.toString()+"\n");
	}
	

	public static int getSubCount(String str, String key) {
		int count = 0;
		int index = 0;
		while ((index = str.indexOf(key)) != -1) {
			//System.out.println("str=" + str);
			str = str.substring(index + key.length());
			count++;
		}
		return count;
	}

	/*public static void main(String[] args) {

		System.out.println(getSubCount(
				"垂死病中惊坐起 是谁 的哪首诗？它的前一句或后一句是？ 20 187****5323 我有更好的答案 推荐于2017-10-14 15:10:47 ... 2016-02-12 垂死病中惊坐起 ，笑问客从何处来的 2 ..."
						+ "我在病中缠绵多日，听闻你的到来惊喜万分，猛然坐起，正遇上你关切的眼神，我的眉目上晕上笑意，道一句，好久不见，何方归来。"
						+ "闻乐天授江州司马 唐 元稹 残灯无焰影幢幢，此夕闻君谪九江。 垂死病中惊坐起，暗风吹雨入寒窗。是元稹听到白居易（字乐天）被贬时的心情，",
				"元"));

	}*/

}
