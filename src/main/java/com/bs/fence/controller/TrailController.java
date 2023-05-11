package com.bs.fence.controller;

import com.bs.fence.dto.LocationPoint;
import com.bs.fence.dto.Point;
import com.bs.fence.dto.Route;
import com.bs.fence.dto.WxDto;
import com.bs.fence.service.TrailService;
import com.bs.fence.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-12:50
 */
@Controller
@RequestMapping("/trail")
public class TrailController {

    @Resource
    TrailService trailService;
    @Resource
    UserService userService;

    @GetMapping("/select")
    public String select(Model model, HttpServletRequest request){
        trailService.selectAll(model, request);
        return "trail";
    }

    @GetMapping("/selectPage/{pageNo}")
    public String selectPage(@PathVariable("pageNo")    Integer pageNo,
                             HttpServletRequest request, Model model){
        trailService.selectPage(pageNo, request,  model);
        return "trail";
    }

    @GetMapping("/selectPath/{userId}")
    public String selectPath(@PathVariable("userId") Long userId, HttpServletRequest request) throws JsonProcessingException {
        List<Route> routes = trailService.selectPath(userId);
        List<List<LocationPoint>> points = routes.stream().map(Route::getPoints).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(points);
        request.getSession().setAttribute("points", jsonString);
        return "redirect:/page/map";
    }

    @GetMapping("/delete/{id}/{pageNo}")
    public String delete(@PathVariable("id") Long id,
                         @PathVariable("pageNo") Integer pageNo,
                         HttpServletRequest request, Model model){
        trailService.delete(id);
        return selectPage(pageNo, request, model);
    }

    //客户端开启监控
    @PostMapping("/monitor")
    @ResponseBody
    public WxDto monitor(Point point, String userId, String locations){
        return trailService.monitor(userId, locations, point);
    }



    //人员去向分析页面
    @RequestMapping("/trailAnalyse")
    public String userAnalyse(Model model){
        trailService.selectSortCount(model);
        userService.userCount(model);
        return "trail_Analyse";
    }

    //查询某个用户某段时间在某处的记录
    @PostMapping("/likeSelect")
    public String likeSelect(String locationName, String userName,
                             String startTime, String endTime, HttpServletRequest request, Model model){
        trailService.likeSelect(locationName, userName, startTime, endTime, request, model);
        return "trail";
    }

    //查询所有用户所处位置信息可视化
    @GetMapping("/selectAllUserNow")
    public String  selectAllUserNow(HttpServletRequest request) {
        trailService.selectAllUserNow(request);
        return "redirect:/page/map";
    }


}
