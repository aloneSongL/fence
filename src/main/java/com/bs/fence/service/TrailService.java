package com.bs.fence.service;

import com.bs.fence.dto.*;
import com.bs.fence.entity.Location;
import com.bs.fence.entity.Trail;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-13:20
 */
public interface TrailService {

    //查询所有轨迹
    int selectAll(Model model, HttpServletRequest request);

    //分页查询
    int selectPage(Integer pageNo, HttpServletRequest request, Model model);

    //删除轨迹
    int delete(Long id);

    //插入记录
    int add(Trail trail);

    //获取用户在哪些围栏内
    List<Location> inPolygon(String userId, Point point);

    //判断用户是否通过某围栏
    WxDto monitor(String userId, String locations, Point point);

    //统计目前学校各区人员分布
    int selectSortCount(Model model);

    //查询某个用户某个时间段在某处的记录
    int likeSelect(String location, String userName, String beforeTime, String afterTime, HttpServletRequest request, Model model);

    //查询所有用户目前所处位置信息
    int selectAllUserNow(HttpServletRequest request);

    // 根据用户ID查询所有轨迹
    List<Route> selectPath(Long userId);

    //查询所有轨迹信息
    List<TrailDto> selectAllDto();
}
