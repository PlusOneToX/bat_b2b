package com.bat.dubboapi.system.user.dto;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/24 10:16
 */
public class UserLoginRpcQry implements Serializable {

    private Integer id;

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
