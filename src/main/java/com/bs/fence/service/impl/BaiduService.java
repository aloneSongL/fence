package com.bs.fence.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bs.fence.config.BaiduConfig;
import com.bs.fence.dto.Point;
import com.bs.fence.dto.Route;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Description
 * @Author MoCi
 * @Date 2023/4/25 10:53
 */
@Service
public class BaiduService {

    @Resource
    BaiduApiService baiduApiService;

    @Resource
    BaiduConfig baiduConfig;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    /**
     * 创建鹰眼服务中的实体
     *
     * @param userId 用户 ID
     * @return
     */
    public Boolean createEntity(String userId, String uuid) {
        String url = baiduConfig.getUrl() + "/entity/add";
        return baiduApiService.execute(url, Method.POST,
                createParam(userId, uuid),
                httpResponse -> {
                    if (httpResponse.isOk()) {
                        String body = httpResponse.body();
                        JSONObject jsonObject = JSONUtil.parseObj(body);
                        return jsonObject.getInt("status") == 0;
                    }
                    return false;
                }
        );
    }

    private Map<String, Object> createParam(String userId, String uuid) {
        return MapUtil.builder("entity_name", createEntityName(userId, uuid)).build();
    }

    private Object createEntityName(String userId, String uuid) {
        return "user_" + userId + "_" + uuid;
    }

    /**
     * 删除百度鹰眼服务中的实体
     *
     * @param userId 用户 ID
     * @param uuid   轨迹唯一ID
     * @return
     */
    public boolean deleteEntity(String userId, String uuid) {
        String url = baiduConfig.getUrl() + "/entity/delete";
        return baiduApiService.execute(url, Method.POST,
                createParam(userId, uuid),
                httpResponse -> {
                    if (httpResponse.isOk()) {
                        String body = httpResponse.body();
                        JSONObject jsonObject = JSONUtil.parseObj(body);
                        return jsonObject.getInt("status") == 0;
                    }
                    return false;
                }
        );

    }

    /**
     * 给实体添加轨迹点
     *
     * @param userId
     * @param point
     * @return
     */
    public Boolean uploadLocation(String userId, Point point, String uuid) {
        String url = baiduConfig.getUrl() + "/track/addpoint";
        return baiduApiService.execute(url,
                Method.POST,
                MapUtil.builder(new HashMap<String, Object>())
                        .put("entity_name", createEntityName(userId, uuid))
                        .put("latitude", point.getLatitude())
                        .put("longitude", point.getLongitude())
                        .put("loc_time", System.currentTimeMillis() / 1000)
                        .put("coord_type_input", "gcj02")
                        .build(),
                httpResponse -> {
                    if (httpResponse.isOk()) {
                        String body = httpResponse.body();
                        JSONObject jsonObject = JSONUtil.parseObj(body);
                        return jsonObject.getInt("status") == 0;
                    }
                    return false;
                }
        );
    }

    /**
     * 根据用户 id 查询一段时间内的轨迹
     *
     * @param userId
     * @param help
     * @param uuid
     * @return
     */
    public List<Route> queryEntity(String userId, List<String[]> help, String uuid) {
        String url = baiduConfig.getUrl() + "/track/gettrack";
        Map<String, Object> paramMap = MapUtil.builder(new HashMap<String, Object>())
                .put("entity_name", createEntityName(userId, uuid))
                .put("is_processed", 1)
                .put("coord_type_output", "gcj02")
                .build();
        List<Route> routes = new ArrayList<>();
        for (String[] strings : help) {
            paramMap.put("start_time", strings[1]);
            paramMap.put("end_time", System.currentTimeMillis());
            baiduApiService.execute(url, Method.GET, paramMap,
                    httpResponse -> {
                        if (httpResponse.isOk()) {
                            String body = httpResponse.body();
                            try {
                                Route route = MAPPER.readValue(body, Route.class);
                                routes.add(route);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    });
        }
        return routes;
    }
}
