package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.CiDao;
import com.sdg.cmdb.dao.ServerDao;
import com.sdg.cmdb.dao.ServerGroupDao;
import com.sdg.cmdb.dao.UserDao;
import com.sdg.cmdb.domain.ci.CiDeployServerVersionDO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.DingtalkItemEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.ZabbixItemEnum;
import com.sdg.cmdb.domain.dingtalk.DingTalkContent;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.handler.DingTalkHandler;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.DingtalkService;
import com.sdg.cmdb.service.ServerService;
import com.sdg.cmdb.service.control.configurationfile.AnsibleHostService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/4/27.
 */
@Service
public class DingtalkServiceImpl implements DingtalkService {

    /**
     * 灰度部署通知
     */
    private boolean isNotifyGray = false;

    @Resource
    private UserDao userDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ServerService serverService;

    @Resource
    private AnsibleHostService ansibleHostService;

    @Resource
    private CiDao ciDao;

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private DingTalkHandler dingTalkHandler;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.DINGTALK.getItemKey());
    }


    private String acqWebhookToken() {
        HashMap<String, String> configMap = acqConifMap();
        String webhook = configMap.get(DingtalkItemEnum.DINGTALK_WEBHOOK.getItemKey());
        String token = configMap.get(DingtalkItemEnum.DINGTALK_TOKEN_DEPLOY.getItemKey());
        return webhook + token;
    }

    @Override
    public boolean sendCiDeployMsg(CiDeployStatisticsDO ciDeployStatisticsDO) throws Exception {
        if (!isNotifyGray && ciDeployStatisticsDO.getEnv().equals(ServerDO.EnvTypeEnum.gray.getDesc())) return false;
        DingTalkContent dingTalkContent = new DingTalkContent();
        dingTalkContent.setWebHook(acqWebhookToken());
        dingTalkContent.setMsg(acqCiDeployMsg(ciDeployStatisticsDO));
        return dingTalkHandler.doNotify(dingTalkContent);
    }

    private String acqCiDeployMsg(CiDeployStatisticsDO ciDeployStatisticsDO) {
        String user = ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName() + "(" + userDao.getDisplayNameByUserName(ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName()) + ")";
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + ciDeployStatisticsDO.getProjectName());

        List<ServerDO> servers = ansibleHostService.queryServerGroupByCiGroupName(serverGroupDO, ciDeployStatisticsDO.getGroupName());
        String serversMsg = "";
        String otherServerMsg = "";
        if (servers.size() != 0) {
            for (ServerDO serverDO : servers) {
                serversMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + "\\n";
            }

            List<ServerDO> otherServers = serverService.acqOtherServers(servers);
            if (otherServers.size() != 0) {
                otherServerMsg += "\\n\\n其它服务器版本信息 :" + " \\n";
                otherServerMsg += "> ";
                for (ServerDO serverDO : otherServers) {
                    CiDeployServerVersionDO ciDeployServerVersionDO = ciDao.getCiDeployServerVersionByServerId(serverDO.getId());
                    if (ciDeployServerVersionDO != null) {
                        otherServerMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + acqServerDeployVersion(ciDeployStatisticsDO, ciDeployServerVersionDO) + "\\n";
                    } else {
                        otherServerMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + " 无版本记录" + "\\n";
                    }
                }
            }

        }

        String textMsg = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"部署消息\"," +
                "\"text\": \"### " + ciDeployStatisticsDO.getProjectName() + "\\n" +
                "环境 : " + ciDeployStatisticsDO.getEnv() + "\\n\\n" +
                "版本名称 : " + ciDeployStatisticsDO.getBambooDeployVersion() + "\\n\\n" +
                "构建编号 : " + ciDeployStatisticsDO.getBambooBuildNumber() + "\\n\\n" +
                "分支 : " + ciDeployStatisticsDO.getBranchName() + "\\n\\n" +
                "操作人 : " + user + "\\n\\n" +
                "服务器信息 : \\n\\n" +
                "> 当前部署的服务器组 : " + ciDeployStatisticsDO.getGroupName() + " \\n" +
                serversMsg + " \\n" +
                otherServerMsg + " \\n" +
                "\"}" +
                "}";
        return textMsg;
    }

    /**
     * 获取服务器的部署版本信息
     *
     * @param ciDeployServerVersionDO
     * @return
     */
    private String acqServerDeployVersion(CiDeployStatisticsDO ciDeployStatisticsDO, CiDeployServerVersionDO ciDeployServerVersionDO) {
        CiDeployStatisticsDO serverDeployDO = ciDao.getCiDeployStatisticsById(ciDeployServerVersionDO.getCiDeployStatisticsId());
        if (ciDeployStatisticsDO != null) {
            if (serverDeployDO.getBambooBuildNumber() == ciDeployStatisticsDO.getBambooBuildNumber()) {
                // 版本相同
                return " (版本相同)";
            } else {
                // 版本不同
                return "\\n\\n>版本信息 : " + serverDeployDO.getBambooDeployVersion() + "  " + serverDeployDO.getBambooBuildNumber() + "\\n\\n> 部署时间 : " + serverDeployDO.getGmtCreate();
            }
        } else {
            return "\\n无部署版本信息记录";
        }
    }


}
