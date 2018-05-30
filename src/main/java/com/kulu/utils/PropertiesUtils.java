package com.kulu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesUtils {
	private static String CONFIG_NAME = "config.properties";
	private static String CONFIG_NAME_PATH = "/" + CONFIG_NAME;

	private HashMap<String, String> map = new HashMap<String, String>();
	public static PropertiesUtils config = new PropertiesUtils(CONFIG_NAME_PATH);

	private String getRootPath() throws Exception {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		path = java.net.URLDecoder.decode(path, "UTF-8");
		return new File(path).getParentFile().getPath();
	}

	private PropertiesUtils(String filePath) {
		InputStream in = null;
		try {
			String path = getRootPath() + File.separator + CONFIG_NAME_PATH;
			File configFile = new File(path);
			if (configFile.exists()) {
				System.out.println("load config in " + path);
				in = new FileInputStream(configFile);
			} else {
				System.out.println("load config in jar");
				in = PropertiesUtils.class.getClassLoader().getResourceAsStream(filePath);
				if (in == null) {
					in = PropertiesUtils.class.getResourceAsStream(CONFIG_NAME_PATH);
				}
			}
			Properties prop = new Properties();
			prop.load(in);
			String _key, _value;
			for (Object key : prop.keySet()) {
				_key = (String) key;
				_value = (String) prop.get(key);
				// LogUtil.i(_key + "=" + _value);
				map.put(_key, _value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String get(String key) {
		String result = "";
		if (map.containsKey(key)) {
			result = map.get(key);
		}
		return result;
	}

	public int getInteger(String key) {
		int ret = 0;
		String result = get(key);
		if (result != null && !result.equals("")) {
			ret = Integer.parseInt(result);
		}
		return ret;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
}