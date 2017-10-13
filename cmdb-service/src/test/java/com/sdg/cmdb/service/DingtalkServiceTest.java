package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.CiDao;
import com.sdg.cmdb.domain.ci.CiDeployServerVersionDO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.impl.ConfigServiceImpl;
import com.sdg.cmdb.service.impl.DingtalkServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/4/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class DingtalkServiceTest {
    @Resource
    private CiDao ciDao;

    @Resource
    private DingtalkServiceImpl dingtalkServiceImpl;

    @Test
    public void testSendCiDeployMsg() {
        try {
            CiDeployStatisticsDO ciDeployStatisticsDO = ciDao.getCiDeployStatisticsById(375);
            System.err.println(ciDeployStatisticsDO);
            dingtalkServiceImpl.sendCiDeployMsg(ciDeployStatisticsDO);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("发生错误！");
        }

    }


}
