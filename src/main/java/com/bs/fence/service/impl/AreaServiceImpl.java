package com.bs.fence.service.impl;

import com.bs.fence.dao.AreaDao;
import com.bs.fence.entity.Area;
import com.bs.fence.service.AreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-04-19-18:31
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Resource
    AreaDao areaDao;

    @Override
    public Area selectAreaById(Integer id) {
        return areaDao.selectAreaById(id);
    }

    @Override
    public Area selectAreaByName(String name) {
        return areaDao.selectAreaByName(name);
    }
}
