/**
 * LiangKun 2017.04.04 created
 * */
package com.epcs.utils.parser2;

public class PacketParser {

    public static CommPacket parsePacket(byte[] in) {
        CommPacket packet = new CommPacket(in);
        if (!packet.isValidPacket())
            return null;
        else
            return packet;
    }
    //
    // public static CarInfo parsePacket(byte[] in) {
    // CarInfo carInfo = new CarInfo();
    //
    // CommPacket packet = new CommPacket(in);
    // if (!packet.isValidPacket())
    // return null;
    //
    // carInfo.id = packet.clientId;
    // carInfo.carType = carInfo.id & 0x0000000f;
    // carInfo.orgId = packet.orgId;
    // carInfo.lastPacketTime = System.currentTimeMillis();
    // carInfo.seq = packet.seq;
    // carInfo.packetType = packet.type;
    // switch (packet.type) {
    // case CommPacket.HEART_BEAT_TYPE:
    // parseHeartBeat(carInfo, packet.msgBody);
    // break;
    // case CommPacket.IO_TYPE:
    // parseIoType(carInfo, packet.msgBody);
    // break;
    //
    // default:
    // break;
    // }
    //
    // return carInfo;
    // }
    //
    // public static CarInfo parsePacket(CarInfo carInfo, CommPacket packet) {
    // if (packet == null || carInfo == null || !packet.isValidPacket())
    // return null;
    //
    // carInfo.id = packet.clientId;
    // carInfo.carType = carInfo.id & 0x0000000f;
    // carInfo.orgId = packet.orgId;
    // carInfo.lastPacketTime = System.currentTimeMillis();
    // carInfo.seq = packet.seq;
    // switch (packet.type) {
    // case CommPacket.HEART_BEAT_TYPE:
    // parseHeartBeat(carInfo, packet.msgBody);
    // break;
    // case CommPacket.IO_TYPE:
    // parseIoType(carInfo, packet.msgBody);
    // break;
    //
    // default:
    // break;
    // }
    //
    // return carInfo;
    // }
    //
    // private static void parseHeartBeat(CarInfo carInfo, byte[] in) {
    // if (carInfo == null || in == null || in.length <
    // CommPacket.LEN_HEART_BEAT)
    // return;
    // int timeBits = formInt(in, CommPacket.INDEX_GPS_TIME,
    // CommPacket.LEN_GPS_TIME);
    //
    // int year = ((timeBits
    // & CommPacket.GPSTIME_YEAR_MASK) >> CommPacket.GPSTIME_YEAR_SHIFT) + 2017;
    // int month = (timeBits
    // & CommPacket.GPSTIME_MONTH_MASK) >> CommPacket.GPSTIME_MONTH_SHIFT;
    // int day = (timeBits & CommPacket.GPSTIME_DAY_MASK) >>
    // CommPacket.GPSTIME_DAY_SHIFT;
    // int hour = (timeBits
    // & CommPacket.GPSTIME_HOUR_MASK) >> CommPacket.GPSTIME_HOUR_SHIFT;
    // int min = (timeBits
    // & CommPacket.GPSTIME_MINUTE_MASK) >> CommPacket.GPSTIME_MINUTE_SHIFT;
    // int sec = (timeBits
    // & CommPacket.GPSTIME_SECOND_MASK) >> CommPacket.GPSTIME_SECOND_SHIFT;
    //
    // carInfo.carGpsTime = UtilsTime.split2TimeLong(year, month, day, hour,
    // min, sec);
    //
    // int index = CommPacket.INDEX_LAT;
    // int latInt = formInt(in, index, CommPacket.LEN_LAT_INT);
    // index += CommPacket.LEN_LAT_INT;
    // int latDecimal = formInt(in, index, CommPacket.LEN_LAT_DECIMAL);
    // String latString = String.format("%d.%d", latInt, latDecimal);
    // carInfo.lat = Double.valueOf(latString);
    //
    // index = CommPacket.INDEX_LON;
    // int lonInt = formInt(in, index, CommPacket.LEN_LON_INT);
    // index += CommPacket.LEN_LON_INT;
    // int lonDecimal = formInt(in, index, CommPacket.LEN_LON_DECIMAL);
    // String lonString = String.format("%d.%d", lonInt, lonDecimal);
    // carInfo.lon = Double.valueOf(lonString);
    //
    // index = CommPacket.INDEX_SPEED;
    // int speedInt = formInt(in, index, CommPacket.LEN_SPEED_INT);
    // index += CommPacket.LEN_SPEED_INT;
    // int speedDecimal = formInt(in, index, CommPacket.LEN_SPEED_DECIMAL);
    // carInfo.speed = UtilsConvert.int2Double(speedInt, speedDecimal);
    //
    // carInfo.heading = formInt(in, CommPacket.INDEX_HEADING,
    // CommPacket.LEN_HEADING);
    // carInfo.signal = formInt(in, CommPacket.INDEX_SIGNAL,
    // CommPacket.LEN_SIGNAL);
    // }
    //
    // private static void parseIoType(CarInfo carInfo, byte[] in) {
    // if (carInfo == null || in == null || in.length < CommPacket.LEN_IO_TYPE)
    // return;
    // int workingTime = formInt(in, CommPacket.INDEX_WORKING_TIME_PI1,
    // CommPacket.LEN_WORKING_TIME_PI1);
    //
    // int workingFreq = formInt(in, CommPacket.INDEX_WORKING_FREQ_PI2,
    // CommPacket.LEN_WORKING_FREQ_PI2);
    // int oilLevel = formInt(in, CommPacket.INDEX_OIL_LEVEL_AI1,
    // CommPacket.LEN_OIL_LEVEL_AI1);
    //
    // carInfo.workingTime = workingTime;
    // carInfo.workingFreq = workingFreq;
    // carInfo.oilLevel = oilLevel;
    //
    // }
}
