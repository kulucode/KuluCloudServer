package com.epcs.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class SocketClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("192.168.8.7", 9001);
            OutputStream os = new BufferedOutputStream(s.getOutputStream());
            String info = "adc";
            byte[] bi= info.getBytes();
            os.write(bi);
            os.flush();
            os.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
