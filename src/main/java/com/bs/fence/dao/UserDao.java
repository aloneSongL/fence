package com.bs.fence.dao;

import com.bs.fence.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-21:18
 */
@Mapper
public interface UserDao {

    //查询账户
    User select(User user);

    //查询姓名
    String selectName(Long id);

    //查询所有用户
    List<User> selectAll();

    //编辑用户信息
    int update(User user);

    //添加用户
    int add(User user);

    //查询用户总人数
    int userCount();

    //根据姓名查询用户id
    Long selectIdByName(String name);
}
