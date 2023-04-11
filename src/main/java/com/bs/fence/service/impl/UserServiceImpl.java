package com.bs.fence.service.impl;

import com.bs.fence.common.BaseContext;
import com.bs.fence.dao.UserDao;
import com.bs.fence.dto.Page;
import com.bs.fence.entity.Location;
import com.bs.fence.entity.User;
import com.bs.fence.service.LocationService;
import com.bs.fence.service.UserService;
import com.bs.fence.utils.DtoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-21:30
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;
    @Resource
    LocationService locationService;

    @Override
    public Long register(User user, ServletResponse servletResponse, Model model) {
        if("".equals(user.getUserId()) || "".equals(user.getPassword())){
            model.addAttribute("message", "请输入账号、密码");
            return -1L;
        }
        User result = userDao.select(user);
        //登录成功
        if(result != null){
            if(result.getStatus() == '1'){
                HttpServletResponse response = (HttpServletResponse)servletResponse;
                Cookie isLogin = new Cookie("isLogin", DigestUtils.md5DigestAsHex("yes".getBytes()));
                isLogin.setDomain("songlovefree.top");
                isLogin.setPath("/");
                response.addCookie(isLogin);
                isLogin.setMaxAge(3600);

                locationService.selectAll(model);
                model.addAttribute("userId", result.getId());
                return result.getId();
            }
            model.addAttribute("message", "登录失败，权限不够");
            return -1L;
        }else {
            model.addAttribute("message", "登录失败，账号或密码错误");
            return -1L;
        }
    }

    //客户端登录校验账号、密码
    public Long login(User user) {
        User result = userDao.select(user);
        if(result != null) {
            return result.getId();
        }else{
            return 0L;
        }
    }

    @Override
    public String selectName(Long id) {
        return userDao.selectName(id);
    }

    @Override
    public Integer selectAll(Model model) {
        PageHelper.startPage(1, 8);
        List<User> users = userDao.selectAll();
        PageInfo pageInfo = new PageInfo(users);
        Page page = DtoUtils.Pages(pageInfo);

        model.addAttribute("users", users);
        model.addAttribute("page", page);
        return 0;
    }

    @Override
    public int selectPage(Integer pageNo, Model model) {
        if(pageNo == 0){
            PageHelper.startPage(1, 8);
        }else {
            PageHelper.startPage(pageNo, 8);
        }
        List<User> users = userDao.selectAll();
        PageInfo pageInfo = new PageInfo(users);
        Page page = DtoUtils.Pages(pageInfo);
        if(users.size() == 0){
            return 0;
        }
        model.addAttribute("users", users);
        model.addAttribute("page", page);
        return 1;
    }

    @Override
    public int update(User user) {
        userDao.update(user);
        return 0;
    }

    @Override
    public int add(User user) {
        return userDao.add(user);
    }
}
