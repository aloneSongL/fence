package com.bs.fence.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.druid.util.StringUtils;
import com.bs.fence.dao.TrailDao;
import com.bs.fence.dao.UserTimeDao;
import com.bs.fence.dto.*;
import com.bs.fence.entity.Area;
import com.bs.fence.entity.Location;
import com.bs.fence.entity.Trail;
import com.bs.fence.entity.UserTime;
import com.bs.fence.service.AreaService;
import com.bs.fence.service.LocationService;
import com.bs.fence.service.TrailService;
import com.bs.fence.service.UserService;
import com.bs.fence.utils.CoordinateChangeUtil;
import com.bs.fence.utils.MapUtils;
import com.bs.fence.utils.PageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-13:21
 */
@Service
public class TrailServiceImpl implements TrailService {

    @Resource
    private TrailDao trailDao;
    @Resource
    private LocationService locationService;
    @Resource
    private UserService userService;
    @Resource
    private AreaService areaService;
    @Resource
    private BaiduService baiduService;
    @Resource
    private UserTimeDao userTimeDao;

    static String uuid;

    /**
     * @author sjx
     * @Description 统一分页
     * @since 2023-04-19 22-52
     */
    @Override
    public int selectPage(Integer pageNo, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        List<Location> trailList = (List<Location>) session.getAttribute("trailList");
        if (trailList == null || trailList.size() == 0) {
            model.addAttribute("trails", null);
            return 1;
        }
        PageInfo pageInfo = null;
        if (pageNo == 0) {
            pageInfo = PageUtils.pageHelperPlus(trailList, 1, 8);
        } else {
            pageInfo = PageUtils.pageHelperPlus(trailList, pageNo, 8);
        }

        PageDto page = PageUtils.getPage(pageInfo);
        model.addAttribute("trails", pageInfo.getList());
        model.addAttribute("page", page);
        return 1;
    }

    @Override
    public int selectAll(Model model, HttpServletRequest request) {
        List<TrailDto> trailDtoList = selectAllDto();
        HttpSession session = request.getSession();
        session.setAttribute("trailList", trailDtoList);
        return selectPage(1, request, model);
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
    public List<Location> inPolygon(String userId, Point point) {
        //GCJ02转百度BD09坐标
        double[] doubles = CoordinateChangeUtil.transformGCJ02ToBD09(point.getLongitude(), point.getLatitude());
        point.setLatitude(doubles[1]);
        point.setLongitude(doubles[0]);
        baiduService.uploadLocation(userId, point, uuid);
        //获取所有被监控区域
        List<Location> locations = locationService.selectAll();
        //用于存储用户在哪些区域内
        List<Location> list = new ArrayList<>();
        for (Location location : locations) {
            if(location.getIsMonitor() == '1'){
                String coordinate = location.getCoordinate();
                if(coordinate != "" && coordinate != null){
                    List<Point> points = MapUtils.splitToPoints(coordinate);
                    boolean inPolygon = MapUtils.isInPolygon(points, point);
                    if (inPolygon == true) {
                        list.add(location);
                    }
                }
                if (list.size() > 1) {
                    return list;
                }
            }
        }
        return list;
    }

    @SneakyThrows
    @Override
    public WxDto monitor(String userId, String locations, Point point) {

        WxDto wxDto = new WxDto();
        //判断用户是否登录
        if (Strings.isEmpty(userId)) {
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
        if ("[]".equals(locations) || locations == null) {
            uuid = UUID.randomUUID().toString().replace("-", "");
            String startTime = System.currentTimeMillis() + "";
            userTimeDao.insertUserTime(userId, uuid, startTime);
            baiduService.createEntity(userId, uuid);
            List<Location> list = inPolygon(userId, point);
            if (list.size() == 0) {
                wxDto.setMsg("目前在校外");
            } else {
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
        for (Object obj : list) {
            LinkedHashMap map = (LinkedHashMap) obj;
            Location location = new Location();
            location.setId((Integer) map.get("id"));
            location.setCoordinate((String) map.get("coordinate"));
            location.setDescription((String) map.get("description"));
            location.setIsMonitor((Character) map.get("isMonitor"));
            arrayList.add(location);
        }

        List<Location> inPolygon = inPolygon(userId, point);
        //arrayList为上次请求所在区域，inPolygon为本次请求所在区域
        //出的记录
        List<Location> out = locationChange(arrayList, inPolygon);
        //进的记录
        List<Location> into = locationChange(inPolygon, arrayList);
        String msg = "";
        if (out.size() != 0) {
            for (Location location : out) {
                Trail trail = new Trail();
                trail.setLocationId(location.getId());
                //出
                trail.setStatus('0');
                Timestamp date = new Timestamp(System.currentTimeMillis());
                trail.setTime(date);
                trail.setUserId(id);
                trail.setCoordinate(point.getLongitude() + "," + point.getLatitude());
                //存储记录
                add(trail);
                msg = msg + "离开" + location.getDescription();
            }
        }

        if (into.size() != 0) {
            for (Location location : into) {
                Trail trail = new Trail();
                trail.setLocationId(location.getId());
                //进
                trail.setStatus('1');
                Timestamp date = new Timestamp(System.currentTimeMillis());
                trail.setTime(date);
                trail.setUserId(id);
                trail.setCoordinate(point.getLongitude() + "," + point.getLatitude());
                //存储记录
                add(trail);
                msg = msg + "进入" + location.getDescription();
            }
        }
        if (Strings.isEmpty(msg)) {
            msg = "目前在校园内";
        }

        wxDto.setMsg(msg);
        wxDto.setStatus("200");
        wxDto.setLocations(inPolygon);
        return wxDto;
    }

    //学校各分区人员分布情况
    @Override
    public int selectSortCount(Model model) {
        List<UserDistribution> sortCount = trailDao.selectSortCount();
        List<Area> areas = areaService.selectAllArea();
        HashMap<Integer, String> helpMap = new HashMap<>();
        areas.forEach(area -> {
            helpMap.put(area.getId(), area.getName());
        });
        sortCount.forEach(userDistribution -> {
            userDistribution.setAreaName(helpMap.get(userDistribution.getAreaId()));
        });
        model.addAttribute("sortCount", sortCount);
        return 1;
    }

    @Override
    public int likeSelect(String location, String userName, String beforeTime, String afterTime, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Integer locationId = locationService.selectByName(location);
        Long userId = userService.selectIdByName(userName);
        Timestamp startTimestamp = null;
        Timestamp endTimestamp = null;
        if (beforeTime != null && beforeTime != "") {
            startTimestamp = Timestamp.valueOf(beforeTime);
        }
        if (afterTime != null && afterTime != "") {
            endTimestamp = Timestamp.valueOf(afterTime);
        }
        if (locationId == null && userId == null && startTimestamp == null && endTimestamp == null) {
            session.setAttribute("trailList", null);
            return 0;
        }
        List<Trail> trails = trailDao.likeSelect(locationId, userId, startTimestamp, endTimestamp);
        List<TrailDto> trailDtoList = trailDto(trails);
        session.setAttribute("trailList", trailDtoList);
        selectPage(1, request, model);
        return 1;
    }

    @SneakyThrows
    @Override
    public int selectAllUserNow(HttpServletRequest request) {
        List<Trail> trails = trailDao.selectAllUserNow();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(trails);
        HttpSession session = request.getSession();
        session.setAttribute("userLocation", jsonString);
        return 1;
    }

    @Override
    public List<Route> selectPath(Long userId) {
        List<UserTime> userTimes = userTimeDao.selectByUserId(userId);
        List<String[]> help = new ArrayList<>(userTimes.size());
        for (UserTime userTime : userTimes) {
            String[] strings = {userTime.getUuid(), userTime.getStartTime()};
            help.add(strings);
        }
        return baiduService.queryEntity(userId.toString(), help, uuid);
    }

    @Override
    public List<TrailDto> selectAllDto() {
        return trailDao.selectAllDto();
    }

    /**
     * @author sjx
     * @Description 用户位置变化，当locations1为之前的位置时，返回的为出；当locations2为之后的位置时，返回的为进
     * @since 2023-03-17 10-32
     */
    public List<Location> locationChange(List<Location> locations1, List<Location> locations2) {
        if (locations2.size() != 0) {
            List<Location> list = new ArrayList<>();
            for (Location location1 : locations1) {
                int status = 0;
                for (Location location2 : locations2) {
                    if (location1.equals(location2)) {
                        status = 1;
                        break;
                    }
                }
                if (status == 0) {
                    list.add(location1);
                }
            }
            //返回变化的位置信息
            return list;
        }
        //用户离开了学校或进入学校
        return locations1;
    }

    /**
     * @author sjx
     * @Description 将人员流动信息里面的id转换成具体信息
     * @since 2023-04-12 14-15
     */
    private List<TrailDto> trailDto(List<Trail> list) {
        List<TrailDto> trails = new ArrayList<>();
        for (Trail trail : list) {
            TrailDto trailDto = new TrailDto();
            //获取位置信息
            Integer locationId = trail.getLocationId();
            Location location = locationService.selectLocation(locationId);
            trailDto.setLocation(location.getDescription());
            //获取用户姓名
            Long userId = trail.getUserId();
            String userName = userService.selectName(userId);
            trailDto.setUserName(userName);

            trailDto.setId(trail.getId());
            trailDto.setStatus(trail.getStatus());
            trailDto.setTime(trail.getTime());

            trails.add(trailDto);
        }

        return trails;
    }
}
