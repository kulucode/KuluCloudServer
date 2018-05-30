package com.epcs.utils.parser2;

import java.util.ArrayList;

public class PacketSetTermParas {
    public final static int MSG_ID = 0x8103;

    /** 位置基本信息 */
    final static int ALERT_FLAG_INDEX = 0;// 报警标志
    final static int ALERT_FLAG_LEN = 4;
    final static int STATUS_INDEX = ALERT_FLAG_INDEX + ALERT_FLAG_LEN;// 状态
    final static int STATUS_LEN = 4;
    final static int LATITUDE_INDEX = STATUS_INDEX + STATUS_LEN;// 纬度
    final static int LATITUDE_LEN = 4;
    final static int LONGITUDE_INDEX = LATITUDE_INDEX + LATITUDE_LEN;// 经度
    final static int LONGITUDE_LEN = 4;
    final static int ALTITUDE_INDEX = LONGITUDE_INDEX + LONGITUDE_LEN;// 高程
    final static int ALTITUDE_LEN = 2;
    final static int SPEED_INDEX = ALTITUDE_INDEX + ALTITUDE_LEN;// 速度
    final static int SPEED_LEN = 2;
    final static int DIRECTION_INDEX = SPEED_INDEX + SPEED_LEN;// 方向
    final static int DIRECTION_LEN = 2;
    final static int TIME_INDEX = DIRECTION_INDEX + DIRECTION_LEN;// 时间
    final static int TIME_LEN = 6;
    /** 位置附加信息项列表 */
    final static int EXTRA_MSG_INDEX = TIME_INDEX + TIME_LEN;// 附加信息ID
    final static int EXTRA_MSG_ID_LEN = 1;
    final static int EXTRA_MSG_LEN_LEN = 1;

    public final static int LOCATION_RATIO = 1000000;
    public int alertFlag;
    public int status;
    public double latitude;
    public double longitude;
    public int altitude;// m
    public int speed;// 0.1km/h
    public int direction;// 0-359顺时针
    public String time;// BCD[6] YY-MM-DD-hh-mm-ss
    public ArrayList<ExtraMsg> extraMsgList = new ArrayList<>();// 位置附加信息项列表

    public class ExtraMsg {
        public int id;
        public int length;
        public byte[] msg;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" [LocationPacket]:");
        stringBuilder.append(" alertFlag=" + alertFlag);
        stringBuilder.append(" status=" + status);
        stringBuilder.append(" latitude=" + latitude);
        stringBuilder.append(" longitude=" + longitude);
        stringBuilder.append(" altitude=" + altitude);
        stringBuilder.append(" speed=" + speed);
        stringBuilder.append(" direction=" + direction);
        stringBuilder.append(" time=" + time + "\n");
        return stringBuilder.toString();
    }

    public PacketSetTermParas(byte[] msgBody) {
        if (msgBody == null)
            return;
        alertFlag = UtilsConvert.formInt(msgBody, ALERT_FLAG_INDEX, ALERT_FLAG_LEN);
        status = UtilsConvert.formInt(msgBody, STATUS_INDEX, STATUS_LEN);
        latitude = (double) UtilsConvert.formInt(msgBody, LATITUDE_INDEX, LATITUDE_LEN)
                / LOCATION_RATIO;
        longitude = (double) UtilsConvert.formInt(msgBody, LONGITUDE_INDEX, LONGITUDE_LEN)
                / LOCATION_RATIO;
        altitude = UtilsConvert.formInt(msgBody, ALTITUDE_INDEX, ALTITUDE_LEN);
        speed = UtilsConvert.formInt(msgBody, SPEED_INDEX, SPEED_LEN);
        direction = UtilsConvert.formInt(msgBody, DIRECTION_INDEX, DIRECTION_LEN);
        time = UtilsConvert.bcd2Str(UtilsConvert.subBytes(msgBody, TIME_INDEX, TIME_LEN));

        int baseIndex = EXTRA_MSG_INDEX;
        while (baseIndex < msgBody.length) {
            /** 读取位置附加信息项列表 */
            ExtraMsg extraMsg = new ExtraMsg();
            extraMsg.id = UtilsConvert.formInt(msgBody, baseIndex, EXTRA_MSG_ID_LEN);
            baseIndex += EXTRA_MSG_ID_LEN;
            extraMsg.length = UtilsConvert.formInt(msgBody, baseIndex, EXTRA_MSG_LEN_LEN);
            baseIndex += EXTRA_MSG_LEN_LEN;
            extraMsg.msg = UtilsConvert.subBytes(msgBody, baseIndex, extraMsg.length);
            baseIndex += extraMsg.length;
            extraMsgList.add(extraMsg);
        }
    }

    public boolean isValidPacket() {
        return true;
    }
}
