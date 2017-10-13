package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.plugin.chain.TaskCallback;
import com.sdg.cmdb.plugin.chain.TaskItem;


/**
 * Created by liangjian on 2017/3/31.
 */


public interface AnsibleTaskService {





    String taskLogCleanup(ServerDO serverDO, int history);

    String taskInitializationSystem(ServerDO serverDO, ServerGroupDO serverGroupDO);

    /**
     * 新版接口
     * @param serverDO
     * @param serverGroupDO
     * @param step 步骤
     * @return
     */
    TaskItem taskInitSystem(ServerDO serverDO, ServerGroupDO serverGroupDO, int step);

    void taskCteateCiDir(String projectName);

    String taskGetwayAddAccount(String username, String pwd);

    String taskGetwayDelAccount(String username);



}
