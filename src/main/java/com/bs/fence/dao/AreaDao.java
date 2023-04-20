package com.bs.fence.dao;

import com.bs.fence.entity.Area;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-04-19-18:21
 */
@Mapper
public interface AreaDao {

    //根据id查询分区
    Area selectAreaById(Integer id);

    //根据分区名称查询分区信息
    Area selectAreaByName(String name);
}
