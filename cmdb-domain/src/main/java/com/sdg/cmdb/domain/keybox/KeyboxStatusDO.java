package com.sdg.cmdb.domain.keybox;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class KeyboxStatusDO implements Serializable {
    private static final long serialVersionUID = -2288311882090882506L;

    private long id;

    private long userId;

    private String username;

    private long serverId;

    private String gmtCreate;

    private String gmtModify;


    public KeyboxStatusDO() {

    }

    public KeyboxStatusDO(ServerDO serverDO, String username) {
        this.serverId = serverDO.getId();
        this.username = username;
    }

    public KeyboxStatusDO(ServerDO serverDO, UserDO userDO) {
        this.serverId = serverDO.getId();
        this.username = userDO.getUsername();
        this.userId = userDO.getId();
    }

    @Override
    public String toString() {
        return "KeyboxStatusDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", serverId=" + serverId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
