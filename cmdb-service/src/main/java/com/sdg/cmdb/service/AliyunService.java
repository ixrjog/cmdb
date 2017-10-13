package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.domain.server.CreateEcsVO;
import com.sdg.cmdb.domain.server.EcsServerDO;

import java.util.List;

/**
 * Created by liangjian on 2017/6/12.
 */
public interface AliyunService {

    List<AliyunEcsImageVO> queryAliyunImage(String queryDesc);

    List<AliyunNetworkDO> getAliyunNetwork();

    List<AliyunVpcVO> queryAliyunVpc(String networkType,String queryDesc);

    List<AliyunVswitchVO> queryAliyunVswitch(long vpcId, String queryDesc);

    List<AliyunVpcSecurityGroupVO>  querySecurityGroup(long vpcId, String queryDesc);

    AliyunVO getAliyun(CreateEcsVO template);




}
