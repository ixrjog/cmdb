package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2017/4/12.
 */
public interface AnsibleHostService {

    /**
     * 按环境分组(prod环境不分多组)
     * @param serverGroupDO
     * @return
     */
    Map<String, List<ServerDO>> grouping(ServerGroupDO serverGroupDO);

    /**
     * 按环境分组
     * @param serverGroupDO
     * @return
     */
    Map<String, List<ServerDO>> groupingWebService(ServerGroupDO serverGroupDO);



    /**
     * 按持续集成分组名称查询主机组
     * @param serverGroupDO
     * @param ciGroupName
     * @return
     */
    List<ServerDO> queryServerGroupByCiGroupName(ServerGroupDO serverGroupDO, String ciGroupName);


    /**
     *
     * @param type 分组类型（只针对production）
     *             0 只分2组
     *             (1 按每组2台分组)
     *             2 生成全局服务器列表
     */
    String acqConfig(int type);


    String acqHostsCfg();

    String acqHostsAllCfg();

}
