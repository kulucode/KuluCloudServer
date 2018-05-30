package cn.tpson.device.watch.cmd;

import java.util.ArrayList;

import org.apache.mina.core.session.IoSession;

import com.kulu.activemq.MQWatchMessage;

import cn.tpson.device.watch.data.LbsData;
import cn.tpson.device.watch.data.WifiData;

/**
 * （cmd：42）SOS警报(WIFI+LBS)
 * 
 * @author tangbin
 *
 */
public class InSosCmd extends BaseCommand {
	public int baseStationNum = 0;
	public ArrayList<LbsData> lbsData = null;
	public int wifiNum = 0;
	public ArrayList<WifiData> wifiData = null;

	public InSosCmd(int baseStationNum, ArrayList<LbsData> lbsData, int wifiNum, ArrayList<WifiData> wifiData) {
		this.type = "42";
		this.baseStationNum = baseStationNum;
		this.lbsData = lbsData;
		this.wifiNum = wifiNum;
		this.wifiData = wifiData;
	}

	@Override
	public String toCmd() {
		return null;
	}

	@Override
	public void handle(IoSession session) {
		MQWatchMessage.sendData(this);
	}
}
