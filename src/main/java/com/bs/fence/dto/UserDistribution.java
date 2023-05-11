package com.bs.fence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-04-11-17:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDistribution {

    private int areaId;
    private int count;
    private String areaName;
}
