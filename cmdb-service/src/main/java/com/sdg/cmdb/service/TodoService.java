package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.todo.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 16/10/9.
 */
public interface TodoService {

    /**
     * 新增 or 更新配置项
     *
     * @param todoConfigDO
     * @return
     */
    BusinessWrapper<Boolean> saveTodoConfig(TodoConfigDO todoConfigDO);

    /**
     * 删除指定id的配置项
     *
     * @param itemId
     * @return
     */
    BusinessWrapper<Boolean> delTodoConfig(long itemId);

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
    TableVO<List<TodoConfigVO>> queryConfigPage(String queryName, long parent, int valid, int page, int length);

    /**
     * 查询指定配置项的子项目
     *
     * @param parentId
     * @return
     */
    List<TodoConfigDO> queryChildrenById(long parentId);

    /**
     * 查询工单分页数据
     *
     * @param queryDO
     * @param page
     * @param length
     * @return
     */
    TableVO<List<TodoDailyVO>> queryTodoPage(TodoDailyQueryDO queryDO, int page, int length);

    /**
     * 保存 or 更新日常工单
     *
     * @param dailyDO
     * @return
     */
    BusinessWrapper<Boolean> saveTodoItem(TodoDailyDO dailyDO);

    /**
     * 查询工单待处理分页数据
     *
     * @param queryDO
     * @param page
     * @param length
     * @return
     */
    TableVO<List<TodoDailyVO>> queryTodoProcessPage(TodoDailyQueryDO queryDO, int page, int length);

    /**
     * 查询指定工单的数据
     *
     * @param id
     * @return
     */
    TodoDailyVO queryTodoById(long id);

    List<TodoGroupVO> queryTodoGroup();

    TodoDetailVO queryTodoDetail(long id);

    /**
     * 创建工单
     *
     * @param todoId
     * @return
     */
    TodoDetailVO establishTodo(long todoId);

    /**
     * 提交工单（配置完成）
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> submitTodoDetail(long id);

    BusinessWrapper<Boolean> saveKeyboxDetail(TodoKeyboxDetailDO todoKeyboxDetailDO);

    BusinessWrapper<Boolean> delKeyboxDetail(long todoKeyboxDetailId);

    BusinessWrapper<Boolean> saveCiUserGroupDetail(TodoCiUserGroupDetailDO ciUserGroupDetailDO);

    BusinessWrapper<Boolean> delCiUserGroupDetail(long todoCiUserGroupDetailId);

    BusinessWrapper<Boolean> setTodoSystemAuth(long todoSystemAuthDetailId);

    /**
     * 查询我的待办工单
     *
     * @return
     */
    List<TodoDetailVO> queryMyTodoJob();

    /**
     * 撤销工单
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> revokeTodoDetail(long id);

    /**
     * 处理工单
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> processingTodoDetail(long id);

    /**
     * 查询我完成的工单
     *
     * @return
     */
    List<TodoDetailVO> queryCompleteTodoJob();

}
