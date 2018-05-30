package com.epcs.utils.parser2;

public class PacketGeneralResponse {
    public final static int RESPONSE_MSG_ID = 0x8001;// server->client
    public final static int MSG_ID = 0x0001;// client->server

    public final static byte SUCESS = 0;
    public final static byte FAILED = 1;
    public final static byte WRONG_MSG = 2;
    public final static byte UNSUPPORTED = 3;
    public final static byte ALERT_HANDLED = 4;

    public int answerSeq = 0;
    public int answerId = 0;
    public int answerResult = 0;

    final static int ANS_SEQ_INDEX = 0;
    final static int ANS_SEQ_LEN = 2;
    final static int ANS_ID_INDEX = ANS_SEQ_INDEX + ANS_SEQ_LEN;
    final static int ANS_ID_LEN = 2;
    final static int ANS_RESULT_INDEX = ANS_ID_INDEX + ANS_ID_LEN;
    final static int ANS_RESULT_LEN = 1;

    public PacketGeneralResponse(byte[] msgBody) {
        if (msgBody == null)
            return;
        answerSeq = UtilsConvert.formInt(msgBody, ANS_SEQ_INDEX, ANS_SEQ_LEN);
        answerId = UtilsConvert.formInt(msgBody, ANS_ID_INDEX, ANS_ID_LEN);
        answerResult = UtilsConvert.formInt(msgBody, ANS_RESULT_INDEX, ANS_RESULT_LEN);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" [TermGeneralResponse]:");
        stringBuilder.append(" answerSeq=" + answerSeq);
        stringBuilder.append(" answerId=" + answerId);
        stringBuilder.append(" answerResult=" + answerResult);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static byte[] formPlatformMsgBody(int termMsgSeq, int termMsgId, byte result) {
        byte[] seq = UtilsConvert.int2BytesBigEndian(termMsgSeq, 2);
        byte[] msgId = UtilsConvert.int2BytesBigEndian(termMsgId, 2);
        byte[] ret = UtilsConvert.combineBytes(seq, msgId, new byte[] { result });
        return ret;
    }
}
