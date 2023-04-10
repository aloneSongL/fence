package com.bs.fence.dao;

import com.bs.fence.entity.Trail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-13:01
 */
@Mapper
public interface TrailDao {

    //查询所有轨迹
    List<Trail> selectAll();

    //查询某一区域的轨迹
    List<Trail> selectById(Integer locationId);

    //删除轨迹
    int delete(Long id);

    //插入记录
    int add(Trail trail);
}
