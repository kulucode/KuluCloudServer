package cn.tpson.device.watch.cmd;

import java.util.ArrayList;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.annotation.JSONField;

import cn.tpson.device.watch.data.LbsData;
import cn.tpson.device.watch.data.WifiData;

public abstract class BaseCommand {

	public final static String START_STR = "@G#@";
	public final static String END_STR = "@R#@";
	public String type = "";// 类别代码
	public String version = "V01";// 协议版本
	public String imei = "";
	public String imsi = "";
	public String time = "";

	@JSONField(serialize = false)
	public String getToken() {
		return imei + "|" + getImsi();
	}

	public static BaseCommand getBaseCommand(String cmd) {
		BaseCommand ret = null;
		String[] cmds = cmd.split(",");
		String version = cmds[1];// 版本
		String type = cmds[2];// 类别版本
		String imei = cmds[3];// IMEI
		String imsi = cmds[4];// IMSI
		imsi = imsi.substring(1);// 去掉第一个字符9
		String time = cmds[5];//
		if ("1".equals(type)) {// （cmd：1）时间同步报文
			// @G#@,V01,1,108101712007180,9460040114915162,0,@R#@
			ret = new InDateTimeCmd();
		} else if ("2".equals(type)) {// （cmd：2）开机通告
			// @G#@,V01,2,108101712007180,9460004820707903,20180315170108,59,@R#@
			String electricity = cmds[6];// 电量
			ret = new InStartCmd(electricity);
		} else if ("6".equals(type)) {// （cmd：6）轨迹报文(GPS)
			// @B#@,V01,6,111112222233333,8888888888888888,20150312010203,23.2014050;104.235212,@E#@
			String location = cmds[6];// 位置
			ret = new InLocationCmd(location);
		} else if ("13".equals(type)) {// （cmd：13）计步周期上传
			// @B#@,V01,14,111112222233333,8888888888888888,20150312010303,66,15,@E#@
			String step = cmds[6];// 心率
			ret = new InStepCmd(step);
		} else if ("14".equals(type)) {// （cmd：14）心率周期上传（中间包含了实时电量）
			// @B#@,V01,14,111112222233333,8888888888888888,20150312010303,66,15,@E#@
			String heartRate = cmds[6];// 心率
			String electricity = cmds[7];// 电量
			ret = new InHeartRateCmd(heartRate, electricity);
		} else if ("42".equals(type)
				// || "43".equals(type)
				|| "104".equals(type)) {// （cmd：42）SOS警报(WIFI+LBS)||
			// （cmd：43）轨迹报文（WIFI+LBS） （cmd：104）跌倒报警
			// @G#@,V01,43,108101712007180,9460004820707903,20180315170109,4,460;0;29601;8431;-33|460;0;29601;7652;-62|460;0;29601;12522;-36|460;0;29820;9903;-22,6,8c:21:0a:7e:22:52;ffffffffffffffff;-64|50:fa:84:56:ad:09;00430043002d0048006f006d0065002d0032002e00340047;-72|d8:b0:4c:b6:ea:e0;00470061006f00450072002d005800430053004700300031;-73|d4:68:ba:07:55:21;0042004100490043004800550041004e0047;-73|34:96:72:c0:c1:b7;006700640074006b;-89|08:9b:4b:93:e7:8d;0059004c00490054002d0032002e00340047;-89,@R#@
			ArrayList<LbsData> lbsData = new ArrayList<LbsData>();
			ArrayList<WifiData> wifiData = new ArrayList<WifiData>();
			int baseStationNum = Integer.parseInt(cmds[6]);
			String lbsDataStr = cmds[7];
			if (!"NONE".equals(lbsDataStr)) {
				String[] strs = lbsDataStr.split("\\|");
				LbsData lbs = null;
				for (String str : strs) {
					String[] data = str.split(";");
					lbs = new LbsData();
					lbsData.add(lbs);
					lbs.setMcc(Integer.parseInt(data[0]));
					lbs.setMnc(Integer.parseInt(data[1]));
					lbs.setLac(Integer.parseInt(data[2]));
					lbs.setCell_id(Integer.parseInt(data[3]));
					lbs.setRssi(Integer.parseInt(data[4]));
				}
			}
			int wifiNum = Integer.parseInt(cmds[8]);
			String wifiDataStr = cmds[9];
			if (!"NONE".equals(wifiDataStr)) {
				String[] strs = wifiDataStr.split("\\|");
				WifiData wifi = null;
				for (String str : strs) {
					String[] data = str.split(";");
					wifi = new WifiData();
					wifiData.add(wifi);
					wifi.setAddress(data[0]);
					wifi.setSsid(data[1]);
					wifi.setRssi(Integer.parseInt(data[2]));
				}
			}
			if ("42".equals(type)) {
				ret = new InSosCmd(baseStationNum, lbsData, wifiNum, wifiData);
			} else if ("43".equals(type)) {
				ret = new InTrackCmd(baseStationNum, lbsData, wifiNum, wifiData);
			} else {// 104
				ret = new InFallCmd(baseStationNum, lbsData, wifiNum, wifiData);
			}
		} else if ("44".equals(type)) {// （cmd：44）心跳报文
			// @G#@,V01,44,108101712007180,9460004820707903,@R#@
			ret = new InHeartbeatCmd();
		} else if ("99".equals(type)) {// （cmd：99）心跳异常报文
			// @B#@,V01,6,111112222233333,8888888888888888,20150312010203,23.2014050;104.235212,@E#@
			String value = cmds[6];// 位置
			ret = new InHeartbeatErrorCmd(value);
		} else if ("110".equals(type)) {// （cmd：110）血压正常报文
			// @B#@,V01,110,111112222233333,8888888888888888,20150312010303,120,70,@E#@
			String high = cmds[6];//
			String low = cmds[7];//
			ret = new InBloodPressureOkCmd(high, low);
		} else if ("113".equals(type)) {// （cmd：113）血压异常报文
			// @B#@,V01,113,111112222233333,8888888888888888,20150312010203,170,80,@E#@
			String high = cmds[6];//
			String low = cmds[7];//
			ret = new InBloodPressureErrorCmd(high, low);
		}
		if (ret != null) {
			ret.setType(type);
			ret.setVersion(version);
			ret.setImei(imei);
			ret.setImsi(imsi);
			if (!END_STR.equals(time)) {
				ret.setTime(time);
			}
		}
		return ret;
	}

	public String formatCmdStr(String data) {
		if (data == null || data.equals("")) {
			return START_STR + "," + version + "," + type + "," + END_STR;
		} else {
			return START_STR + "," + version + "," + type + "," + data + "," + END_STR;
		}
	}

	public abstract String toCmd();

	public abstract void handle(IoSession session);

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
