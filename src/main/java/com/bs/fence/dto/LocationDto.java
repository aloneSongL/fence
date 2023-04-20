package com.bs.fence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-04-19-21:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private Integer id;
    private String coordinate;
    private String description;
    private Character isMonitor;
    private String areaName;
}
