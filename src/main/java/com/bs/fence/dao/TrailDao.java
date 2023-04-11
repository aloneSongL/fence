package com.bs.fence.dao;

import com.bs.fence.dto.UserDistribution;
import com.bs.fence.entity.Location;
import com.bs.fence.entity.Trail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-13:01
 */
@Mapper
public interface TrailDao {

    //查询所有轨迹
    List<Trail> selectAll();

    //查询某一区域的轨迹
    List<Trail> selectById(Integer locationId);

    //删除轨迹
    int delete(Long id);

    //插入记录
    int add(Trail trail);

    //查询每个用户最新的一条记录
    List<Location> selectAllNew();

    //查询某个用户最新的一条记录
    Location selectNewById(Long userId);

    //查询某一个时间段内的记录
    List<Location> selectInTime(@Param("beforeTime") Timestamp beforeTime,
                                @Param("afterTime") Timestamp afterTime);

    //查询某一个时间段某个用户的记录
    List<Location> selectInTimeById(@Param("userId") Long userId,
                                    @Param("beforeTime") Timestamp beforeTime,
                                    @Param("afterTime") Timestamp afterTime);

    //统计目前学校各区人员分布
    List<UserDistribution> selectSortCount();

    //统计学校某一时间人员分布

}
