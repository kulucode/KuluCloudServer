/**
 * LiangKun 2017.04.04 created
 */
package com.epcs.utils.parser2;

import java.sql.Timestamp;

public class CarInfo {

	public int id;// 车辆或盒子的ID
	public int orgId;// 机构组织orgnization或运营公司ID
	public double lat;// 纬度
	public double lon;// 经度
	public double speed;// km/h
	public long lastPacketTime;// 最后一个有效包的时间,服务器时间

	public int heading;// 航向与北磁极的顺时针夹角值，单位：度
	public long carGpsTime;// 心跳包中包含的时间,取自GPS
	public int signal;// TODO:待确认

	public int alertFlag;
	public int status;
	public int direction;// 0-359顺时针
	public Timestamp carGpsTimestamp;// YYYY-MM-DD hh:mm:ss
	public boolean areStartPoint;

	public int workingTime;// 盒子运行时间/分
	public int workingTimeDelta;// 盒子运行时间/分
	public int workingFreq;// 工作频率/HZ
	public int oilLevel;// 油量百分比
	public int oilLevelDeta;// 油量百分比
	public int carType;// 车辆类型
	public int seq;// 数据包序号
	public int packetType;// 数据包类型
	public int altitude;
	public long companyId;// 公司ID
	public String plate;// 车牌
	public boolean areAlerted;// 是否警报

	//
	public int distantDelta; // 上一次位置距离, unit:cm
	public boolean isAlerted; // 是否报警
	public boolean alertSource; // 是否报警
	public double latBaidu;// 纬度
	public double lonBaidu;// 经度
	public double lastLat;// 用于计算oil
	public double lastLon;// 用于计算oil
	public int isAccOn;// 判断车的状态熄火或启动

	public CarInfo() {
	}

	@Override
	public String toString() {
		return String.format(
				"type=%d,id=%d,orgId=%d,lat=%f,lon=%f,speed=%f,gpsTime=%s,heading=%d,signal=%d,"
						+ "seq=%d,workingTime=%d,workingFreq=%d,oilLevel=%d,oilLevelDeta=%d,workingTimeDelta=%d",
				carType, id, orgId, lat, lon, speed, UtilsTime.getDate(carGpsTime), heading, signal, seq, workingTime,
				workingFreq, oilLevel, oilLevelDeta, workingTimeDelta, isAccOn);

	}
}
