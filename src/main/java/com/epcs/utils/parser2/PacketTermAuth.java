package com.epcs.utils.parser2;

import java.io.UnsupportedEncodingException;

public class PacketTermAuth {
    public final static int MSG_ID = 0x0102;

    public String authKey = null;

    public PacketTermAuth(byte[] msgBody) {
        try {
            authKey = new String(msgBody, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidPacket() {
        return (authKey != null);
    }

    public static byte[] simulate() {

        // if (authKey != null)
        // return authKey.getBytes();
        // else
        return ("key" + System.currentTimeMillis() % 100000).getBytes();
    }
}
