package com.bs.fence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-01-15:54
 */
@Data
@AllArgsConstructor
public class Location {

    private Integer id;
    private String coordinate;
    private String description;
    private Character isMonitor;

    public Location(){}

    public Location(Integer id, String coordinate, String description){
        this.id = id;
        this.coordinate = coordinate;
        this.description = description;
    }
}
