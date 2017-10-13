package com.sdg.cmdb.dao;

import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/6/12.
 */
@Component
public interface AliyunDao {

    /**
     * 查询阿里云ecs镜像
     *
     * @return
     */
    List<AliyunEcsImageDO> getEcsImage(@Param("queryDesc") String queryDesc);

    AliyunEcsImageDO queryEcsImageById(@Param("id") long id);

    /**
     * 查询阿里云网络类型
     *
     * @return
     */
    List<AliyunNetworkDO> getNetwork();

    /**
     * 查询阿里云网络类型
     *
     * @return
     */
    AliyunNetworkDO getNetworkByType(@Param("networkType") String networkType);


    /**
     * 查询阿里云vpc网络列表
     *
     * @param queryDesc
     * @return
     */
    List<AliyunVpcDO> queryAliyunVpc( @Param("networkId") long networkId,
                                      @Param("queryDesc") String queryDesc);

    AliyunVpcDO queryAliyunVpcById(@Param("id") long id);

    /**
     * 查询阿里云vpc下的虚拟交换机
     *
     * @param vpcId
     * @param queryDesc
     * @return
     */
    List<AliyunVswitchDO> queryAliyunVswitch(
            @Param("vpcId") long vpcId,
            @Param("queryDesc") String queryDesc
    );

    AliyunVswitchDO queryAliyunVswitchById(@Param("id") long id);

    /**
     * 查询vpc下的安全组
     * @param vpcId
     * @param queryDesc
     * @return
     */
    List<AliyunVpcSecurityGroupDO> querySecurityGroup(
            @Param("vpcId") long vpcId,
            @Param("queryDesc") String queryDesc
    );

    AliyunVpcSecurityGroupDO querySecurityGroupById(@Param("id") long id);

}
