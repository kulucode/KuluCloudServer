/**
 * LiangKun 2017.04.04 created
 * */
package com.epcs.utils.parser2;

import org.apache.log4j.Logger;

public class CommPacket {

    static Logger logger = Logger.getLogger(CommPacket.class.getName());

    /** identifier */
    public final static int HEAD = 0x7e;
    public final static int TAIL = 0x7e;

    /** message header - section_based - bytes为单位 */
    final static int MSG_ID_INDEX = 0;
    final static int MSG_ID_LEN = 2;

    final static int MSG_BODY_ATTR_INDEX = MSG_ID_INDEX + MSG_ID_LEN;
    final static int MSG_BODY_ATTR_LEN = 2;

    final static int TERM_PHONE_NUMBER_INDEX = MSG_BODY_ATTR_INDEX + MSG_BODY_ATTR_LEN;
    final static int TERM_PHONE_NUMBER_LEN = 6;

    final static int MSG_SEQ_INDEX = TERM_PHONE_NUMBER_INDEX + TERM_PHONE_NUMBER_LEN;
    final static int MSG_SEQ_LEN = 2;

    final static int MSG_PACK_ITEMS_INDEX = MSG_SEQ_INDEX + MSG_SEQ_LEN;
    final static int MSG_PACK_ITEMS_LEN = 4;// 0或4字节不固定长度
    final static int MSG_MULTI_PACK_NUM_INDEX = MSG_PACK_ITEMS_INDEX;
    final static int MSG_MULTI_PACK_NUM_LEN = 2;
    final static int MSG_MULTI_PACK_SEQ_INDEX = MSG_MULTI_PACK_NUM_INDEX + MSG_MULTI_PACK_NUM_LEN;
    final static int MSG_MULTI_PACK_SEQ_LEN = 2;

    final static int MSG_BODY_NO_MULTI_PACK_INDEX = MSG_PACK_ITEMS_INDEX;
    final static int MSG_BODY_HAVE_MULTI_PACK_INDEX = MSG_MULTI_PACK_SEQ_INDEX
            + MSG_MULTI_PACK_SEQ_LEN;

    /** message pack items,MPI - section_based - byte为单位 */
    final static int MPI_PACK_NUM_INDEX = MSG_PACK_ITEMS_INDEX;
    final static int MPI_PACK_NUM_LEN = 2;
    final static int MPI_PACK_SEQ_INDEX = MPI_PACK_NUM_INDEX + MPI_PACK_NUM_LEN;
    final static int MPI_PACK_SEQ_LEN = 2;

    /** message body attr, MBA - bit_based - bit为单位 */
    final static int MBA_RSV_INDEX = 15;// big endian实际上它是end bit
    final static int MBA_RSV_LEN = 2;
    final static int MBA_IS_MULTI_INDEX = MBA_RSV_INDEX - MBA_RSV_LEN;// 是否分包
    final static int MBA_IS_MULTI_LEN = 1;
    final static int MBA_ENCRYPT_MODE_INDEX = MBA_IS_MULTI_INDEX - MBA_IS_MULTI_LEN;// 数据加密方式
    final static int MBA_ENCRYPT_MODE_LEN = 3;
    final static int MBA_MSG_BODY_LEN_INDEX = MBA_ENCRYPT_MODE_INDEX - MBA_ENCRYPT_MODE_LEN;// 消息体长度
    final static int MBA_MSG_BODY_LEN_LEN = 10;

    /** check code - section_based - bytes为单位 */
    // static int CHECK_CODE_INDEX = 0;//下标不定,紧接着消息体后
    final static int CHECK_CODE_LEN = 1;

    public int msgId;
    // int msgBodyAttr;
    public int mbaReserved;
    public int isMultiPack;
    public int encryptMode;
    public int msgBodyLength;
    public byte[] msgBody = null;// 数据
    public byte checkCode;
    public String termPhoneNumber;
    public int multiPackTotalNum;
    public int multiPackSeq;//包序号 从1开始
    public int msgSeq;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" [Packet]:");
        stringBuilder.append(" msgId=" + msgId);
        stringBuilder.append(" isMultiPack=" + isMultiPack);
        stringBuilder.append(" multiPackTotalNum=" + multiPackTotalNum);
        stringBuilder.append(" multiPackSeq=" + multiPackSeq);
        stringBuilder.append(" encryptMode=" + encryptMode);
        stringBuilder.append(" msgBodyLength=" + msgBodyLength);
        stringBuilder.append(" termPhoneNumber=" + termPhoneNumber);
        stringBuilder.append(" msgSeq=" + msgSeq + "" + "\n");
        return stringBuilder.toString();
    }

    // 标识位 消息头 消息体 检验码 标识位
    // 接收消息时：去掉标识位 -> 转义还原 -> 验证校验码 -> 解析消息
    public CommPacket(byte[] in) {
        if (in != null) {
            logger.info("PacketSize: " + in.length);
        }
        if (in == null || in.length < 2) {
            logger.info("check in failed: " + in);
            return;
        }
        if (in[0] != HEAD || in[in.length - 1] != TAIL) {
            logger.info("check HEAD/TAIL failed: " + UtilsConvert.byteToHex0x(in));
            return;
        }

        byte[] msg = decodeSpecialByte(in);
        msgId = UtilsConvert.formInt(msg, MSG_ID_INDEX, MSG_ID_LEN);

        int msgBodyAttr = UtilsConvert.formInt(msg, MSG_BODY_ATTR_INDEX, MSG_BODY_ATTR_LEN);
        mbaReserved = UtilsConvert.formIntForBitsBigEndian(msgBodyAttr, MBA_RSV_INDEX, MBA_RSV_LEN);
        isMultiPack = UtilsConvert.formIntForBitsBigEndian(msgBodyAttr, MBA_IS_MULTI_INDEX,
                MBA_IS_MULTI_LEN);
        encryptMode = UtilsConvert.formIntForBitsBigEndian(msgBodyAttr, MBA_ENCRYPT_MODE_INDEX,
                MBA_ENCRYPT_MODE_LEN);
        msgBodyLength = UtilsConvert.formIntForBitsBigEndian(msgBodyAttr, MBA_MSG_BODY_LEN_INDEX,
                MBA_MSG_BODY_LEN_LEN);

        termPhoneNumber = UtilsConvert.bcd2Str(
                UtilsConvert.subBytes(msg, TERM_PHONE_NUMBER_INDEX, TERM_PHONE_NUMBER_LEN));
        msgSeq = UtilsConvert.formInt(msg, MSG_SEQ_INDEX, MSG_SEQ_LEN);
        byte calculatedCheckCode = 0;
        if (isMultiPack == 1) {
            // 加一个checkcode字节
            if (msg.length != MSG_BODY_HAVE_MULTI_PACK_INDEX + msgBodyLength + 1)
                return;
            multiPackTotalNum = UtilsConvert.formInt(msg, MSG_MULTI_PACK_NUM_INDEX,
                    MSG_MULTI_PACK_NUM_LEN);
            multiPackSeq = UtilsConvert.formInt(msg, MSG_MULTI_PACK_SEQ_INDEX,
                    MSG_MULTI_PACK_SEQ_LEN);
            msgBody = UtilsConvert.subBytes(msg, MSG_BODY_HAVE_MULTI_PACK_INDEX, msgBodyLength);
            checkCode = UtilsConvert.subBytes(msg, MSG_BODY_HAVE_MULTI_PACK_INDEX + msgBodyLength,
                    1)[0];
            calculatedCheckCode = getCheckCode(
                    UtilsConvert.subBytes(msg, 0, MSG_BODY_HAVE_MULTI_PACK_INDEX + msgBodyLength));
        } else {
            if (msg.length != MSG_BODY_NO_MULTI_PACK_INDEX + msgBodyLength + 1)
                return;
            msgBody = UtilsConvert.subBytes(msg, MSG_BODY_NO_MULTI_PACK_INDEX, msgBodyLength);
            checkCode = UtilsConvert.subBytes(msg, MSG_BODY_NO_MULTI_PACK_INDEX + msgBodyLength,
                    1)[0];
            calculatedCheckCode = getCheckCode(
                    UtilsConvert.subBytes(msg, 0, MSG_BODY_NO_MULTI_PACK_INDEX + msgBodyLength));
        }

        // logd(termPhoneNumber);
        if (checkCode != calculatedCheckCode) {
            logd("checkcode check failed!");
            logger.info("checkcode check failed: " + UtilsConvert.byteToHex0x(in));
            msgBody = null;
            return;
        }
    }

    public boolean isValidPacket() {
        return (msgBody != null);
    }

    public static byte[] decodeSpecialByte(byte[] src) {
        if (src == null)
            return null;

        byte[] output = new byte[src.length];

        int j = 0;
        for (int i = 0; i < src.length; i++) {
            if (i == src.length - 1) {
                output[j++] = src[i];
            } else {
                if (src[i] == 0x7d && src[i + 1] == 0x01) {
                    output[j++] = 0x7d;
                    i++;
                } else if (src[i] == 0x7d && src[i + 1] == 0x02) {
                    output[j++] = 0x7e;
                    i++;
                } else
                    output[j++] = src[i];
            }
        }
        // remove the head and tail 0x7E
        byte[] ret = UtilsConvert.subBytes(output, 1, j - 2);
        return ret;
    }

    public static byte[] encodeSpecialByte(byte[] src) {
        if (src == null)
            return null;

        byte[] output = new byte[src.length * 2 + 2];

        int j = 0;
        output[j++] = 0x7e;
        for (int i = 0; i < src.length; i++) {
            if (src[i] == 0x7e) {
                output[j++] = 0x7d;
                output[j++] = 0x02;
            } else if (src[i] == 0x7d) {
                output[j++] = 0x7d;
                output[j++] = 0x01;
            } else
                output[j++] = src[i];
        }
        output[j++] = 0x7e;

        byte[] ret = UtilsConvert.subBytes(output, 0, j);
        return ret;
    }

    public static byte getCheckCode(byte[] src) {
        if (src == null || src.length < 2)
            return 0;

        byte checkCode = src[0];
        for (int i = 1; i < src.length; i++) {
            checkCode ^= src[i];
        }

        return checkCode;
    }

    //  发送消息时：消息封装 -> 计算并填充校验码 -> 转义 -> 添加标识位；
    public static byte[] formResponsePacket(int msgId, String termPhoneNumber, int seq,
            byte[] msgBody) {
        if (msgBody == null)
            return null;
        byte[] msgIdBytes = UtilsConvert.int2BytesBigEndian(msgId, 2);

        int msgBodyAttr = 0;
        int msgBodyLength = msgBody.length;
        int encryptMode = 0;
        int isMultiPack = 0;
        int mbaReserved = 0;
        msgBodyAttr = UtilsConvert.fillBitsBigEndian(msgBodyAttr, msgBodyLength,
                MBA_MSG_BODY_LEN_INDEX + 1 - MBA_MSG_BODY_LEN_LEN, MBA_MSG_BODY_LEN_LEN);
        msgBodyAttr = UtilsConvert.fillBitsBigEndian(msgBodyAttr, encryptMode,
                MBA_ENCRYPT_MODE_INDEX + 1 - MBA_ENCRYPT_MODE_LEN, MBA_ENCRYPT_MODE_LEN);
        msgBodyAttr = UtilsConvert.fillBitsBigEndian(msgBodyAttr, isMultiPack,
                MBA_IS_MULTI_INDEX + 1 - MBA_IS_MULTI_LEN, MBA_IS_MULTI_LEN);
        msgBodyAttr = UtilsConvert.fillBitsBigEndian(msgBodyAttr, mbaReserved,
                MBA_RSV_INDEX + 1 - MBA_RSV_LEN, MBA_RSV_LEN);
        byte[] msgBodyAttrBytes = UtilsConvert.int2BytesBigEndian(msgBodyAttr, 2);
        byte[] termPhoneMumberBytes = UtilsConvert.str2Bcd(termPhoneNumber);
        byte[] msgSeqBytes = UtilsConvert.int2BytesBigEndian(seq, 2);

        byte[] msgHeaderAndMsgBody = UtilsConvert.combineBytes(msgIdBytes, msgBodyAttrBytes,
                termPhoneMumberBytes, msgSeqBytes, msgBody);
        byte[] checkCodeBytes = new byte[] { getCheckCode(msgHeaderAndMsgBody) };
        byte[] msgOrg = UtilsConvert.combineBytes(msgHeaderAndMsgBody, checkCodeBytes);
        byte[] encodedMsg = encodeSpecialByte(msgOrg);

        return encodedMsg;
    }

    static void logd(Object s) {
        System.out.println(s + "");
    }
}
