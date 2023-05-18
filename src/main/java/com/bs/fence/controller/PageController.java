package com.bs.fence.controller;

import com.bs.fence.dto.LocationPoint;
import com.bs.fence.entity.Area;
import com.bs.fence.entity.Location;
import com.bs.fence.service.AreaService;
import com.bs.fence.service.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-23:56
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @Resource
    AreaService areaService;
    @Resource
    LocationService locationService;

    //登录页面
    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    //百度地图页面，显示用户位置和反绘制围栏
    @RequestMapping("/map")
    public String map(HttpServletRequest request, Model model){
        //拿到用户位置信息JSON串
        String userLocation = (String)request.getSession().getAttribute("userLocation");
        if(userLocation != null) {
            model.addAttribute("userLocation", userLocation);
        }
        // 拿到围栏数据
        String location = (String)request.getSession().getAttribute("location");
        if(location != null) {
            model.addAttribute("locations", location);
        }
        return "map";
    }

    //电子围栏绘制页面
    @SneakyThrows
    @RequestMapping("/map/{id}/{pageNo}")
    public String map(@PathVariable("id") Integer id,
                      @PathVariable("pageNo") Integer pageNo,
                      HttpServletRequest request, Model model){
        List<Location> locations = (List<Location>)request.getSession().getAttribute("locationList");
        List<List<LocationPoint>> points = (List<List<LocationPoint>>) request.getSession().getAttribute("points");
        ObjectMapper mapper = new ObjectMapper();
        String locationListString = mapper.writeValueAsString(locations);
        model.addAttribute("locations", locationListString);
        model.addAttribute("locationId", id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("points", points);
        return "map";
    }

    //电子围栏添加页面
    @RequestMapping("/add")
    public String add(Model model){
        List<Area> areas = areaService.selectAllArea();
        model.addAttribute("areas", areas);
        return "location_add";
    }

    //电子围栏编辑页面
    @RequestMapping("/locationEdit/{id}/{pageNo}")
    public String locationEdit(@PathVariable("id") Integer id,
                       @PathVariable("pageNo") Integer pageNo, Model model){
        Location location = locationService.selectLocation(id);
        model.addAttribute("location", location);
        List<Area> areas = areaService.selectAllArea();
        model.addAttribute("areas", areas);
        return "location_edit";
    }

    //用户添加页面
    @RequestMapping("/userAdd")
    public String userAdd(){
        return "user_add";
    }

    //用户编辑页面
    @RequestMapping("/userEdit/{id}/{pageNo}")
    public String userEdit(@PathVariable("id") Integer id,
                       @PathVariable("pageNo") Integer pageNo, Model model){
        model.addAttribute("userId", id);
        model.addAttribute("pageNo", pageNo);
        return "user_edit";
    }

    /**
     * 围栏反绘制
     * @param id  围栏 id
     * @return
     */
    @GetMapping("/queryFence/{id}/{pageNo}")
    public String queryFence(@PathVariable("id") Integer id,
                             @PathVariable("pageNo") Integer pageNo,
                             Model model) throws JsonProcessingException {
        Location location = locationService.selectLocation(id);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(location);
        model.addAttribute("fence", jsonString);
        model.addAttribute("pageNo", pageNo);
        return "map";
    }

}
