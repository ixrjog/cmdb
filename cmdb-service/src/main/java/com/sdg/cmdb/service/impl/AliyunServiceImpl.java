package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.AliyunDao;
import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.domain.server.CreateEcsVO;
import com.sdg.cmdb.domain.server.EcsPropertyDO;
import com.sdg.cmdb.domain.server.EcsServerDO;
import com.sdg.cmdb.service.AliyunService;
import com.sdg.cmdb.service.EcsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/6/13.
 */
@Service
public class AliyunServiceImpl implements AliyunService {

    @Resource
    private AliyunDao aliyunDao;

    @Override
    public List<AliyunEcsImageVO> queryAliyunImage(String queryDesc) {
        List<AliyunEcsImageDO> list = aliyunDao.getEcsImage(queryDesc);
        List<AliyunEcsImageVO> listVO = new ArrayList<>();
        for (AliyunEcsImageDO aliyunEcsImageDO : list)
            listVO.add(new AliyunEcsImageVO(aliyunEcsImageDO));
        return listVO;
    }

    @Override
    public List<AliyunNetworkDO> getAliyunNetwork() {
        return aliyunDao.getNetwork();
    }

    @Override
    public List<AliyunVpcVO> queryAliyunVpc(String networkType, String queryDesc) {
        if (StringUtils.isEmpty(networkType))
            networkType = "vpc";
        AliyunNetworkDO aliyunNetworkDO = aliyunDao.getNetworkByType(networkType);
        List<AliyunVpcDO> list = aliyunDao.queryAliyunVpc(aliyunNetworkDO.getId(), queryDesc);
        List<AliyunVpcVO> listVO = new ArrayList<>();
        for (AliyunVpcDO aliyunVpcDO : list)
            listVO.add(new AliyunVpcVO(aliyunVpcDO));
        return listVO;
    }

    @Override
    public List<AliyunVswitchVO> queryAliyunVswitch(long vpcId, String queryDesc) {
        List<AliyunVswitchDO> list = aliyunDao.queryAliyunVswitch(vpcId, queryDesc);
        List<AliyunVswitchVO> listVO = new ArrayList<>();
        for (AliyunVswitchDO aliyunVswitchDO : list)
            listVO.add(new AliyunVswitchVO(aliyunVswitchDO));
        return listVO;
    }

    @Override
    public List<AliyunVpcSecurityGroupVO> querySecurityGroup(long vpcId, String queryDesc) {
        List<AliyunVpcSecurityGroupDO> list = aliyunDao.querySecurityGroup(vpcId, queryDesc);
        List<AliyunVpcSecurityGroupVO> listVO = new ArrayList<>();
        for (AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO : list)
            listVO.add(new AliyunVpcSecurityGroupVO(aliyunVpcSecurityGroupDO));
        return listVO;
    }


    @Override
    public AliyunVO getAliyun(CreateEcsVO template) {
        AliyunVO aliyunVO = new AliyunVO();
        aliyunVO.setAliyunEcsImageDO(aliyunDao.queryEcsImageById(template.getImageId()));
        if (!StringUtils.isEmpty(template.getNetworkType()) && template.getNetworkType().equalsIgnoreCase("vpc")) {
            aliyunVO.setAliyunVpcDO(aliyunDao.queryAliyunVpcById(template.getVpcId()));
            aliyunVO.setAliyunVswitchDO(aliyunDao.queryAliyunVswitchById(template.getVswitchId()));
            aliyunVO.setAliyunVpcSecurityGroupDO(aliyunDao.querySecurityGroupById(template.getSecurityGroupId()));
        }
        return aliyunVO;
    }


}
