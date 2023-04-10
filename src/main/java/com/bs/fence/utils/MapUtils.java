package com.bs.fence.utils;

import com.bs.fence.dto.Point;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-17:50
 */
public class MapUtils {

    //将坐标字符串解析成坐标列表
    public static List<Point> splitToPoints(String coordinate) {
        ArrayList<Point> points = new ArrayList<>();
        Boolean is_Loop = true;
        while(is_Loop){
            //字符串中首次出现,的位置
            int index1 = coordinate.indexOf(",");
            if(index1 >= 0){
                //截取x
                String string_lon = coordinate.substring(0, index1);
                Double lon = Double.valueOf(string_lon);
                //字符串中首次出现;的位置
                int index2 = coordinate.indexOf(";");
                if(index2 == -1){
                    String string_lat = coordinate.substring(index1 + 1);
                    Double lat = Double.valueOf(string_lat);
                    Point point = new Point(lon, lat);
                    points.add(point);
                    break;
                }
                //截取y
                String string_lat = coordinate.substring(index1 + 1, index2);
                Double lat = Double.valueOf(string_lat);

                Point point = new Point(lon, lat);
                points.add(point);

                //截取剩余部分重新赋值
                coordinate = coordinate.substring(index2 + 1);
            }else{
                is_Loop = false;
            }
        }
        return points;
    }


    /**
    @Description 判断某个点是否在某个区域内部
    @since 2023-03-17 18-07
    */
    public static Boolean isInPolygon(List<Point> points, Point point) {
        Point[] pointList = points.toArray(new Point[points.size()]);
        return isPointInPolygon(point, pointList);
    }

    /**
     * 判断 点 是否在 多边形 内(基本思路是用 光线投射 法)
     */
    public static Boolean isPointInPolygon(Point point, Point[] pointList) {
        // 判断第一个点与最后一个点是否相同
        if (pointList != null && pointList.length > 0
                && pointList[pointList.length - 1].equals(pointList[0])) {
            pointList = Arrays.copyOf(pointList, pointList.length - 1);
        }

        // 多边形的边数和点数是相同的
        int pointCount = pointList.length;

        // 首先判断点是否在多边形的外包矩形内，如果在，则进一步判断，否则返回false
        if (!isPointInRectangle(point, pointList)) {
            return false;
        }

        // 如果点与多边形的其中一个顶点重合，那么直接返回true
        for (int i = 0; i < pointCount; i++) {
            if (point.equals(pointList[i])) {
                return true;
            }
        }

        // 射线与边相交点个数
        int intersectPointCount = 0;
        // X轴射线与多边形的交点权值
        float intersectPointWeights = 0;
        // 浮点类型计算时候与0比较时候的容差
        double precision = 2e-10;

        // 边P1P2的两个端点
        Point point1 = pointList[0];
        Point point2;

        // 循环判断所有的边
        for (int i = 1; i <= pointCount; i++) {
            point2 = pointList[i % pointCount];

            // point的纬度 在 边的两点纬度范围外，永远不相交
            if (point.getLatitude() < Math.min(point1.getLatitude(), point2.getLatitude())
                    || point.getLatitude() > Math.max(point1.getLatitude(), point2.getLatitude())) {
                point1 = point2;
                continue;
            }

            // 此处判断射线与边相交
            // point的纬度 在 边的两点纬度范围内
            if (point.getLatitude() > Math.min(point1.getLatitude(), point2.getLatitude())
                    && point.getLatitude() < Math.max(point1.getLatitude(), point2.getLatitude())) {

                // 1. 若 边P1P2 是垂直的，直接比较经度
                if (point1.getLongitude().equals(point2.getLongitude())) {
                    if (point.getLongitude().equals(point1.getLongitude())) {
                        // 若点在垂直的边P1P2上，则点在多边形内
                        return true;
                    } else if (point.getLongitude() < point1.getLongitude()) {
                        // 若点在在垂直的边P1P2左边，则点与该边必然有交点
                        ++intersectPointCount;
                    }
                }// 2. 若边P1P2是斜线
                else {

                    // 2.1 点point的x坐标在点P1和P2的左侧
                    if (point.getLongitude() <= Math.min(point1.getLongitude(), point2.getLongitude())) {
                        ++intersectPointCount;
                    }

                    // 2.2 点point的x坐标在点P1和P2的x坐标中间, 此时计算夹角来判断
                    else if (point.getLongitude() > Math.min(point1.getLongitude(), point2.getLongitude())
                            && point.getLongitude() < Math.max(point1.getLongitude(), point2.getLongitude())) {
                        double slopeDiff = 0.0d;

                        // 2.2.1 降的，向右下倾斜
                        if (point1.getLatitude() > point2.getLatitude()) {
                            slopeDiff = (point.getLatitude() - point2.getLatitude()) / (point.getLongitude() - point2.getLongitude())
                                    - (point1.getLatitude() - point2.getLatitude()) / (point1.getLongitude() - point2.getLongitude());
                        }

                        // 2.2.2 升的，向右上倾斜
                        else {
                            slopeDiff = (point.getLatitude() - point1.getLatitude()) / (point.getLongitude() - point1.getLongitude())
                                    - (point2.getLatitude() - point1.getLatitude()) / (point2.getLongitude() - point1.getLongitude());
                        }

                        if (slopeDiff > 0) {
                            if (slopeDiff < precision) {// 由于double精度在计算时会有损失，故匹配一定的容差。经试验，坐标经度可以达到0.0001
                                // 点在斜线P1P2上
                                return true;
                            } else {
                                // 点与斜线P1P2有交点
                                intersectPointCount++;
                            }
                        }
                    }
                }
            } else {

                // 边P1P2水平
                if (point1.getLatitude() == point2.getLatitude()) {
                    if (point.getLongitude() <= Math.max(point1.getLongitude(), point2.getLongitude())
                            && point.getLongitude() >= Math.min(point1.getLongitude(), point2.getLongitude())) {
                        // 若点在水平的边P1P2上，则点在多边形内
                        return true;
                    }
                }

                // 判断点通过多边形顶点
                if (((point.getLatitude().equals(point1.getLatitude()) && point.getLongitude() < point1.getLongitude()))
                        || (point.getLatitude().equals(point2.getLatitude()) && point.getLongitude() < point2.getLongitude())) {
                    if (point2.getLatitude() < point1.getLatitude()) {
                        intersectPointWeights += -0.5;
                    } else if (point2.getLatitude() > point1.getLatitude()) {
                        intersectPointWeights += 0.5;
                    }
                }
            }
            point1 = point2;
        }

        if ((intersectPointCount + Math.abs(intersectPointWeights)) % 2 == 0) {// 偶数在多边形外
            return false;
        } else { // 奇数在多边形内
            return true;
        }
    }

    /**
     * 根据这组坐标，画一个矩形，然后得到这个矩形西南角的顶点坐标
     */
    private static Point getNorthEastPoint(Point[] pointList) {
        double maxLng = 0.0d, maxLat = 0.0d;
        for (Point point : pointList) {
            double lng = point.getLongitude();
            double lat = point.getLatitude();
            if (lng > maxLng) {
                maxLng = lng;
            }
            if (lat > maxLat) {
                maxLat = lat;
            }
        }
        return new Point(maxLng, maxLat);
    }

    /**
     * 判断 点 是否在 矩形框 内
     */
    public static Boolean isPointInRectangle(Point point, Point[] pointList) {
        // 西南角点 和 东北角点
        Point southWestPoint = getSouthWestPoint(pointList);
        Point northEastPoint = getNorthEastPoint(pointList);

        return (point.getLongitude() >= southWestPoint.getLongitude() && point.getLongitude() <= northEastPoint.getLongitude()
                && point.getLatitude() >= southWestPoint.getLatitude() && point.getLatitude() <= northEastPoint.getLatitude());
    }

    /**
     * 根据这组坐标，画一个矩形，然后得到这个矩形西南角的顶点坐标
     */
    private static Point getSouthWestPoint(Point[] pointList) {
        double minLng = 0.0d, minLat = 0.0d;
        for (Point point : pointList) {
            double lng = point.getLongitude();
            double lat = point.getLatitude();
            if (lng < minLng) {
                minLng = lng;
            }
            if (lat < minLat) {
                minLat = lat;
            }
        }
        return new Point(minLng, minLat);
    }
}
