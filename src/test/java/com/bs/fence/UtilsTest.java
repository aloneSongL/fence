package com.bs.fence;

import com.bs.fence.dto.Point;
import com.bs.fence.utils.CoordinateChangeUtil;
import com.bs.fence.utils.MapUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-19:20
 */
@SpringBootTest
public class UtilsTest {

    @Test
    public static void main(String[] args) {
        Point point = new Point(1.0,1.0);
        Point point1 = new Point(0.0,0.0);
        Point point2 = new Point(2.0,0.0);
        Point point3 = new Point(0.0,2.0);
        Point point4 = new Point(2.0,2.0);
        ArrayList<Point> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        boolean inPolygon = MapUtils.isInPolygon(points, point);
        System.out.println(inPolygon);
    }

    @Test
    public void test2(){
        Point point = new Point(116.401264,39.917622);
        Point point1 = new Point(116.40086,39.917986);
        Point point2 = new Point(116.401493,39.918006);
        Point point3 = new Point(116.401529,39.917287);
        Point point4 = new Point(116.400914,39.917273);
        ArrayList<Point> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        double plon= 116.401264;
        double plat= 39.917622;
        double[] lon={116.40086,116.401493,116.401529,116.400914};
        double[] lat={39.917986,39.918006,39.917287,39.917273};
        System.out.println(MapUtils.isInPolygon(points, point));
    }

    @Test
    public void pointChange(){

//        double[] doubles = CoordinateChangeUtil.transformWGS84ToBD09(116.03516628689236, 28.689069010416667);
        double[] doubles = CoordinateChangeUtil.transformGCJ02ToBD09(116.0351904296875,28.68912109375);
        System.out.println(doubles[0] + "," + doubles[1]);
    }
}
