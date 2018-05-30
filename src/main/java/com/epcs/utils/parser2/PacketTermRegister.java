package com.epcs.utils.parser2;

import java.io.UnsupportedEncodingException;

import cn.tpson.device.watch.codec.StrUtil;

public class PacketTermRegister {
	public final static int MSG_ID = 0x0100;
	public final static int RESPONSE_MSG_ID = 0x8100;

	final static int PROVINCE_ID_INDEX = 0;// 省域ID
	final static int PROVINCE_ID_LEN = 2;
	final static int CITY_ID_INDEX = PROVINCE_ID_INDEX + PROVINCE_ID_LEN;// 市县域ID
	final static int CITY_ID_LEN = 2;
	final static int PRODUCER_ID_INDEX = CITY_ID_INDEX + CITY_ID_LEN;// 制造商ID
	final static int PRODUCER_ID_LEN = 5;
	final static int TERM_TYPE_INDEX = PRODUCER_ID_INDEX + PRODUCER_ID_LEN;// 终端类型
	final static int TERM_TYPE_LEN = 20;
	final static int TERM_ID_INDEX = TERM_TYPE_INDEX + TERM_TYPE_LEN;// 终端ID
	final static int TERM_ID_LEN = 7;
	final static int PLATE_COLOR_INDEX = TERM_ID_INDEX + TERM_ID_LEN;// 车牌颜色
	final static int PLATE_COLOR_LEN = 1;
	final static int CAR_VIN_INDEX = PLATE_COLOR_INDEX + PLATE_COLOR_LEN;// 车辆标识
	final static int CAR_VIN_LEN = 0;// 不定长

	public int provinceId;
	public int cityId;
	public byte[] producerId;
	public byte[] termType;
	public byte[] termId;// 7 bytes
	public int plateColor;
	public String carVIN;
	public String plateNumber;

	public PacketTermRegister(byte[] msgBody) {
		if (msgBody == null || msgBody.length < CAR_VIN_INDEX - 1)
			return;
		provinceId = UtilsConvert.formInt(msgBody, PROVINCE_ID_INDEX, PROVINCE_ID_LEN);
		cityId = UtilsConvert.formInt(msgBody, CITY_ID_INDEX, CITY_ID_LEN);
		producerId = UtilsConvert.subBytes(msgBody, PRODUCER_ID_INDEX, PRODUCER_ID_LEN);
		termType = UtilsConvert.subBytes(msgBody, TERM_TYPE_INDEX, TERM_TYPE_LEN);
		termId = UtilsConvert.subBytes(msgBody, TERM_ID_INDEX, TERM_ID_LEN);
		plateColor = UtilsConvert.formInt(msgBody, PLATE_COLOR_INDEX, PLATE_COLOR_LEN);

		byte[] carIdentifier = UtilsConvert.subBytes(msgBody, CAR_VIN_INDEX, msgBody.length - CAR_VIN_INDEX);
		if (plateColor == 0)
			carVIN = StrUtil.bytesToString(carIdentifier);
		else
			plateNumber = StrUtil.bytesToString(carIdentifier);
	}

	/** Response消息相关 */
	public static int REG_SUCCESS = 0;
	public static int REG_CAR_ALREADY_REGED = 1;// 车辆已被注册
	public static int REG_CAR_NOTEXIST = 2;// 数据库中无该车辆
	public static int REG_TERM_ALREADY_REGED = 3;// 终端已被注册
	public static int REG_TERM_NOTEXIST = 4;// 数据库中无该终端

	public static byte[] formResponseMsgBody(int registerMsgSeq, int result, String authKey) {
		if (result > 4 || authKey == null)
			return null;
		byte[] seq = UtilsConvert.int2BytesBigEndian(registerMsgSeq, 2);
		byte[] res = UtilsConvert.int2BytesBigEndian(result, 1);
		byte[] auth = null;
		try {
			auth = authKey.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		byte[] ret = UtilsConvert.combineBytes(seq, res, auth);
		return ret;
	}

	public byte[] simulate() {

		byte[] bprovinceId = UtilsConvert.int2BytesBigEndian(provinceId, PROVINCE_ID_LEN);
		byte[] bcityId = UtilsConvert.int2BytesBigEndian(cityId, CITY_ID_LEN);

		byte[] bproducerId = producerId;
		byte[] btermType = termType;
		byte[] btermId = termId;
		byte[] bplateColor = new byte[] { 2 };

		byte[] bcarIdentifier = null;
		bcarIdentifier = ("KuA" + System.currentTimeMillis() % 100000).getBytes();
		return UtilsConvert.combineBytes(bprovinceId, bcityId, bproducerId, btermType, btermId, bplateColor,
				bcarIdentifier);
	}
}
