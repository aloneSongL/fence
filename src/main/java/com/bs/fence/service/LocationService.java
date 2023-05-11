package com.bs.fence.service;

import com.bs.fence.entity.Location;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-01-16:39
 */
public interface LocationService {

    //插入一个位置信息
    int insert(Location location);

    int addById(Integer id, String position);

    //根据ID删除位置信息
    int delete(int id);

    //修改位置信息
    int update(Integer id, String description);

    //查询位置列表
    int selectAll(Model model, HttpServletRequest httpServletRequest);

    //根据id删除坐标
    int delCoordinateById(Integer id);

    //根据id修改是否监控
    int isMonitorById(Integer id, Character isMonitor);

    //分页查询
    int selectPage(Integer pageNo, Model model, HttpServletRequest httpServletRequest);

    //根据id查询位置信息
    Location selectLocation(Integer id);

    //查询被监控的位置信息
    List<Location> selectAll();

    //根据名称查找围栏信息
    Integer selectByName(String name);

    //根据名称查找电子围栏信息，模糊查询
    Integer likeSelect(String locationName, String areaName, HttpServletRequest request, Model model);
}
