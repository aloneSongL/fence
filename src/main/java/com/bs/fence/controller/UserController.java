package com.bs.fence.controller;

import com.bs.fence.entity.User;
import com.bs.fence.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-21:19
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //web管理页面登录
    @PostMapping("/select")
    public String register(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Model model) {
        Long result = userService.register(user, httpServletRequest, httpServletResponse,model);
        if(result > 0L){
            return "redirect:/location/select";
        }else{
            return "register";
        }
    }

    //客户端登录
    @PostMapping("/login")
    @ResponseBody
    public String login(User user) {
        Long result = userService.login(user);
        if(result > 0L){
            return result + "";
        }else{
            return "0";
        }
    }

    //查询所有用户的账号、姓名、权限
    @GetMapping("/selectAll")
    public String selectAll(Model model){
        userService.selectAll(model);
        return "user_manage";
    }

    //用户分页查询
    @GetMapping("/selectPage/{pageNo}")
    public String selectPage(@PathVariable("pageNo") Integer pageNo, Model model){
        userService.selectPage(pageNo, model);
        return "user_manage";
    }

    //修改用户信息
    @PostMapping("/update/{id}/{pageNo}")
    public String update(@PathVariable("id") String userId,
                         @PathVariable("pageNo") Integer pageNo,
                         String userName, Character status, Model model){
        long id = Long.parseLong(userId);
        User user = new User(id, userName, status);
        userService.update(user);
        return selectPage(pageNo, model);
    }

    //添加用户
    @PostMapping("/add")
    public String add(String userId, Character status, String userName, Model model){
        userService.add(new User(userId, userName, status));
        return selectAll(model);
    }

    /**
    @author sjx
    @Description 退出登录
    @since 2023-04-23 13-09
    */
    @GetMapping("/outLogin")
    public String outLogin(HttpServletRequest request){
        userService.outLogin(request);
        return "register";
    }
}
