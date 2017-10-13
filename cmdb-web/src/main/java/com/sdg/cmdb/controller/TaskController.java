package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.service.ServerTaskService;
import com.sdg.cmdb.zabbix.LogCleanupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/3/31.
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    @Resource
    private LogCleanupService logCleanupService;

    @Resource
    private ServerTaskService serverTaskService;


    /**
     * 日志弹性清理分页数据查询
     *
     * @param serverGroupId
     * @param serverName
     * @param enabled
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/logcleanup/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogCleanupPage(
            @RequestParam long serverGroupId,
            @RequestParam String serverName,
            @RequestParam int enabled,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(logCleanupService.getLogCleanupPage(serverGroupId, serverName, enabled, page, length));
    }

    /**
     * 清理服务器组日志
     *
     * @param serverId
     * @return
     */
    @RequestMapping(value = "/logcleanup/cleanup", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCleanup(
            @RequestParam long serverId) {
        return new HttpResult(logCleanupService.cleanup(serverId));
    }

    /**
     * 同步数据
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/refresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRefresh() {
        logCleanupService.syncData();
        return new HttpResult(new BusinessWrapper<>(true));
    }

    /**
     * 同步数据
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/setEnabled", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSetEnabled(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.setEnabled(serverId)));
    }

    /**
     * 减少清理日期
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/subtractHistory", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSubtractHistory(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.setHistory(serverId, -1)));
    }

    /**
     * 增加清理日期
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/addHistory", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getAddHistory(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.setHistory(serverId, 1)));
    }

    /**
     * 增加清理日期
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/refreshDiskRate", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRefreshDiskRate(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.refreshDiskRate(serverId)));
    }


    /**
     * 初始化系统
     *
     * @return
     */
    @RequestMapping(value = "/servertask/initializationSystem", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getInitializationSystem(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(serverTaskService.initializationSystem(serverId)));
    }


    /**
     * 修改日志保留天数
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/save", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSaveLogcleanup(@RequestParam long id,@RequestParam int history) {

        return new HttpResult(new BusinessWrapper<>(logCleanupService.saveHistory(id, history)));
    }


}
