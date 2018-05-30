package com.epcs;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WatchTestMain {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 9002);
			// Socket socket = new Socket("103.46.128.41", 36890);
			ArrayList<String> data = new ArrayList<String>();
			data.add("瞧瞧上..........");
			data.add("@G#@,V01,1,108101712007180,9460040114915162,@R#@");
			data.add("瞧瞧上...");
			data.add("@G#@,V01,1,108101712007180,9460040114915162,@R#@");
			data.add("瞧瞧上...");
			data.add("@G#@,V01,1,108101712007180,9460040114915162,@R#@");
			data.add("瞧瞧上...");
			data.add("@G#@,V01,1,108101712007180,9460040114915162,@R#@");
			data.add("瞧瞧上...");
			data.add("@G#@,V01,1,108101712007180,9460040114915162,@R#@");
			for (String str : data) {
				socket.getOutputStream().write(str.getBytes());
			}
			socket.getOutputStream().flush();
			InputStream input = socket.getInputStream();
			byte[] tempbytes = new byte[1024];
			int byteread = 0;
			while ((byteread = input.read(tempbytes)) != -1) {
				System.out.print("客户端:");
				System.out.write(tempbytes, 0, byteread);
				System.out.println();
			}
			Thread.sleep(1000 * 60 * 60 * 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
