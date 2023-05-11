package com.bs.fence.dao;

import com.bs.fence.entity.Location;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-01-16:07
 */
@Mapper
public interface LocationDao {

    //插入一个位置信息
    int insert(Location location);

    //根据ID插入坐标
    int addById(Location location);

    //根据ID删除位置信息
    int delete(Integer id);

    //修改位置信息
    int update(Location location);

    //查询位置列表
    List<Location> selectAll();

    //根据id删除坐标
    int delCoordinateById(Integer id);

    //根据id修改是否监控
    int isMonitorById(Location location);

    //根据id查询位置信息
    Location selectLocation(Integer id);

    //查询被监控的位置信息
    List<Location> queryAll();

    //根据名称查找围栏id
    Integer selectByName(String name);

    //根据名称模糊查询围栏信息
    List<Location> likeSelectByName(Location location);
}
