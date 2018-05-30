package com.epcs.utils.parser2;

import java.io.UnsupportedEncodingException;


public class PacketQueryTermAttr {
    public final static int MSG_ID = 0x8107;

    public final static int RESPONSE_MSG_ID = 0x0107;
    /** 终端应答消息消息体 */
    final static int TERM_TYPE_INDEX = 0;// 终端类型
    final static int TERM_TYPE_LEN = 2;
    final static int PRODUCER_ID_INDEX = TERM_TYPE_INDEX + TERM_TYPE_LEN;// 制造商ID
    final static int PRODUCER_ID_LEN = 5;
    final static int TERM_MODEL_INDEX = PRODUCER_ID_INDEX + PRODUCER_ID_LEN;// 终端型号
    final static int TERM_MODEL_LEN = 20;
    final static int TERM_ID_INDEX = TERM_MODEL_INDEX + TERM_MODEL_LEN;// 终端ID
    final static int TERM_ID_LEN = 7;
    final static int TERM_SIM_ICCID_INDEX = TERM_ID_INDEX + TERM_ID_LEN;//42;// 
                                               // 终端SIM卡ICCID
    final static int TERM_SIM_ICCID_LEN = 10;
    final static int TERM_HW_VERION_LEN_INDEX = TERM_SIM_ICCID_INDEX + TERM_SIM_ICCID_LEN;// 终端硬件版本号长度
    final static int TERM_HW_VERION_LEN_LEN = 1;
    final static int TERM_HW_VERION_INDEX = TERM_HW_VERION_LEN_INDEX + TERM_HW_VERION_LEN_LEN;// 终端硬件版本号
    final static int TERM_FIRMWARE_VERION_LEN_LEN = 1;// 终端固件版本号长度
    final static int GNSS_MODULE_ATTR_LEN = 1;// GNSS 模块属性
    final static int COMMUNICATE_MODULE_ATTR_LEN = 1;// 通信模块属性

    public int termType;
    public long producerId;
    public byte[] termModel;
    public long termId;
    public String termSimIccid;
    public String termHwVersion;
    public String termFirmwareVersion;
    public int gnssModuleAttr;
    public int communicateModuleAttr;
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" [QueryTermAttr]:");
        stringBuilder.append(" termType="+termType);
        stringBuilder.append(" producerId="+producerId);
        stringBuilder.append(" termId="+termId);
        stringBuilder.append(" termSimIccid="+termSimIccid);
        stringBuilder.append(" termHwVersion="+termHwVersion);
        stringBuilder.append(" termFirmwareVersion="+termFirmwareVersion);
        stringBuilder.append(" gnssModuleAttr="+gnssModuleAttr);
        stringBuilder.append(" communicateModuleAttr="+communicateModuleAttr);
        return stringBuilder.toString();
    }

    public PacketQueryTermAttr(byte[] msgBody) {
        if (msgBody == null || msgBody.length < TERM_HW_VERION_INDEX - 1)
            return;
        termType = UtilsConvert.formInt(msgBody, TERM_TYPE_INDEX, TERM_TYPE_LEN);
        producerId = UtilsConvert.formLong(msgBody, PRODUCER_ID_INDEX, PRODUCER_ID_LEN);
        termModel = UtilsConvert.subBytes(msgBody, TERM_MODEL_INDEX, TERM_MODEL_LEN);
        termId = UtilsConvert.formLong(msgBody, TERM_ID_INDEX, TERM_ID_LEN);
        termSimIccid = UtilsConvert
                .bcd2Str(UtilsConvert.subBytes(msgBody, TERM_SIM_ICCID_INDEX, TERM_SIM_ICCID_LEN));
        int termHwVersionLen = UtilsConvert.formInt(msgBody, TERM_HW_VERION_LEN_INDEX,
                TERM_HW_VERION_LEN_LEN);
        try {
            termHwVersion = new String(
                    UtilsConvert.subBytes(msgBody, TERM_HW_VERION_INDEX, termHwVersionLen), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int index = TERM_HW_VERION_INDEX + termHwVersionLen;
        int termFirmwareVersionLen = UtilsConvert.formInt(msgBody, index, TERM_FIRMWARE_VERION_LEN_LEN);
        index += TERM_FIRMWARE_VERION_LEN_LEN;

        try {
            termFirmwareVersion = new String(
                    UtilsConvert.subBytes(msgBody, index, termFirmwareVersionLen), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        index+=termFirmwareVersionLen;
        gnssModuleAttr=UtilsConvert.formInt(msgBody, index, GNSS_MODULE_ATTR_LEN);
        index+=GNSS_MODULE_ATTR_LEN;
        communicateModuleAttr=UtilsConvert.formInt(msgBody, index, COMMUNICATE_MODULE_ATTR_LEN);
        
    }

    // 平台发送的查询消息,消息体为空
    public static byte[] formPlatformMsgBody() {
        return new byte[] {};
    }

    public boolean isValidPacket() {
        return true;
    }

}
