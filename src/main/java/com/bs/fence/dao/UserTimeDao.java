package com.bs.fence.dao;

import com.bs.fence.entity.UserTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Description
 * @Author MoCi
 * @Date 2023/5/8 22:52
 */
@Mapper
public interface UserTimeDao {

    boolean insertUserTime(@Param("userId") String userId, @Param("uuid") String uuid, @Param("startTime") String startTime);

    List<UserTime> selectByUserId(Long userId);
}
