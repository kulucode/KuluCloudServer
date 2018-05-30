package com.epcs.utils.parser2;

import java.util.ArrayList;

public class PacketQueryTermParas {
    public final static int MSG_ID = 0x8104;

    public final static int RESPONSE_MSG_ID = 0x0104;
    /** 终端应答消息消息体 */
    final static int RESPONSE_SEQ_INDEX = 0;// 对应的平台发出的查询消息的流水号
    final static int RESPONSE_SEQ_LEN = 2;
    final static int RESPONSE_PARAS_NUM_INDEX = RESPONSE_SEQ_INDEX + RESPONSE_SEQ_LEN;
    final static int RESPONSE_PARAS_NUM_LEN = 1;
    final static int RESPONSE_PARAS_LIST_INDEX = RESPONSE_PARAS_NUM_INDEX + RESPONSE_PARAS_NUM_LEN;
    final static int RESPONSE_PARAS_LIST_LEN = 0;// 不固定长度

    public int msgSeq;
    public int parasNum;// 应答参数个数

    public ArrayList<TermPara> parasList = new ArrayList<>();// 参数项列表

    final static int PARA_ID_LEN = 4;// 参数ID
    final static int PARA_LEN = 1;
    public class TermPara {
        public int id;
        public int length;
        public byte[] data;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" [QueryTermParas]:");
        return stringBuilder.toString();
    }

    // response
    public PacketQueryTermParas(byte[] msgBody) {
        if (msgBody == null)
            return;
        msgSeq = UtilsConvert.formInt(msgBody, RESPONSE_SEQ_INDEX, RESPONSE_SEQ_LEN);
        parasNum = UtilsConvert.formInt(msgBody, RESPONSE_PARAS_NUM_INDEX, RESPONSE_PARAS_NUM_LEN);
        
        int baseIndex = RESPONSE_PARAS_LIST_INDEX;
        while (baseIndex < msgBody.length) {
            /** 读取位置附加信息项列表 */
            TermPara para = new TermPara();
            para.id = UtilsConvert.formInt(msgBody, baseIndex, PARA_ID_LEN);
            baseIndex += PARA_ID_LEN;
            para.length = UtilsConvert.formInt(msgBody, baseIndex, PARA_LEN);
            baseIndex += PARA_LEN;
            para.data = UtilsConvert.subBytes(msgBody, baseIndex, para.length);
            baseIndex += para.length;
            parasList.add(para);
        }
    }

    // 平台发送的查询消息,消息体为空
    public static byte[] formPlatformMsgBody() {
        return new byte[] {};
    }

    public boolean isValidPacket() {
        return true;
    }
}
