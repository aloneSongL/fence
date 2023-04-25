package com.bs.fence.service.impl;

import com.bs.fence.dao.LocationDao;
import com.bs.fence.dto.LocationDto;
import com.bs.fence.entity.Area;
import com.bs.fence.entity.Location;
import com.bs.fence.dto.PageDto;
import com.bs.fence.service.AreaService;
import com.bs.fence.service.LocationService;
import com.bs.fence.utils.PageUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    @Resource
    private AreaService areaService;

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
    public int selectAll(Model model, HttpServletRequest httpServletRequest) {
        List<Location> locationList = locationDao.selectAll();
        List<LocationDto> locationDtoList = toLocationDto(locationList);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("locationList", locationDtoList);
        selectPage(1, model, httpServletRequest);
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
    public int selectPage(Integer pageNo, Model model, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        List<Location> locationList = (List<Location>)session.getAttribute("locationList");
        if(locationList == null  || locationList.size() == 0){
            model.addAttribute("locations", null);
            return 1;
        }
        PageInfo pageInfo;
        if(pageNo == 0){
            pageInfo = PageUtils.pageHelperPlus(locationList, 1, 8);
        }else {
            pageInfo = PageUtils.pageHelperPlus(locationList, pageNo, 8);
        }

        PageDto page = PageUtils.getPage(pageInfo);
        model.addAttribute("locations", pageInfo.getList());
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

    @Override
    public Integer selectByName(String name) {
        return locationDao.selectByName(name);
    }

    @Override
    public Integer likeSelect(String locationName, String areaName, HttpServletRequest request, Model model) {
        Location location = new Location();
        if(locationName != null && locationName != ""){
            location.setDescription(locationName);
        }
        if(areaName != null && areaName != ""){
            Area area = areaService.selectAreaByName(areaName);
            if(area != null){
                location.setAreaId(area.getId());
            }else {
                location.setAreaId(0);
            }
        }
        List<Location> locations = locationDao.likeSelectByName(location);
        List<LocationDto> locationDtoList = toLocationDto(locations);
        HttpSession session = request.getSession();
        session.setAttribute("locationList", locationDtoList);
        selectPage(1, model, request);
        return 1;
    }

    public List<LocationDto> toLocationDto(List<Location> list){

        ArrayList<LocationDto> locationDtoList = new ArrayList<>();
        for(Location location : list){
            LocationDto locationDto = new LocationDto();
            if(location.getDescription() != null){
                locationDto.setDescription(location.getDescription());
            }
            if(location.getId() != null){
                locationDto.setId(location.getId());
            }
            if(location.getIsMonitor() != null){
                locationDto.setIsMonitor(location.getIsMonitor());
            }
            if(location.getCoordinate() != null){
                locationDto.setCoordinate(location.getCoordinate());
            }
            if(location.getAreaId() != null){
                Area area = areaService.selectAreaById(location.getAreaId());
                if(area.getName() != null){
                    locationDto.setAreaName(area.getName());
                }
            }
            locationDtoList.add(locationDto);
        }

        return locationDtoList;
    }
}
