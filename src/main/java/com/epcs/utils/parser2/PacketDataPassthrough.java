package com.epcs.utils.parser2;

// 数据透传
public class PacketDataPassthrough {
    public final static int MSG_ID = 0x8900;
    public final static int RESPONSE_MSG_ID = 0x0900;

    public final static int OIL_LEVEL_TYPE = 0x41;
    public final static int OIL_LEVEL_TYPE2 = 0x42;
    public final static byte[] QUERY_OIL_LEVEL_MSG = new byte[] { 0x24, 0x21, 0x52, 0x59, 0x30,
            0x31, 0x35, 0x31, 0x0D, 0x0A };
    public final static int OIL_REPLY_INDEX = 8;
    public final static int OIL_REPLY_LEN = 4;

    public final static int MSG_TYPE_INDEX = 0;
    public final static int MSG_TYPE_LEN = 1;
    public final static int MSG_INDEX = MSG_TYPE_INDEX + MSG_TYPE_LEN;
    public int msgType;
    public byte[] msg;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" [PacketDataPassthrough]:");
        stringBuilder.append(" msgType=" + msgType);
        stringBuilder.append(" msg=" + UtilsConvert.byteToHex0x(msg));
        return stringBuilder.toString();
    }

    public PacketDataPassthrough(byte[] msgBody) {
        if (msgBody == null)
            return;
        msgType = UtilsConvert.formInt(msg, MSG_TYPE_INDEX, MSG_TYPE_LEN);
        msg = UtilsConvert.subBytes(msgBody, MSG_INDEX, msgBody.length - MSG_TYPE_LEN);
    }

    // 平台发送的查询消息,消息体为空
    public static byte[] formPlatformMsgBody(byte type) {
        switch (type) {
        case OIL_LEVEL_TYPE:
        case OIL_LEVEL_TYPE2:
            return UtilsConvert.combineBytes(new byte[] { type }, QUERY_OIL_LEVEL_MSG);
        default:
            break;
        }
        return new byte[] {};
    }

    // 0-999.9mm
    public static float getOilLevel(byte[] msg) {
        if (msg == null)
            return -1;
        int ad = UtilsConvert.formInt(msg, OIL_REPLY_INDEX, OIL_REPLY_LEN);
        return (float) (ad * 999.9 / 0xffff);
    }

    public boolean isValidPacket() {
        return true;
    }

}
