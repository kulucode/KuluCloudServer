package com.epcs.utils.parser2;

import java.util.ArrayList;

public class PacketLocationBatchReport {
    public final static int MSG_ID = 0x0704;
    private boolean isValid = true;

    final static int ITEMS_NUM_INDEX = 0;// 数据项个数
    final static int ITEMS_NUM_LEN = 2;
    final static int LOCATION_TYPE_INDEX = ITEMS_NUM_INDEX + ITEMS_NUM_LEN;// 位置数据类型
    final static int LOCATION_TYPE_LEN = 1;

    final static int LOCATION_DATA_LEN_LEN = 2;

    public int itemsNum;
    public int locationType;
    public final static int TYPE_NORMAL_BATCH = 0;
    public final static int TYPE_BLIND_ZONE_MISSED = 1;
    public ArrayList<PacketLocationReport> locations = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" [LocationBatchPacket]:");
        stringBuilder.append(" itemsNum=" + itemsNum);
        stringBuilder.append(" locationType=" + locationType);
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public PacketLocationBatchReport(byte[] msgBody) {
        if (msgBody == null || msgBody.length < 10)
            return;
        itemsNum = UtilsConvert.formInt(msgBody, ITEMS_NUM_INDEX, ITEMS_NUM_LEN);
        locationType = UtilsConvert.formInt(msgBody, LOCATION_TYPE_INDEX, LOCATION_TYPE_LEN);
        int totalSize = msgBody.length;
        int index = LOCATION_TYPE_INDEX + LOCATION_TYPE_LEN;

        for (int i = 0; i < itemsNum && index < msgBody.length; i++) {
            int dataLen = UtilsConvert.formInt(msgBody, index, LOCATION_DATA_LEN_LEN);
            index += LOCATION_DATA_LEN_LEN;
            if (index + dataLen > totalSize) {
                isValid = false;
                break;
            }
            PacketLocationReport loc = new PacketLocationReport(
                    UtilsConvert.subBytes(msgBody, index, dataLen));
            index += dataLen;
            if (loc.isValidPacket()) {
                //logd(i + ": " + loc);
                locations.add(loc);
            } else {
                isValid = false;
                break;
            }
        }

        if (locations.size() != itemsNum)
            isValid = false;

    }

    public boolean isValidPacket() {
        return isValid;
    }

    static void logd(Object s) {
        System.out.println(s + "");
    }
}