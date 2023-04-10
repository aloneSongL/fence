package com.bs.fence.service;

import com.bs.fence.entity.User;
import org.springframework.ui.Model;

import javax.servlet.ServletResponse;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-21:17
 */
public interface UserService {

    //查询账户
    Long register(User user, ServletResponse servletResponse, Model model);

    //查询用户
    Long login(User user);

    //查询姓名
    String selectName(Long id);

    //查询所有用户
    Integer selectAll(Model model);

    //分页查询
    int selectPage(Integer pageNo, Model model);

    //编辑用户信息
    int update(User user);

    //添加用户
    int add(User user);
}
