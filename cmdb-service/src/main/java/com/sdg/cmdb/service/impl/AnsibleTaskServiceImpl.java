package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AnsibleItemEnum;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.plugin.chain.TaskCallback;
import com.sdg.cmdb.plugin.chain.TaskChain;
import com.sdg.cmdb.plugin.chain.TaskItem;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.util.CmdUtils;
import org.apache.commons.exec.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by liangjian on 2017/6/2.
 */
@Service
public class AnsibleTaskServiceImpl implements AnsibleTaskService {

    private static final Logger logger = LoggerFactory.getLogger(AnsibleTaskServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    @Resource
    private ConfigCenterService configCenterService;

    public static final String INIT_SYSTEM_DIR = "init_system";

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ANSIBLE.getItemKey());
    }

    public String task(boolean isSudo, ServerGroupDO serverGroupDO, int envType, String cmd) {
        String groupName = serverGroupDO.getName().replace("group_", "");
        String hostgroupName = groupName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType);
        return task(isSudo, hostgroupName, cmd);
    }


    public String task(boolean isSudo, String hostgroupName, String cmd) {
        HashMap<String, String> configMap = acqConifMap();
        String ansible_bin = configMap.get(AnsibleItemEnum.ANSIBLE_BIN.getItemKey());
        String ansible_hosts_path = configMap.get(AnsibleItemEnum.ANSIBLE_HOSTS_PATH.getItemKey());

        CommandLine c = new CommandLine(ansible_bin);
        c.addArgument(hostgroupName);
        //c.addArgument("-vvv");
        c.addArgument("-i");
        c.addArgument(ansible_hosts_path);
        if (isSudo)
            c.addArgument("-sudo");
        c.addArgument("-a");
        c.addArgument(cmd, false);
        logger.info("ansible task :" + c.toString());

        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public String taskLogCleanup(ServerDO serverDO, int history) {
        HashMap<String, String> configMap = acqConifMap();
        String ansible_logcleanup_invoke = configMap.get(AnsibleItemEnum.ANSIBLE_LOGCLEANUP_INVOKE.getItemKey());

        CommandLine c = new CommandLine(ansible_logcleanup_invoke);
        c.addArgument("-servername=" + serverDO.acqServerName());
        c.addArgument("-ip=" + serverDO.getInsideIp());
        c.addArgument("-history=" + history);
        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public String taskInitializationSystem(ServerDO serverDO, ServerGroupDO serverGroupDO) {
        HashMap<String, String> configMap = acqConifMap();
        String ansible_init_system_invoke = configMap.get(AnsibleItemEnum.ANSIBLE_INIT_SYSTEM_INVOKE.getItemKey());

        if (serverGroupDO.getUseType() != ServerGroupDO.UseTypeEnum.webservice.getCode())
            return "目前只支持web-service类型服务器初始化";
        CommandLine c = new CommandLine(ansible_init_system_invoke);
        c.addArgument("-servername=" + serverDO.acqHostname());
        c.addArgument("-ip=" + serverDO.getInsideIp());
        c.addArgument("-server.type=" + serverDO.getServerType());
        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public TaskItem taskInitSystem(ServerDO serverDO, ServerGroupDO serverGroupDO, int step) {
        TaskItem taskItem = new TaskItem(InitSystemTypeEnum.step1.name()) {
            @Override
            public BusinessWrapper<String> runTask() {
                HashMap<String, String> configMap = acqConifMap();
                String ansible_task_path = configMap.get(AnsibleItemEnum.ANSIBLE_TASK_PATH.getItemKey());

                if (serverGroupDO.getUseType() != ServerGroupDO.UseTypeEnum.webservice.getCode())
                    return new BusinessWrapper<>("目前只支持web-service类型服务器初始化");
                String cmdline = ansible_task_path + "/" + INIT_SYSTEM_DIR + "/" + AnsibleTaskServiceImpl.InitSystemTypeEnum.getInitSystemTypeName(step);
                CommandLine c = new CommandLine(cmdline);
                c.addArgument("-servername=" + serverDO.acqHostname());
                c.addArgument("-ip=" + serverDO.getInsideIp());
                c.addArgument("-server.type=" + serverDO.getServerType());
                String rt = CmdUtils.run(c);
                return new BusinessWrapper<>(rt);
            }

        };
        return taskItem;

    }

    public enum InitSystemTypeEnum {
        //0 保留
        step1(1, "1_set_dns"),
        step2(2, "2_update_prometheus"),
        step3(3, "3_set_hostname"),
        step4(4, "4_mount_disk"),
        step5(5, "5_install_tomcat"),
        step6(6, "6_install_zabbix_agentd");
        private int code;
        private String desc;

        InitSystemTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getInitSystemTypeName(int code) {
            for (InitSystemTypeEnum initSystemTypeEnum : InitSystemTypeEnum.values()) {
                if (initSystemTypeEnum.getCode() == code) {
                    return initSystemTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    @Override
    public void taskCteateCiDir(String projectName) {
        HashMap<String, String> configMap = acqConifMap();
        String ansible_create_ci_deploy_dir = configMap.get(AnsibleItemEnum.ANSIBLE_CREATE_CI_DEPLOY_DIR.getItemKey());

        CommandLine c = new CommandLine(ansible_create_ci_deploy_dir);
        c.addArgument("-name=" + projectName);
        String rt = CmdUtils.run(c);
    }

    @Override
    public String taskGetwayAddAccount(String username, String pwd) {
        HashMap<String, String> configMap = acqConifMap();
        String ansible_getway_account = configMap.get(AnsibleItemEnum.ANSIBLE_GETWAY_ACCOUNT_INVOKE.getItemKey());
        //String getwayHostgroupName = configMap.get(AnsibleItemEnum.ANSIBLE_GETWAY_GROUP.getItemKey());
        CommandLine c = new CommandLine(ansible_getway_account);
        c.addArgument("-u=" + username);
        c.addArgument("-p=" + pwd);
        String rt = CmdUtils.run(c);
        return rt;
    }

    @Override
    public String taskGetwayDelAccount(String username) {
        HashMap<String, String> configMap = acqConifMap();
        String ansible_getway_account = configMap.get(AnsibleItemEnum.ANSIBLE_GETWAY_ACCOUNT_INVOKE.getItemKey());
        //String getwayHostgroupName = configMap.get(AnsibleItemEnum.ANSIBLE_GETWAY_GROUP.getItemKey());
        CommandLine c = new CommandLine(ansible_getway_account);
        c.addArgument("-d=" + username);
        String rt = CmdUtils.run(c);
        return rt;
    }


}
