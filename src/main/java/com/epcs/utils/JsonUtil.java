package com.epcs.utils;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

	public static Map toMap(String json) {
		return (Map) JSON.parse(json);

	}

	public static String toJson(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static void main(String[] args) {
		// 第六种方式
		/**
		 * JSONObject是Map接口的一个实现类
		 */
		String str = "{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":\"maliu\"}";
		Map maps = (Map) JSON.parse(str);
		System.out.println("这个是用JSON类来解析JSON字符串!!!");
		for (Object map : maps.entrySet()) {
			System.out.println(((Map.Entry) map).getKey() + "     " + ((Map.Entry) map).getValue());
		}
	}
}
