package com.kulu.utils;

/**
 * Created by Super on 2017-10-15.
 */

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtils {

	// @JSONField(serialize=false)//过滤字段
	public static String toJSONString(Object inObj) {
		return JSON.toJSONString(inObj);
	}

	/**
	 * 过滤字段
	 *
	 * @param inObj
	 * @param names
	 * @return
	 */
	public static String toJSONString(Object inObj, final String... names) {
		return JSON.toJSONString(inObj, new PropertyFilter() {
			@Override
			public boolean apply(Object object, String name, Object value) {
				for (String str : names) {
					if (name.equalsIgnoreCase(str)) {
						return false;
					}
				}
				return true;
			}
		});
	}

	public static String toJSONString(Object inObj, SerializerFeature... features) {
		return JSON.toJSONString(inObj, features);
	}

	public static JSONArray parseArray(String json) {
		return JSON.parseArray(json);
	}

	public static JSONObject parseObject(Object obj) {
		return JSON.parseObject(toJSONString(obj));
	}

	public static JSONObject parseObject(String json) {
		return JSON.parseObject(json);
	}

	public static <T> T ToObj(String json, Class<T> _class) {
		return JSON.parseObject(json, _class);
	}

	public static <T> T ToObj(Object obj, Class<T> _class) {
		return JSON.parseObject(toJSONString(obj), _class);
	}

	public static <T> T ToObj(HashMap<String, Object> map, Class<T> _class) {
		return JSON.parseObject(JSON.toJSONString(map), _class);
	}

	public static <T> T ToObj(JSONObject obj, Class<T> _class) {
		return JSON.parseObject(obj.toString(), _class);
	}

	public static <T> List<T> ToObj(JSONArray obj, Class<T> _class) {
		return (List) JSON.parseArray(obj.toString(), _class);
	}

	public static <T> List<T> ToArray(String obj, Class<T> _class) {
		return (List) JSON.parseArray(obj, _class);
	}

	public static <T> List<T> ToArray(Object obj, Class<T> _class) {
		return (List) JSON.parseArray(toJSONString(obj), _class);
	}

}
