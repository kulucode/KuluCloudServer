package com.epcs.utils.fense;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Fense {
	final static boolean BOUND_VERTEX_OK = true; // 如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true

	public static boolean IsPtInPoly(double lat, double lon, List<Point2D.Double> pts) {
		return IsPtInPoly(new Point2D.Double(lat, lon), pts);
	}

	public static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts) {

		if (point == null || pts == null || pts.size() < 3)
			return true;
		int N = pts.size();
		int intersectCount = 0;// cross points count of x
		double precision = 2e-8; // 浮点类型计算时候与0比较时候的容差
		Point2D.Double p1, p2;// neighbour bound vertices
		Point2D.Double p = point; // 当前点

		p1 = pts.get(0);// left vertex
		for (int i = 1; i <= N; ++i) {// check all rays
			if (p.equals(p1)) {
				return BOUND_VERTEX_OK;// p is an vertex
			}

			p2 = pts.get(i % N);// right vertex
			if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {
				p1 = p2;
				continue;// next ray left point
			}

			if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {
				if (p.y <= Math.max(p1.y, p2.y)) {// x is before of ray
					if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {
						return BOUND_VERTEX_OK;
					}

					if (p1.y == p2.y) {// ray is vertical
						if (p1.y == p.y) {// overlies on a vertical ray
							return BOUND_VERTEX_OK;
						} else {// before ray
							++intersectCount;
						}
					} else {// cross point on the left side
						double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;// cross
																								// point
																								// of
																								// y
						if (Math.abs(p.y - xinters) < precision) {
							return BOUND_VERTEX_OK;
						}

						if (p.y < xinters) {// before ray
							++intersectCount;
						}
					}
				}
			} else {// special case when ray is crossing through the vertex
				if (p.x == p2.x && p.y <= p2.y) {// p crossing over p2
					Point2D.Double p3 = pts.get((i + 1) % N); // next vertex
					if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2;// next ray left point
		}

		if (intersectCount % 2 == 0) {// 偶数在多边形外
			return false;
		} else { // 奇数在多边形内
			return true;
		}

	}

	public static boolean IsPtInPoly(Point2D.Double point, String jsonString) {

		List<Point2D.Double> pts = new ArrayList<Point2D.Double>();

		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		for (int i = 0; i < jsonArray.size(); i++) {
			Point2D.Double gps = new Point2D.Double();
			JSONObject obj = jsonArray.getJSONObject(i);
			gps.x = obj.getDoubleValue("lat");
			gps.y = obj.getDoubleValue("lng");
			pts.add(gps);
		}

		return IsPtInPoly(point, pts);
	}

	public static List<Point2D.Double> jsonArray2GpsList(String jsonString) {

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
			gps.x = obj.getDoubleValue("lat");
			gps.y = obj.getDoubleValue("lng");
			pts.add(gps);
		}

		return pts;
	}

	public static void main(String[] args) {

		Point2D.Double point = new Point2D.Double(116.395072, 39.910605);

		List<Point2D.Double> pts = new ArrayList<Point2D.Double>();
		pts.add(new Point2D.Double(116.395, 39.910));
		pts.add(new Point2D.Double(116.394, 39.914));
		pts.add(new Point2D.Double(116.403, 39.920));
		pts.add(new Point2D.Double(116.402, 39.914));
		pts.add(new Point2D.Double(116.410, 39.913));

		String jsonString = "[{\"lon\":116.395,\"lat\":39.910},{\"lon\":116.394,\"lat\":39.914},{\"lon\":116.403,\"lat\":39.920},{\"lon\":116.402,\"lat\":39.914},{\"lon\":116.410,\"lat\":39.913}]";
		List<Point2D.Double> list = jsonArray2GpsList(jsonString);
		long time0 = System.currentTimeMillis();
		if (list != null)

			if (IsPtInPoly(point, list)) {
				System.out.println("点在多边形内");
			} else {
				System.out.println("点在多边形外");
			}
		System.out.println((System.currentTimeMillis() - time0) + "ms");
	}

	static void logd(Object o) {

		System.out.println(o + "");
	}
}
