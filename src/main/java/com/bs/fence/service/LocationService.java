package com.bs.fence.service;

import com.bs.fence.entity.Location;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;

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
    int selectAll(Model model);

    //根据id删除坐标
    int delCoordinateById(Integer id);

    //根据id修改是否监控
    int isMonitorById(Integer id, Character isMonitor);

    //分页查询
    int selectPage(Integer pageNo, Model model);

    //根据id查询位置信息
    String selectLocation(Integer id);

    //查询被监控的位置信息
    List<Location> selectAll();
}