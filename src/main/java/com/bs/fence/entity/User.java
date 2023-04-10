package com.bs.fence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-02-21:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String userId;
    private String password;
    private Character status;
    private String userName;

    public User(String userId, String password){
        this.userId = userId;
        this.password = password;
    }

    public User(Long id, String userName, Character status){
        this.id = id;
        this.status = status;
        this.userName = userName;
    }

    public User(String userId, String userName, Character status){
        this.userId = userId;
        this.status = status;
        this.userName = userName;
    }
}
