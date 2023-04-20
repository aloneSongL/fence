package com.bs.fence.dao;

import com.bs.fence.dto.UserDistribution;
import com.bs.fence.entity.Location;
import com.bs.fence.entity.Trail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    //统计目前学校各区人员分布
    List<UserDistribution> selectSortCount();

    //查询某个用户某个时间段在某处的记录
    List<Trail> likeSelect(@Param("locationId") Integer locationId,
                                @Param("userId") Long userName,
                                @Param("beforeTime") Timestamp beforeTime,
                                @Param("afterTime") Timestamp afterTime);

    //查询所有用户目前所处位置信息
    List<Trail> selectAllUserNow();
}
