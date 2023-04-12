package com.bs.fence.controller;

import com.bs.fence.dto.Point;
import com.bs.fence.dto.WxDto;
import com.bs.fence.entity.Location;
import com.bs.fence.service.TrailService;
import com.bs.fence.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public String select(Model model){
        trailService.selectAll(model);
        return "trail";
    }

    @GetMapping("/selectPage/{pageNo}")
    public String selectPage(@PathVariable("pageNo") Integer pageNo, Model model){
        trailService.selectPage(pageNo, model);
        return "trail";
    }

    @GetMapping("/delete/{id}/{pageNo}")
    public String delete(@PathVariable("id") Long id,
                         @PathVariable("pageNo") Integer pageNo, Model model){
        trailService.delete(id);
        return selectPage(pageNo, model);
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
    @PostMapping("/selectInTimeByName")
    public String selectInTimeByName(String location, String userName, String beforeTime, String afterTime, Model model){
        trailService.selectInTime(location, userName, beforeTime, afterTime, model);
        return "trail";
    }
}
