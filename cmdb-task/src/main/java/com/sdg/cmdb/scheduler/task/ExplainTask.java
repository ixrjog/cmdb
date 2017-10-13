package com.sdg.cmdb.scheduler.task;

import com.alibaba.fastjson.JSON;
import com.sdg.cloudstack.explain.domain.ExplainContent;
import com.sdg.cloudstack.explain.domain.SqlItem;
import com.sdg.cloudstack.explain.processor.AuditProcessor;
import com.sdg.cloudstack.explain.processor.GitProcessor;
import com.sdg.cloudstack.explain.transmission.remote.TransRemote;
import com.sdg.cmdb.dao.ExplainDao;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.ExplainCdlItemEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.ZabbixItemEnum;
import com.sdg.cmdb.domain.explain.ExplainInfo;
import com.sdg.cmdb.domain.explain.ExplainJob;
import com.sdg.cmdb.domain.explain.JobStatusEnum;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.util.schedule.BaseJob;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 2017/4/11.
 */
@Service
public class ExplainTask implements BaseJob, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("explainLogger");

    private static final String taskCorn = "0 0 * * * ?";

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private ExplainDao explainDao;

    @Resource
    private SchedulerManager schedulerManager;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.EXPLAIN_CDL.getItemKey());
    }


    @Override
    public void execute() {
        HashMap<String, String> configMap = acqConifMap();
        String localPath = configMap.get(ExplainCdlItemEnum.EXPLAIN_CDL_LOCAL_PATH.getItemKey());
        String username = configMap.get(ExplainCdlItemEnum.EXPLAIN_GIT_USERNAME.getItemKey());
        String pwd = configMap.get(ExplainCdlItemEnum.EXPLAIN_GIT_PWD.getItemKey());

        List<ExplainJob> jobList = explainDao.queryExplainJobsOrderByWeightVersion();

        for(ExplainJob explainJob : jobList) {
            ExplainInfo explainInfo = explainDao.getRepoSubById(explainJob.getMetaId());

            logger.info("begin analysis repo:" + explainInfo.getRepo() + " branch:" + explainJob.getJobBranch());

            try {
                try {
                    GitProcessor.cloneRepository(localPath, explainInfo.getRepo(), username, pwd, Arrays.asList(explainJob.getJobBranch()));
                } catch (Exception e1) {
                    logger.error("clone repository error, so jump over this branch, " + e1.getMessage(), e1);
                    continue;
                }

                explainJob.setJobStatus(JobStatusEnum.statusExecution.getCode());
                explainDao.updateExplainJob(explainJob);

                List<String> scanPathList = JSON.parseArray(explainInfo.getScanPath(), String.class);
                List<String> newScanPathList = new ArrayList<>();
                for(String scanpath : scanPathList) {
                    newScanPathList.add(localPath + scanpath);
                }

                AuditProcessor sqlAudit = new AuditProcessor();
                sqlAudit.setMapperLocations(newScanPathList);

                List<SqlItem> sqlItemList = sqlAudit.init();

                ExplainContent explainContent = new ExplainContent();
                explainContent.setRemoteRepo(explainInfo.getRepo());
                explainContent.setBranch(explainJob.getJobBranch());
                explainContent.setNotifyEmails(explainInfo.getNotifyEmails());
                explainContent.setCdlAppId(explainInfo.getCdlAppId());
                explainContent.setCdlGroup(explainInfo.getCdlGroup());

                TransRemote.writeSqlItemListRemote(sqlItemList, explainContent);

                explainJob.setJobStatus(JobStatusEnum.statusComplate.getCode());
            } catch (Exception e) {
                logger.error("analysis repo:" + explainInfo.getRepo() + " branch:" + explainJob.getJobBranch() + "failure!" + e.getMessage(), e);
                explainJob.setJobStatus(JobStatusEnum.statusExecutionError.getCode());
            }
            explainJob.setUniqueField(System.currentTimeMillis());
            explainJob.setJobVersion(explainJob.getJobVersion() + 1);
            explainDao.updateExplainJob(explainJob);
            logger.info("finish analysis repo:" + explainInfo.getRepo() + " branch:" + explainJob.getJobBranch());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initTran();

        schedulerManager.registerJob(this, taskCorn, ExplainTask.class.getSimpleName());

        //任务随进程停止被挂起，重启进程后，重置执行中的任务
        ExplainJob explainJob = new ExplainJob();
        explainJob.setJobStatus(JobStatusEnum.statusExecution.getCode());
        List<ExplainJob> jobList = explainDao.queryExplainJobs(explainJob);
        for(ExplainJob job : jobList) {
            job.setJobStatus(JobStatusEnum.statusStart.getCode());

            explainDao.updateExplainJob(job);
        }
    }

    private void initTran() {
        HashMap<String, String> configMap = acqConifMap();
        String appId = configMap.get(ExplainCdlItemEnum.EXPLAIN_CDL_APP_ID.getItemKey());
        String appKey = configMap.get(ExplainCdlItemEnum.EXPLAIN_CDL_APP_KEY.getItemKey());
        String appName = configMap.get(ExplainCdlItemEnum.EXPLAIN_CDL_APP_NAME.getItemKey());
        String env = configMap.get(ExplainCdlItemEnum.EXPLAIN_CDL_ENV.getItemKey());
        String groupName = configMap.get(ExplainCdlItemEnum.EXPLAIN_CDL_GROUP_NAME.getItemKey());

        TransRemote.setAppId(appId);
        TransRemote.setAppKey(appKey);
        TransRemote.setAppName(appName);
        TransRemote.setEnv(env);
        TransRemote.setGroupName(groupName);
    }
}
