package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunVpcVO implements Serializable {
    private static final long serialVersionUID = -3675204454130244555L;

    private long id;

    private long networkId;

    private String vpcDesc;

    public AliyunVpcVO() {

    }

    public AliyunVpcVO(AliyunVpcDO aliyunVpcDO) {
        this.id = aliyunVpcDO.getId();
        this.networkId = aliyunVpcDO.getNetworkId();
        this.vpcDesc = aliyunVpcDO.getVpcDesc();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(long networkId) {
        this.networkId = networkId;
    }

    public String getVpcDesc() {
        return vpcDesc;
    }

    public void setVpcDesc(String vpcDesc) {
        this.vpcDesc = vpcDesc;
    }

    @Override
    public String toString() {
        return "AliyunVpcVO{" +
                "id=" + id +
                ", networkId=" + networkId +
                ", vpcDesc='" + vpcDesc + '\'' +
                '}';
    }


}
