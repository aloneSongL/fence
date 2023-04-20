package com.bs.fence.service;


import com.bs.fence.entity.Area;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-04-19-18:30
 */
public interface AreaService {

    //根据id查询分区
    Area selectAreaById(Integer id);

    //根据分区名称查询分区信息
    Area selectAreaByName(String name);
}
