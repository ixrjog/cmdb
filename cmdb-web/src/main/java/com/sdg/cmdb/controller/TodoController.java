package com.sdg.cmdb.controller;

import com.sdg.cmdb.dao.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.auth.CiUserGroupDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerVO;
import com.sdg.cmdb.domain.todo.*;
import com.sdg.cmdb.service.ServerGroupService;
import com.sdg.cmdb.service.TodoService;
import com.sdg.cmdb.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 16/10/9.
 */
@Controller
@RequestMapping("/todo")
public class TodoController {

    @Resource
    private TodoService todoService;

    @Resource
    private ServerGroupService serverGroupService;

    /**
     * 配置项的新增 or 更新
     *
     * @param configDO
     * @return
     */
    @RequestMapping(value = "/config/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveTodoConfig(@RequestBody TodoConfigDO configDO) {
        BusinessWrapper<Boolean> wrapper = todoService.saveTodoConfig(configDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的配置项
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/config/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delTodoConfig(@RequestParam long itemId) {
        BusinessWrapper<Boolean> wrapper = todoService.delTodoConfig(itemId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询合适条件下的工单配置项分页数据
     *
     * @param queryName
     * @param parent
     * @param valid
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/config/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryTodoConfigPage(
            @RequestParam String queryName, @RequestParam long parent, @RequestParam int valid,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(todoService.queryConfigPage(queryName, parent, valid, page, length));
    }

    /**
     * 查询指定父id的子item集合
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/config/children/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryTodoChildrenConfig(@RequestParam long parentId) {
        return new HttpResult(todoService.queryChildrenById(parentId));
    }

    /**
     * 查询工单分页数据
     *
     * @param queryDO
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/daily/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult queryTodoPage(
            @RequestBody TodoDailyQueryDO queryDO,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(todoService.queryTodoPage(queryDO, page, length));
    }

    /**
     * 日常工单新增 or 更新
     *
     * @param dailyDO
     * @return
     */
    @RequestMapping(value = "/daily/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveTodoItem(@RequestBody TodoDailyDO dailyDO) {
        BusinessWrapper<Boolean> wrapper = todoService.saveTodoItem(dailyDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询工单待处理分页数据
     *
     * @param queryDO
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/process/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult queryTodoProcessPage(
            @RequestBody TodoDailyQueryDO queryDO,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(todoService.queryTodoProcessPage(queryDO, page, length));
    }

    /**
     * 查询指定工单的数据
     *
     * @param dailyId
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryTodoById(@RequestParam long dailyId) {
        return new HttpResult(todoService.queryTodoById(dailyId));
    }

    /**
     * 查询工单的列表
     *
     * @return
     */
    @RequestMapping(value = "/group/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryTodoGroup() {
        return new HttpResult(todoService.queryTodoGroup());
    }

    //// TODO 新工单接口

    /**
     * 查询工单的列表
     *
     * @return
     */
    @RequestMapping(value = "/todoDetail/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryTodo(@RequestParam long id) {
        return new HttpResult(todoService.queryTodoDetail(id));
    }


    /**
     * 创建工单的列表
     *
     * @return
     */
    @RequestMapping(value = "/establish", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addTodo(@RequestParam long todoId) {
        return new HttpResult(todoService.establishTodo(todoId));
    }


    /**
     * 提交工单
     *
     * @return
     */
    @RequestMapping(value = "/todoDetail/submit", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult submitTodoDetail(@RequestParam long id) {
        return new HttpResult(todoService.submitTodoDetail(id));
    }

    /**
     * 提交工单
     *
     * @return
     */
    @RequestMapping(value = "/queryMyJob", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryMyJob() {
        return new HttpResult(todoService.queryMyTodoJob());
    }


    /**
     * 提交工单
     *
     * @return
     */
    @RequestMapping(value = "/queryCompleteJob", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryCompleteJob() {
        return new HttpResult(todoService.queryCompleteTodoJob());
    }

    /**
     * 撤销工单
     *
     * @return
     */
    @RequestMapping(value = "/revokeTodoDetail", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult revokeTodoDetail(@RequestParam long id) {
        return new HttpResult(todoService.revokeTodoDetail(id));
    }

    /**
     * 执行工单
     *
     * @return
     */
    @RequestMapping(value = "/invokeTodoDetail", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult processingTodoDetail(@RequestParam long id) {
        return new HttpResult(todoService.processingTodoDetail(id));
    }

    /**
     * 堡垒机工单中添加服务器组
     *
     * @param todoKeyboxDetailDO
     * @return
     */
    @RequestMapping(value = "/todoDetail/addTodoKeybox", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveKeyboxGroup(@RequestBody TodoKeyboxDetailDO todoKeyboxDetailDO) {
        return   new HttpResult( todoService.saveKeyboxDetail(todoKeyboxDetailDO));
    }

    /**
     * 堡垒机工单中移除服务器组
     * @param id
     * @return
     */
    @RequestMapping(value = "/todoDetail/delTodoKeybox", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delKeyboxGroup(@RequestParam long id) {
        return new HttpResult(todoService.delKeyboxDetail(id));
    }

    /**
     * 持续集成工单中添加服务器组
     *
     * @param todoCiUserGroupDetailDO
     * @return
     */
    @RequestMapping(value = "/todoDetail/addTodoCiUserGroup", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveCiUserGroup(@RequestBody TodoCiUserGroupDetailDO todoCiUserGroupDetailDO) {
        return   new HttpResult( todoService.saveCiUserGroupDetail(todoCiUserGroupDetailDO));
    }

    /**
     * 持续集成工单中移除服务器组
     * @param id
     * @return
     */
    @RequestMapping(value = "/todoDetail/delTodoCiUserGroup", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delCiUserGroup(@RequestParam long id) {
        return new HttpResult(todoService.delCiUserGroupDetail(id));
    }

    /**
     * 平台权限申请设置按钮 添加&移除
     *
     * @return
     */
    @RequestMapping(value = "/todoDetail/setTodoSystemAuth", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult setTodoSystemAuth(@RequestParam long id) {
        return new HttpResult(todoService.setTodoSystemAuth(id));
    }

}
