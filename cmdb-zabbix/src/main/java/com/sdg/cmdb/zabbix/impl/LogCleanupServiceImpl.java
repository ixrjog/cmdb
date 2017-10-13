package com.sdg.cmdb.zabbix.impl;

import com.sdg.cmdb.dao.LogCleanupDao;
import com.sdg.cmdb.dao.ServerDao;
import com.sdg.cmdb.dao.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.logCleanup.LogCleanupConfigurationDO;
import com.sdg.cmdb.domain.logCleanup.LogCleanupPropertyDO;
import com.sdg.cmdb.domain.logCleanup.LogCleanupPropertyVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.util.ArithUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.zabbix.LogCleanupService;
import com.sdg.cmdb.zabbix.ServerPerfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangjian on 2017/3/30.
 */
@Service
public class LogCleanupServiceImpl implements LogCleanupService {
    private static final Logger logger = LoggerFactory.getLogger(LogCleanupServiceImpl.class);

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private LogCleanupDao logCleanupDao;

    @Resource
    private ServerPerfService serverPerfService;

    @Resource
    private AnsibleTaskService ansibleTaskService;

    // 日志清理成功的返回字符，用于判断是否成功
    public final static String LOGCLEANUP_SUCCESS_STR = "日志清理成功";


    /**
     * 清理日志任务
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> task() {
        if (!invokeEnv.equalsIgnoreCase("online")) return new BusinessWrapper<>(true);
        this.syncData();
        List<LogCleanupPropertyDO> list = logCleanupDao.getAllEnabledLogCleanupProperty();
        for (LogCleanupPropertyDO logCleanupPropertyDO : list) {
            if (checkTime(logCleanupPropertyDO))
                cleanup(logCleanupPropertyDO.getServerId());
        }
        return new BusinessWrapper<>(true);
    }


    @Override
    public void syncData() {
        // 只在online环境执行
        if (!invokeEnv.equalsIgnoreCase("online")) return;
        List<ServerGroupDO> listServerGroupDO = serverGroupDao.queryServerGroup();
        for (ServerGroupDO serverGroupDO : listServerGroupDO) {
            // 非web-service 类跳过
            if (serverGroupDO.getUseType() != ServerGroupDO.UseTypeEnum.webservice.getCode()) continue;
            List<ServerDO> listServerDO = serverDao.acqServersByGroupId(serverGroupDO.getId());
            for (ServerDO serverDO : listServerDO) {
                LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(serverDO.getId());
                if (logCleanupPropertyDO != null) continue;
                logCleanupPropertyDO = new LogCleanupPropertyDO(serverGroupDO, serverDO);
                invokeHistory(logCleanupPropertyDO);
                logCleanupDao.addLogCleanupProperty(logCleanupPropertyDO);
            }
        }
    }


    private void invokeHistory(LogCleanupPropertyDO logCleanupPropertyDO) {
        LogCleanupConfigurationDO logCleanupConfigurationDO = logCleanupDao.getLogCleanupConfigurationByEnvType(logCleanupPropertyDO.getEnvType());
        logCleanupPropertyDO.setHistory(logCleanupConfigurationDO.getHistory());
    }

    @Override
    public TableVO<List<LogCleanupPropertyVO>> getLogCleanupPage(long serverGroupId, String serverName, int enabled, int page, int length) {
        // TableVO<List<LogCleanupPropertyDO>> getLogCleanupPage(long serverGroupId,String serverName,int enabled, int page, int length);
        long size = logCleanupDao.getLogCleanupPropertySize(serverGroupId, serverName, enabled);
        List<LogCleanupPropertyDO> list = logCleanupDao.getLogCleanupPropertyPage(serverGroupId, serverName, enabled, page * length, length);

        List<LogCleanupPropertyVO> listVO = new ArrayList<LogCleanupPropertyVO>();
        for (LogCleanupPropertyDO logCleanupPropertyDO : list) {
            ServerDO serverDO = serverDao.getServerInfoById(logCleanupPropertyDO.getServerId());
            listVO.add(new LogCleanupPropertyVO(logCleanupPropertyDO, serverDO));
        }

        return new TableVO<>(size, listVO);
    }

    /**
     * 清理日志任务
     *
     * @return
     */
    @Override
    public BusinessWrapper<String> cleanup(long serverId) {
        // 只在online环境执行
        //if (!invokeEnv.equalsIgnoreCase("online")) return new BusinessWrapper<>(ErrorCode.serverFailure);
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(serverId);
        if (!logCleanupPropertyDO.isEnabled())
            return new BusinessWrapper<>(ErrorCode.logcleanupDisabled);
        //logCleanupPropertyDO.setDiskRate((int) serverPerfService.acqDiskRate(serverDO));
        //若无服务器，则删除LogCleanupPropertyDO配置
        if (serverDO == null) {
            logCleanupDao.delLogCleanupProperty(logCleanupPropertyDO.getId());
            return new BusinessWrapper<>(ErrorCode.serverNotExist);
        }

        try {
            calHistory(logCleanupPropertyDO);
            String invokeStr = ansibleTaskService.taskLogCleanup(serverDO, (int) logCleanupPropertyDO.getHistory());
            logger.info(logCleanupPropertyDO.acqServerName() + ":" + invokeStr);
            //success 修改清理时间
            if (checkSuccess(invokeStr))
                logCleanupPropertyDO.setCleanupTime(TimeUtils.nowDate());
            logCleanupDao.updateLogCleanupProperty(logCleanupPropertyDO);
            return new BusinessWrapper<>(invokeStr);
        } catch (Exception e) {
            //calHistory(logCleanupPropertyDO);
            //logCleanupDao.updateLogCleanupProperty(logCleanupPropertyDO);
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }


    //判断日志清理是否成功
    private boolean checkSuccess(String str) {
        if (StringUtils.isEmpty(str)) return false;
        if (str.indexOf(LOGCLEANUP_SUCCESS_STR) != -1) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public BusinessWrapper<Boolean> setEnabled(long serverId) {
        //ServerDO serverDO = serverDao.getServerInfoById(serverId);
        LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(serverId);
        if (logCleanupPropertyDO.isEnabled()) {
            logCleanupPropertyDO.setEnabled(false);
        } else {
            logCleanupPropertyDO.setEnabled(true);
        }
        logCleanupDao.updateLogCleanupProperty(logCleanupPropertyDO);
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> setHistory(long serverId, int cnt) {
        LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(serverId);
        LogCleanupConfigurationDO logCleanupConfigurationDO = logCleanupDao.getLogCleanupConfigurationByEnvType(logCleanupPropertyDO.getEnvType());
        int max = logCleanupConfigurationDO.getMax();
        int min = logCleanupConfigurationDO.getMin();
        float history = logCleanupPropertyDO.getHistory() + cnt;
        if (history < min || history > max) return new BusinessWrapper<>(false);
        logCleanupPropertyDO.setHistory(history);
        logCleanupDao.updateLogCleanupProperty(logCleanupPropertyDO);
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> refreshDiskRate(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(serverId);
        int diskRate =  acqDiskRate(serverDO);
        //ServerPerfVO serverPerfVO = serverPerfService.cache(serverDO);
        //int diskRate = Integer.valueOf(Float.valueOf(serverPerfVO.getDiskRate()).intValue());
        logCleanupPropertyDO.setDiskRate(diskRate);
        logCleanupDao.updateLogCleanupProperty(logCleanupPropertyDO);
        return new BusinessWrapper<>(true);
    }

    private int acqDiskRate(ServerDO serverDO) {
        Float diskRate = serverPerfService.acqDiskRate(serverDO);
        return diskRate.intValue();
    }

    /**
     * 计算递增率rate
     *
     * @param logCleanupPropertyDO
     */
    public void calHistory(LogCleanupPropertyDO logCleanupPropertyDO) {
        if (logCleanupPropertyDO.getDiskRate() == 0) return;
        int diskRate = logCleanupPropertyDO.getDiskRate();
        float history = logCleanupPropertyDO.getHistory();
        LogCleanupConfigurationDO logCleanupConfigurationDO = logCleanupDao.getLogCleanupConfigurationByEnvType(logCleanupPropertyDO.getEnvType());
        int historyMin = logCleanupConfigurationDO.getMin();
        int historyMax = logCleanupConfigurationDO.getMax();
        // rate = ((100 - diskRate) / 30) ^ 4;
        double x = ArithUtils.div(100 - diskRate, 30);
        float rate = (float) ArithUtils.pow(x, 4, 10);
        if (logCleanupPropertyDO.getDiskRate() >= 78) {
            if (historyMin >= (history - 1)) {
                logCleanupPropertyDO.setHistory(historyMin);
            } else {
                logCleanupPropertyDO.setHistory(history - 1);
            }
        } else {
            if (historyMax <= (rate + history)) {
                logCleanupPropertyDO.setHistory(historyMax);
            } else {
                logCleanupPropertyDO.setHistory(rate + history);
            }
        }
    }

    /**
     * 判断时间是否已经过了12小时
     *
     * @param log
     * @return
     */
    public boolean checkTime(LogCleanupPropertyDO log) {
        if (StringUtils.isEmpty(log.getCleanupTime())) return true;
        try {
            long cleanupDateStamp = TimeUtils.dateToStamp(log.getCleanupTime());
            long nowDateStamp = new Date().getTime();
            if ((nowDateStamp - cleanupDateStamp) > 86400 * 500) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

 @Override
   public BusinessWrapper<Boolean> saveHistory(long serverId, int history){
     LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(serverId);
     logCleanupPropertyDO.setHistory(Float.valueOf(history));
     logCleanupDao.updateLogCleanupProperty(logCleanupPropertyDO);
     return new BusinessWrapper<>(true);
   }

}
