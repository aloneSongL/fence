package com.bs.fence.service.impl;

import com.bs.fence.dao.UserDao;
import com.bs.fence.dto.PageDto;
import com.bs.fence.dto.PageDto;
import com.bs.fence.entity.User;
import com.bs.fence.service.LocationService;
import com.bs.fence.service.UserService;
import com.bs.fence.utils.PageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    private final String  COOKIE_NAME = "is_Login";

    @Override
    public Long register(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Model model) {
        if("".equals(user.getUserId()) || "".equals(user.getPassword())){
            model.addAttribute("message", "请输入账号、密码");
            return -1L;
        }
        User result = userDao.select(user);
        //登录成功
        if(result != null){
            if(result.getStatus() == '1'){
                HttpSession session = httpServletRequest.getSession();
                session.setAttribute("userId", result.getId());
                Cookie isLogin = new Cookie(COOKIE_NAME, DigestUtils.md5DigestAsHex("yes".getBytes()));
                isLogin.setDomain("songlovefree.top");
                isLogin.setPath("/");
                isLogin.setMaxAge(-1);
                httpServletResponse.addCookie(isLogin);
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
        PageDto page = PageUtils.getPage(pageInfo);

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
        PageDto page = PageUtils.getPage(pageInfo);
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

    @Override
    public int userCount(Model model) {
        int userCount = userDao.userCount();
        model.addAttribute("userCount", userCount);
        return 1;
    }

    @Override
    public Long selectIdByName(String name) {
        return userDao.selectIdByName(name);
    }

    @Override
    public int outLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(COOKIE_NAME)){
                    cookie.setMaxAge(0);
                }
            }
        }
        return 1;
    }
}
