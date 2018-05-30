package com.epcs.utils;

public class GPSDistance {
    /**
     * 通过GPS坐标计算两点间的距离（米）
     * 
     * @param long1
     * @param lat1
     * @param long2
     * @param lat2
     * @return
     */
    public static double Distance2(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径（米）
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(sa2) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    public static void main(String[] args) {
        // 31.178569,121.494594
        // 31.188565,121.492465
        double x = Distance2(121.494594, 31.178569, 121.492465, 31.188565);
        // double x = Distance2(120, 30, 121, 30);
        logd(x);
    }

    static void logd(Object s) {
        System.out.println(s + "");
    }
}
