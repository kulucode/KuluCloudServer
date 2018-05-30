package com.epcs.utils.parser2.weightmodule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.epcs.utils.parser2.UtilsConvert;
import com.epcs.utils.parser2.UtilsTime;

//java -classpath Utils.jar com.epcs.utils.parser2.weightmodule.SimulateTcpWeightServer port=7081
public class SimulateTcpWeightServer {

    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(100);
    private final static boolean SEND_READ_PARA = true;

    static class ReceiverHandler implements Runnable {

        private static final long serialVersionUID = 0;

        private Socket mSocket;
        private InputStream mInputStream;
        private OutputStream mOutputStream;

        ReceiverHandler(Socket socket) {
            this.mSocket = socket;

            try {
                this.mInputStream = socket.getInputStream();
                this.mOutputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            byte[] b = new byte[1024];

            while (true) {
                try {
                    int count = mInputStream.read(b);
                    if (count == -1) {
                        // logd(mSocket + ":socket stream closed");
                        try {
                            if (mSocket != null)
                                mSocket.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
                    byte[] in = UtilsConvert.subBytes(b, 0, count);
                    DevInfoPacket packet = new DevInfoPacket(in);
                    if (!packet.isValidPacket())
                        continue;
                    logd(packet);

                    byte[] responseMsg = null;

                    /**
                     * send read Socket
                     */
                    if (SEND_READ_PARA) {
                        if (responseMsg != null) {
                            mOutputStream.write(responseMsg);
                            mOutputStream.flush();
                            logd("sendResponse:\n" + UtilsConvert.byteToHex0x(responseMsg));
                        }
                    }

                } catch (Exception e) {
                    logd(e.getStackTrace());
                    for(StackTraceElement s:e.getStackTrace()){
                        logd(s.getLineNumber()+" "+" "+s.getMethodName()+" "+s);
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

        }

    }

    final static String PORT_PREFIX = "port=";

    public static void main(String args[]) {

        ServerSocket server = null;
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
            server = new ServerSocket(port);
            while (true) {
                Socket socket = null;
                try {
                    // logd("Listening on port 7080..");
                    socket = server.accept();
                    socket.setTcpNoDelay(true);
                    socket.setSendBufferSize(1);
                    InetAddress addr = socket.getInetAddress();
                    logd(socket + " connected");
                    // logd("interested socket:" + socket + " connected");
                    mExecutorService.execute(new ReceiverHandler(socket));
                    // }
                } catch (Exception e) {
                    logd(e);
                }
            }

        } catch (Exception e) {
            logd(e);
            if (server != null)
                try {
                    server.close();
                } catch (Exception e1) {
                    logd(e1);
                }
        }
    }

    static void logd(Object s) {
        System.out.println(UtilsTime.getTimestampString(System.currentTimeMillis()) + ": " + s);
    }

}
