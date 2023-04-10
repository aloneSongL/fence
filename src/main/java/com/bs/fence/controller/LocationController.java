package com.bs.fence.controller;

import com.bs.fence.entity.Location;
import com.bs.fence.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-01-16:47
 */
@RequestMapping("/location")
@Controller
public class LocationController {

    @Resource
    private LocationService locationService;

    //插入位置信息，前端传来坐标字符串
    @PostMapping("/add/{id}")
    public String add(@PathVariable("id") Integer id, String position, Model model){
        locationService.addById(id, position);
        return selectAll(model);
    }

    //根据ID删除位置信息
    @GetMapping("/delete/{id}/{pageNo}")
    public String delete(@PathVariable("id") Integer id,
                         @PathVariable("pageNo") Integer pageNo, Model model){
        locationService.delete(id);
        return selectPage(pageNo, model);
    }

    //修改位置信息
    @PostMapping("/update/{id}/{pageNo}")
    public String update(@PathVariable("id") Integer id,
                         @PathVariable("pageNo") Integer pageNo, String description, Model model){
        locationService.update(id, description);
        return selectPage(pageNo, model);
    }

    //查询位置列表
    @GetMapping("/select")
    public String selectAll(Model model){
        locationService.selectAll(model);
        return "location_manage";
    }

    //插入地点
    @PostMapping("/insert")
    public String addPosition(Location location, Model model) {
        int result = locationService.insert(location);
        if(result > 0){
            locationService.selectAll(model);
            return "location_manage";
        }
        return "location_add";
    }

    //删除坐标
    @GetMapping("/delCoordinateById/{id}/{pageNo}")
    public String delCoordinateById(@PathVariable("id") Integer id,
                                    @PathVariable("pageNo") Integer pageNo, Model model) {
        locationService.delCoordinateById(id);
        return selectPage(pageNo, model);
    }

    //根据id修改是否监控
    @GetMapping("/isMonitorById/{isMonitor}/{id}/{pageNo}")
    public String isMonitorById(@PathVariable("id") Integer id,
                                @PathVariable("isMonitor") Character isMonitor,
                                @PathVariable("pageNo") Integer pageNo, Model model) {
        locationService.isMonitorById(id, isMonitor);

        return selectPage(pageNo, model);
    }

    //分页查询
    @GetMapping("/selectPage/{id}")
    public String selectPage(@PathVariable("id") Integer id, Model model) {
        locationService.selectPage(id, model);
        return "location_manage";
    }
}
