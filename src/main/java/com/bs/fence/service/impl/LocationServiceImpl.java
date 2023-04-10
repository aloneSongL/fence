package com.bs.fence.service.impl;

import com.bs.fence.dao.LocationDao;
import com.bs.fence.entity.Location;
import com.bs.fence.dto.Page;
import com.bs.fence.service.LocationService;
import com.bs.fence.utils.DtoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-01-16:42
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Resource
    private LocationDao locationDao;

    @Override
    public int insert(Location location) {
        if("".equals(location.getDescription())) {
            return -1;
        }
        return locationDao.insert(location);
    }

    @Override
    public int addById(Integer id, String position) {
        int index = position.lastIndexOf(";");
        String substring = position.substring(0,index);
        Location location = new Location(id, substring, null);
        return locationDao.addById(location);
    }

    @Override
    public int delete(int id) {
        return locationDao.delete(id);
    }

    @Override
    public int update(Integer id, String description) {
        Location location = new Location();
        location.setId(id);
        location.setDescription(description);
        return locationDao.update(location);
    }

    @Override
    public int selectAll(Model model) {
        PageHelper.startPage(1, 8);
        List<Location> locations = locationDao.selectAll();
        PageInfo pageInfo = new PageInfo(locations);
        Page page = DtoUtils.Pages(pageInfo);
        if(locations.size() == 0){
            return 0;
        }
        model.addAttribute("locations", locations);
        model.addAttribute("page", page);
        return 1;
    }

    //删除坐标，同时将该位置改为不监控
    @Override
    public int delCoordinateById(Integer id) {
        Location location = new Location();
        location.setIsMonitor('0');
        location.setId(id);
        locationDao.isMonitorById(location);
        return locationDao.delCoordinateById(id);
    }

    @Override
    public int isMonitorById(Integer id, Character isMonitor) {
        Location location = new Location();
        location.setId(id);
        location.setIsMonitor(isMonitor);
        return locationDao.isMonitorById(location);
    }

    @Override
    public int selectPage(Integer pageNo, Model model) {
        if(pageNo == 0){
            PageHelper.startPage(1, 8);
        }else {
            PageHelper.startPage(pageNo, 8);
        }
        List<Location> locations = locationDao.selectAll();
        PageInfo pageInfo = new PageInfo(locations);
        Page page = DtoUtils.Pages(pageInfo);
        if(locations.size() == 0){
            return 0;
        }
        model.addAttribute("locations", locations);
        model.addAttribute("page", page);
        return 1;
    }

    @Override
    public String selectLocation(Integer id) {
        return locationDao.selectLocation(id);
    }

    @Override
    public List<Location> selectAll() {
        return locationDao.queryAll();
    }


}
