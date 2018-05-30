package com.epcs.utils.parser2.weightmodule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.epcs.utils.parser2.UtilsConvert;
import com.epcs.utils.parser2.UtilsTime;

//java -classpath Utils.jar com.epcs.utils.parser2.weightmodule.SimulateUdpWeightServer port=7081
public class SimulateUdpWeightServer {

    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(2);

    static class ReceiverHandler implements Runnable {

        static int threadNum = 0;
        private DatagramSocket mSocket;
        private byte[] buf = new byte[512];
        private DatagramPacket mDatagramPacket = new DatagramPacket(buf, buf.length);

        ReceiverHandler(DatagramSocket socket) {
            this.mSocket = socket;
        }

        @Override
        public void run() {

            threadNum++;
            while (true) {
                try {
                    mSocket.receive(mDatagramPacket);
                    logd(mDatagramPacket.getLength());
                    byte[] in = mDatagramPacket.getData();
                    DevInfoPacket packet = new DevInfoPacket(in);
                    if (!packet.isValidPacket())
                        continue;
                    logd(packet);

                    byte[] sendBuf = ServerDownPacket.setAll(packet, 116.443938, 39.979542, 1);

                    // 从服务器返回给客户端数据
                    SocketAddress sendAddress = mDatagramPacket.getSocketAddress();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length,
                            sendAddress);
                    mSocket.send(sendPacket);
                    logd("sendPacket:\n" + UtilsConvert.byteToHex0x(sendBuf));

                } catch (Exception e) {
                    logd(e.getStackTrace());
                    for (StackTraceElement s : e.getStackTrace()) {
                        logd(s.getLineNumber() + " " + " " + s.getMethodName() + " " + s);
                    }
                    try {
                        if (mSocket != null)
                            mSocket.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                }

            }
            threadNum--;

        }
    }

    final static String PORT_PREFIX = "port=";

    public static void main(String args[]) {

        DatagramSocket socket = null;
        int port = 7081;
        for (String arg : args) {
            if (arg.startsWith(PORT_PREFIX)) {
                String value = arg.substring(PORT_PREFIX.length()).trim();
                try {
                    port = Integer.valueOf(value);
                } catch (Exception e) {

                }
                logd(PORT_PREFIX + port);
            }
        }
        try {
            socket = new DatagramSocket(port);
            while (true) {
                if (ReceiverHandler.threadNum < 1) {
                    try {
                        mExecutorService.execute(new ReceiverHandler(socket));
                    } catch (Exception e) {
                        logd(e);
                    }
                }
                Thread.sleep(5000);
            }

        } catch (Exception e) {
            logd(e);
            if (socket != null)
                try {
                    socket.close();
                } catch (Exception e1) {
                    logd(e1);
                }
        }
    }

    static void logd(Object s) {
        System.out.println(UtilsTime.getTimestampString(System.currentTimeMillis()) + ": " + s);
    }

}
