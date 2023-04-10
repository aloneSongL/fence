package com.bs.fence.controller;

import com.bs.fence.dto.Point;
import com.bs.fence.dto.WxDto;
import com.bs.fence.entity.Location;
import com.bs.fence.service.TrailService;
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

    //客户端查询目前所在区域
    @PostMapping("/inPolygon")
    @ResponseBody
    public List<Location> inPolygon(Point point){
        List<Location> locations = trailService.inPolygon(point);
        return locations;
    }
}
