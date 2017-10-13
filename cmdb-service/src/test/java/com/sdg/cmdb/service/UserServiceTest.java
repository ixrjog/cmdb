package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.ServerDao;
import com.sdg.cmdb.dao.ServerGroupDao;
import com.sdg.cmdb.dao.UserDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserLeaveDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.shandiangou.org.domain.EmpVO;
import com.shandiangou.org.service.EmpQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by liangjian on 2017/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class UserServiceTest {

    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    private EmpQueryService empQueryService;

    @Test
    public void testAddUserLeave() {
        UserDO userDO = userDao.getUserByName("li");
        UserLeaveDO userLeaveDO = new UserLeaveDO(userDO);
        userDao.addUserLeave(userLeaveDO);
    }

    @Test
    public void testAddUserLeave2() {
        UserDO userDO = userDao.getUserByName("li");
        UserLeaveDO userLeaveDO = new UserLeaveDO(userDO);
        userDao.addUserLeave(userLeaveDO);
        userLeaveDO.setDumpLdap(UserLeaveDO.DumpTypeEnum.success.getCode());
        userService.saveUserLeave(userLeaveDO);
    }

    @Test
    public void testEmpQueryService() {
        String mobile = userService.queryUserMobileByEmail("li.net");
        System.err.println(mobile);
    }
}
