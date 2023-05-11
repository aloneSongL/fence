package com.bs.fence.dto;

import cn.hutool.core.lang.ObjectId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Description
 * @Author MoCi
 * @Date 2023/4/25 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {


    private ObjectId id; //主键id
    private String title; //路线标题
    private Long userId; //创建路线的用户id
    private Boolean isShare; //是否投稿
    private Integer size; //轨迹点数量
    private Double distance; //此段轨迹的里程数，单位：米
    private String time; //运动时间，格式：mm:ss
    private Double speed; //平均速度，单位：km/h
    private Long startTime; //创建路线时间戳
    private Long endTime; //结束路线时间戳
    private LocationPoint startPoint; //起点信息
    private LocationPoint endPoint; //终点信息
    private List<LocationPoint> points; //历史轨迹点列表

}
