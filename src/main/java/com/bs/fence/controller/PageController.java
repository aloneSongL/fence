package com.bs.fence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-23:56
 */
@Controller
@RequestMapping("/page")
public class PageController {

    //登录页面
    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    //电子围栏绘制页面
    @RequestMapping("/map/{id}/{pageNo}")
    public String map(@PathVariable("id") Integer id,
                      @PathVariable("pageNo") Integer pageNo, Model model){
        model.addAttribute("locationId", id);
        model.addAttribute("pageNo", pageNo);
        return "map";
    }

    //电子围栏添加页面
    @RequestMapping("/add")
    public String add(){
        return "add";
    }

    //电子围栏编辑页面
    @RequestMapping("/locationEdit/{id}/{pageNo}")
    public String locationEdit(@PathVariable("id") Integer id,
                       @PathVariable("pageNo") Integer pageNo, Model model){
        model.addAttribute("locationId", id);
        model.addAttribute("pageNo", pageNo);
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


}
