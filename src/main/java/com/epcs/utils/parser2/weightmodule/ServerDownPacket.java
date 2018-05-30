package com.epcs.utils.parser2.weightmodule;

import java.util.Calendar;

import com.epcs.utils.parser2.UtilsConvert;

public class ServerDownPacket {
    /** identifier */
    public final static byte HEAD = (byte) 0x68;
    public final static byte TAIL = (byte) 0x97;

    public static byte[] setAll(DevInfoPacket devInfo, double lon, double lat,
            int uploadCycle) {
        byte[] msg = new byte[] {};
        // msg=UtilsConvert.combineBytes(msg,)
        byte[] msgType = new byte[] { devInfo.msgType };
        msg = UtilsConvert.combineBytes(msg, msgType);
        byte[] devType = UtilsConvert.int2BytesBigEndian(devInfo.devType, 2);
        msg = UtilsConvert.combineBytes(msg, devType);
        byte[] devId = devInfo.devId;
        msg = UtilsConvert.combineBytes(msg, devId);
        byte[] cmdId = new byte[] { 0x0 };
        msg = UtilsConvert.combineBytes(msg, cmdId);

        Calendar cal = Calendar.getInstance();// 使用日历类
        int year = cal.get(Calendar.YEAR);// 得到年
        int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
        int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 得到小时
        int minute = cal.get(Calendar.MINUTE);// 得到分钟
        int second = cal.get(Calendar.SECOND);// 得到秒
        byte[] date = new byte[] { (byte) (year - 2000), (byte) month, (byte) day, (byte) hour,
                (byte) minute, (byte) second };
        msg = UtilsConvert.combineBytes(msg, date);

        int setLon = 0;
        setLon = setLon | (0xff & (int) lon) << 24;// 高8位整数，低24位小数
        setLon = setLon | (0xffffff & ((int) (1000000 * (lon - (int) lon))));// 高8位整数，低24位小数
        byte[] longitude = UtilsConvert.int2BytesBigEndian(setLon);

        int setLat = 0;
        setLat = setLat | (0xff & (int) lat) << 24;// 高8位整数，低24位小数
        setLat = setLat | (0xffffff & ((int) (1000000 * (lat - (int) lat))));// 高8位整数，低24位小数
        byte[] latitude = UtilsConvert.int2BytesBigEndian(setLat);

        msg = UtilsConvert.combineBytes(msg, longitude, latitude);

        byte[] uploadCycles = UtilsConvert.int2BytesBigEndian(uploadCycle, 2);
        msg = UtilsConvert.combineBytes(msg, uploadCycles);
        // head 1 tail 1 msgLen 2 crc 2=>6
        byte[] msgLen = UtilsConvert.int2BytesBigEndian(msg.length + 6, 2);
        msg = UtilsConvert.combineBytes(msgLen, msg);
        byte[] crc = UtilsConvert.int2BytesBigEndian(CRC.calcCrc16(msg), 2);
        msg = UtilsConvert.combineBytes(new byte[] { HEAD }, msg, crc, new byte[] { TAIL });

        return msg;
    }

    public static byte[] setLocation(DevInfoPacket devInfo, double lon, double lat) {
        
        return setAll(devInfo, lon, lat, devInfo.upLoadCycle);
    }

    public static byte[] setUploadCycle(DevInfoPacket devInfo, int uploadCycle) {

        return setAll(devInfo, devInfo.longitude, devInfo.latitude, uploadCycle);
    }
}
