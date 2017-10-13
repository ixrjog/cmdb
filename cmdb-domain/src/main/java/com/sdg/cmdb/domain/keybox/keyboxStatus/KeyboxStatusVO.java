package com.sdg.cmdb.domain.keybox.keyboxStatus;

import java.io.Serializable;
import java.util.List;

public class KeyboxStatusVO implements Serializable {
    private static final long serialVersionUID = -7858611848296550596L;

    private List<KeyboxServerVO> topServerList;


    private List<KeyboxUserVO> topUserList;


    private int authedUserCnt = 0;

    private int keyboxLoginCnt = 0;

    public List<KeyboxServerVO> getTopServerList() {
        return topServerList;
    }

    public void setTopServerList(List<KeyboxServerVO> topServerList) {
        this.topServerList = topServerList;
    }

    public List<KeyboxUserVO> getTopUserList() {
        return topUserList;
    }

    public void setTopUserList(List<KeyboxUserVO> topUserList) {
        this.topUserList = topUserList;
    }

    public int getAuthedUserCnt() {
        return authedUserCnt;
    }

    public void setAuthedUserCnt(int authedUserCnt) {
        this.authedUserCnt = authedUserCnt;
    }

    public int getKeyboxLoginCnt() {
        return keyboxLoginCnt;
    }

    public void setKeyboxLoginCnt(int keyboxLoginCnt) {
        this.keyboxLoginCnt = keyboxLoginCnt;
    }
}
