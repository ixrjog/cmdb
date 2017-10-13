package com.sdg.cmdb.zabbix;

import com.sdg.cmdb.dao.ServerDao;
import com.sdg.cmdb.domain.server.ServerDO;

import com.sdg.cmdb.zabbix.impl.ServerPerfServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ServerPerfServiceTest {


    @Resource
    private ServerPerfService serverPerfService;

    @Resource
    private ZabbixHistoryService zabbixHistoryService;

    @Resource
    private ServerDao serverDao;

    @Test
    public void test() {
        ServerDO s1 = serverDao.getServerInfoById(752);
        ServerDO outway2 = serverDao.getServerInfoById(108);

        ServerDO serverDO = s1;

        long sysDiskRate = 0;
        String freeDiskOnSys = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryFreeDiskSpaceOnSys(serverDO, 1));
        System.err.println("freeDiskOnSys:" + freeDiskOnSys);

        String freeDiskOnData = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryFreeDiskSpaceOnData(serverDO, 1));
        System.err.println("freeDiskOnData:" + freeDiskOnData);

        //sysDiskRate = Long.parseLong(usedDiskSpaceOnSys) * 100 / Long.parseLong(totalDiskSpaceOnSys);

        //String usedDiskSpaceOnData = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryUsedDiskSpaceOnData(serverDO, 1));
        //String totalDiskSpaceOnData = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryTotalDiskSpaceOnData(serverDO, 1));
        //long dataDiskRate = Long.parseLong(usedDiskSpaceOnData) * 100 / Long.parseLong(totalDiskSpaceOnData);

        //System.err.println("sysDiskRate:" + sysDiskRate);
        //System.err.println("dataDiskRate:" + dataDiskRate);

    }

    @Test
    public void testGetCache() {
        ServerDO itemcenter1 = serverDao.getServerInfoById(98);
        System.err.println(serverPerfService.getCache(itemcenter1).perfInfo());


    }

}


