package com.bs.fence.service;

import com.bs.fence.dto.Point;
import com.bs.fence.dto.UserDistribution;
import com.bs.fence.dto.WxDto;
import com.bs.fence.entity.Location;
import com.bs.fence.entity.Trail;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-13:20
 */
public interface TrailService {

    //查询所有轨迹
    int selectAll(Model model);

    //查询某一区域的轨迹
    int selectById(Model model, Integer locationId);

    //分页查询
    int selectPage(Integer pageNo, Model model);

    //删除轨迹
    int delete(Long id);

    //插入记录
    int add(Trail trail);

    //获取用户在哪些围栏内
    List<Location> inPolygon(Point point);

    //判断用户是否通过某围栏
    WxDto monitor(String userId, String locations, Point point);

    //查询每个用户最新的一条记录
    int selectAllNew(Model model);

    //查询某个用户最新的一条记录
    int selectNewById(Long userId, Model model);

    //查询某一个时间段内的记录
    int selectInTime(Model model);

    //统计目前学校各区人员分布
    int selectSortCount(Model model);

    //统计学校某一时间人员分布
}
