package com.bs.fence.service.impl;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.bs.fence.dao.TrailDao;
import com.bs.fence.dto.Page;
import com.bs.fence.dto.Point;
import com.bs.fence.dto.TrailDto;
import com.bs.fence.dto.WxDto;
import com.bs.fence.entity.Location;
import com.bs.fence.entity.Trail;
import com.bs.fence.service.LocationService;
import com.bs.fence.service.TrailService;
import com.bs.fence.service.UserService;
import com.bs.fence.utils.CoordinateChangeUtil;
import com.bs.fence.utils.DtoUtils;
import com.bs.fence.utils.MapUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-13:21
 */
@Service
public class TrailServiceImpl implements TrailService{

    @Resource
    private TrailDao trailDao;
    @Resource
    private LocationService locationService;
    @Resource
    private UserService userService;

    @Override
    public int selectPage(Integer pageNo,Model model) {
        if(pageNo == 0){
            PageHelper.startPage(1, 8);
        }
        PageHelper.startPage(pageNo, 8);
        List<Trail> result = trailDao.selectAll();
        PageInfo pageInfo = new PageInfo(result);
        Page page = DtoUtils.Pages(pageInfo);

        List<TrailDto> trails = new ArrayList<>();
        for(Trail trail : result) {
            TrailDto trailDto = new TrailDto();
            //获取位置信息
            Integer locationId = trail.getLocationId();
            String description = locationService.selectLocation(locationId);
            trailDto.setLocation(description);
            //获取用户姓名
            Long userId = trail.getUserId();
            String userName = userService.selectName(userId);
            trailDto.setUserName(userName);

            trailDto.setId(trail.getId());
            trailDto.setStatus(trail.getStatus());
            trailDto.setTime(trail.getTime());

            trails.add(trailDto);
        }

        model.addAttribute("trails", trails);
        model.addAttribute("page", page);
        return 1;
    }

    @Override
    public int selectById(Model model, Integer locationId) {
        PageHelper.startPage(1, 10);
        List<Trail> result = trailDao.selectById(locationId);
        PageInfo pageInfo = new PageInfo(result);
        Page page = DtoUtils.Pages(pageInfo);

        List<TrailDto> trails = new ArrayList<>();
        for(Trail trail : result) {
            TrailDto trailDto = new TrailDto();
            //获取位置信息
            Integer location = trail.getLocationId();
            String description = locationService.selectLocation(location);
            trailDto.setLocation(description);
            //获取用户姓名
            Long userId = trail.getUserId();
            String userName = userService.selectName(userId);
            trailDto.setUserName(userName);

            trailDto.setId(trail.getId());
            trailDto.setStatus(trail.getStatus());
            trailDto.setTime(trail.getTime());

            trails.add(trailDto);
        }
        model.addAttribute("trails", trails);
        model.addAttribute("page", page);
        return 1;
    }

    @Override
    public int selectAll(Model model) {
        return selectPage(1, model);
    }

    @Override
    public int delete(Long id) {
        return trailDao.delete(id);
    }

    @Override
    public int add(Trail trail) {
        return trailDao.add(trail);
    }

    @Override
    public List<Location> inPolygon(Point point) {
        //GCJ02转百度BD09坐标
        double[] doubles = CoordinateChangeUtil.transformGCJ02ToBD09(point.getLongitude(), point.getLatitude());
        point.setLatitude(doubles[1]);
        point.setLongitude(doubles[0]);
        //获取所有被监控区域
        List<Location> locations = locationService.selectAll();
        //用于存储用户在哪些区域内
        List<Location> list = new ArrayList<>();
        for(Location location : locations) {
            String coordinate = location.getCoordinate();
            List<Point> points = MapUtils.splitToPoints(coordinate);
            boolean inPolygon = MapUtils.isInPolygon(points, point);
            if(inPolygon == true) {
                list.add(location);
            }
            if(list.size() > 1) {
                return list;
            }
        }
        return list;
    }

    @SneakyThrows
    @Override
    public WxDto monitor(String userId, String locations, Point point) {

        WxDto wxDto = new WxDto();
        //判断用户是否登录
        if(Strings.isEmpty(userId)){
            wxDto.setStatus("500");
            wxDto.setMsg("用户未登录，请先登录");
            return wxDto;
        }
        Long id = Long.parseLong(userId);

        //坐标若为空，说明微信定位失败
        if (point.getLongitude() == null || point.getLatitude() == null) {
            wxDto.setStatus("500");
            wxDto.setMsg("定位失败，坐标为空");
            return wxDto;
        }
        //判断用户是否第一次发起定位，如果是则返回目前所在区域，
        //否则返回位置变化信息及更新目前所在区域
        if("[]".equals(locations)) {
                List<Location> list = inPolygon(point);
                if(list.size() == 0){
                    wxDto.setMsg("目前在校外");
                }else {
                    wxDto.setMsg("目前在校园内");
                }
                wxDto.setStatus("200");
                wxDto.setLocations(list);
                return wxDto;
        }
        //list是一个LinkedHashMap列表
        List list = new ObjectMapper().readValue(locations, List.class);

        //将Location的信息从LinkedHashMap中取出
        ArrayList<Location> arrayList = new ArrayList<>();
        for(Object obj : list){
            LinkedHashMap map = (LinkedHashMap)obj;
            Location location = new Location();
            location.setId((Integer) map.get("id"));
            location.setCoordinate((String) map.get("coordinate"));
            location.setDescription((String) map.get("description"));
            location.setIsMonitor((Character) map.get("isMonitor"));
            arrayList.add(location);
        }

        List<Location> inPolygon = inPolygon(point);
        //arrayList为上次请求所在区域，inPolygon为本次请求所在区域
        //出的记录
        List<Location> out = locationChange(arrayList, inPolygon);
        //进的记录
        List<Location> into = locationChange(inPolygon, arrayList);
        String msg = "";
        if(out.size() != 0){
            for(Location location : out){
                Trail trail = new Trail();
                trail.setLocationId(location.getId());
                //出
                trail.setStatus('0');
                Timestamp date = new Timestamp(System.currentTimeMillis());
                trail.setTime(date);
                trail.setUserId(id);
                //存储记录
                add(trail);
                msg = msg + "离开" + location.getDescription();
            }
        }

        if(into.size() != 0){
            for(Location location : into){
                Trail trail = new Trail();
                trail.setLocationId(location.getId());
                //进
                trail.setStatus('1');
                Timestamp date = new Timestamp(System.currentTimeMillis());
                trail.setTime(date);
                trail.setUserId(id);
                //存储记录
                add(trail);
                msg = msg + "进入" + location.getDescription();
            }
        }
        if(Strings.isEmpty(msg)){
            msg = "目前在校园中";
        }

        wxDto.setMsg(msg);
        wxDto.setStatus("200");
        wxDto.setLocations(inPolygon);
        return wxDto;
    }

    /**
    @author sjx
    @Description 用户位置变化，当locations1为之前的位置时，返回的为出；当locations2为之后的位置时，返回的为进
    @since 2023-03-17 10-32
    */
    public List<Location> locationChange(List<Location> locations1, List<Location> locations2){
        if(locations2.size() != 0) {
            List<Location> list = new ArrayList<>();
            for(Location location1 : locations1) {
                int status = 0;
                for(Location location2 : locations2) {
                    if(location1.equals(location2)){
                        status = 1;
                        break;
                    }
                }
                if(status == 0){
                    list.add(location1);
                }
            }
            //返回变化的位置信息
            return list;
        }
        //用户离开了学校或进入学校
        return  locations1;
    }
}
