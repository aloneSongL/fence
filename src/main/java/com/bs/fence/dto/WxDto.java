package com.bs.fence.dto;

import com.bs.fence.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-20-10:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxDto {

    public String status;
    public String msg;
    public List<Location> locations;
}
