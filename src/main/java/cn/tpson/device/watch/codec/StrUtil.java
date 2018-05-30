package cn.tpson.device.watch.codec;

import java.io.UnsupportedEncodingException;

public class StrUtil {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static boolean isEmpty(String obj) {
		if (obj == null || obj.equals("")) {
			return true;
		}
		return false;
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String bytesToString(byte[] bytes) {
	    if (bytes == null || bytes.length == 0)
	        return "";

        String ret = "";
        try {
            ret = new String(bytes, "utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
/*
        String ret = "";
		int len = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			if (bytes[i] != 0) {
				len = i + 1;
				break;
			}
		}
		if (len > 0) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(bytes, 0, len);
			try {
				ret = new String(out.toByteArray(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception ex) {
				}
			}
		}
		return ret;*/
	}

	public static byte[] hexStringToByte(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return baKeyword;
	}

	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "UTF-8");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) {
		/*System.out.println(StrUtil.bytesToHex("20150313180820".getBytes()));
		System.out.println(StrUtil.hexStringToString(
				"404723402C5630312C312C3130383130313731323030373138302C393436303034303131343931353136322C405223400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));*/


		byte[] b = {97,97,97,0,0,0,0,0,0,0,0,0,0};
        System.out.println(new String(b));
        System.out.println(new String(b).trim().length());
	}

}
