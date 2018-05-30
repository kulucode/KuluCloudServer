package com.epcs.utils;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/** store gps data in file */

public class DataTableHelper {
	static Logger logger = Logger.getLogger(DataTableHelper.class.getName());

	/** parse */
	public static DataTableBean toBean(JSONArray data) {

		if (data == null || data.size() < 1)
			return null;

		DataTableBean dBean = new DataTableBean();

		for (int i = 0; i < data.size(); i++) {
			JSONObject obj = (JSONObject) data.get(i);
			if (obj.get("name").equals("sEcho") && obj.get("value") != null)
				dBean.setsEcho(obj.get("value").toString());

			else if (obj.get("name").equals("iColumns") && obj.get("value") != null)
				dBean.setiColumns(Integer.valueOf(obj.get("value").toString()));

			else if (obj.get("name").equals("sColumns") && obj.get("value") != null)
				dBean.setsColumns(obj.get("value").toString());

			else if (obj.get("name").equals("sSearch") && obj.get("value") != null)
				dBean.setsSearch(obj.get("value").toString());

			else if (obj.get("name").equals("iDisplayStart") && obj.get("value") != null)
				dBean.setiDisplayStart(Integer.valueOf(obj.getString("value")));

			else if (obj.get("name").equals("iDisplayLength") && obj.get("value") != null)
				dBean.setiDisplayLength(Integer.valueOf(obj.getString("value")));

			else if (obj.get("name").equals("bRegex") && obj.get("value") != null)
				dBean.setbRegex(Boolean.parseBoolean(obj.getString("value")));

			else if (obj.get("name").equals("iSortCol_0") && obj.get("value") != null)
				dBean.setiSortCol_0(Integer.valueOf(obj.getString("value")));

			else if (obj.get("name").equals("sSortDir_0") && obj.get("value") != null)
				dBean.setsSortDir_0(obj.get("value").toString());

			else if (obj.get("name").equals("iSortingCols") && obj.get("value") != null)
				dBean.setiSortingCols(Integer.valueOf(obj.getString("value")));

			else if (obj.get("name").equals("startTimeStr") && obj.get("value") != null)
				dBean.setStartTimeStr(obj.get("value").toString());

			else if (obj.get("name").equals("endTimeStr") && obj.get("value") != null)
				dBean.setEndTimeStr(obj.get("value").toString());

			else if (obj.get("name").equals("targetId") && obj.get("value") != null)
				dBean.setTargetId(Integer.valueOf(obj.getString("value")));

			else if (obj.get("name").equals("carId") && obj.getLong("value") != null)
				dBean.setCarId(obj.getLong("value"));

			else if (obj.get("name").equals("companyId") && obj.getLong("value") != null)
				dBean.setCompanyId(obj.getLong("value"));

			else if (obj.get("name").equals("plate") && obj.get("value") != null)
				dBean.setPlate(obj.getString("value"));

			else if (obj.get("name").equals("alertId") && obj.getLong("value") != null)
				dBean.setAlertId(obj.getLong("value"));

			else if (obj.get("name").equals("status") && obj.getInteger("value") != null) {
				dBean.setStatus(obj.getIntValue("value"));
			}

			else if (obj.get("name").equals("sKey") && obj.get("value") != null)
				dBean.setsKey(obj.getString("value"));

		}

		return dBean;
	}

}
