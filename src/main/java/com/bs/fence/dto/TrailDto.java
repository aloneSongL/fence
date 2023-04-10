package com.bs.fence.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-13:25
 */
@Data
@NoArgsConstructor
public class TrailDto {

    private Long id;
    private String location;
    private Timestamp time;
    private Character status;
    private String userName;

}
