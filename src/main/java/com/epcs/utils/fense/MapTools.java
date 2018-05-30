package com.epcs.utils.fense;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by mx on 2017/10/12.
 */
public class MapTools {
    public static List<Point2D.Double> jsonArrayGpsList(String jsonString) {
        if (jsonString == null)
            return null;
        List<Point2D.Double> pts = new ArrayList<Point2D.Double>();

        JSONArray jsonArray = JSONArray.parseArray(jsonString);
        if (jsonArray == null) {
            System.out.println(jsonString);
            return pts;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            Point2D.Double gps = new Point2D.Double();
            JSONObject obj = jsonArray.getJSONObject(i);
            gps.x = obj.getDoubleValue("lng");
            gps.y = obj.getDoubleValue("lat");
            pts.add(gps);
        }

        return pts;
    }

    public static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts) {
        java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();
        Point2D.Double first = pts.get(0);
        p.moveTo(first.x, first.y);
        pts.remove(0);
        for (Point2D.Double d : pts) {
            p.lineTo(d.x, d.y);
        }

        p.lineTo(first.x, first.y);

        p.closePath();

        return p.contains(point);

    }

    public static void main(String[] args) {
        Point2D.Double point1 = new Point2D.Double(112.860215, 28.183855);
        Point2D.Double point2 = new Point2D.Double(112.879645, 28.174594);
        String jsonString = "[{\"lng\":112.859413,\"lat\":28.196188},{\"lng\":112.845041,\"lat\":28.178995},{\"lng\":112.864731,\"lat\":28.169315},{\"lng\":112.87623,\"lat\":28.178104},{\"lng\":112.874792,\"lat\":28.189057}]";
        List<Point2D.Double> list = jsonArrayGpsList(jsonString);
        if (IsPtInPoly(point1, list)) {
            System.out.println("点在多边形内");
        } else {
            System.out.println("点在多边形外");
        }
    }

}
