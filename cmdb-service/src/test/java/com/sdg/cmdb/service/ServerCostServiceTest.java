package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.ServerDao;
import com.sdg.cmdb.domain.server.EcsServerDO;
import com.sdg.cmdb.domain.server.ServerDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangjian on 2017/2/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ServerCostServiceTest {


    @Resource
    private ServerDao serverDao;

    @Test
    public void test() {



    }
}
