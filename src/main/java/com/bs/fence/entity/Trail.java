package com.bs.fence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-12:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trail {

    private Long id;
    private Integer locationId;
    private Timestamp time;
    private Character status;
    private Long userId;
    private String coordinate;
}
