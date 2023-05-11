package com.bs.fence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @Description
 * @Author MoCi
 * @Date 2023/5/8 22:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTime {
    private Long id;
    private Long userId;
    private String uuid;
    private String startTime;
}
