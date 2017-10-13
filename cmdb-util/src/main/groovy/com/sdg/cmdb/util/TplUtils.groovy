package com.sdg.cmdb.util

import com.sdg.cmdb.domain.auth.UserDO
import com.sdg.cmdb.domain.todo.TodoDailyDO
import com.sdg.cmdb.domain.todo.TodoDailyVO
import com.sdg.cmdb.domain.todo.TodoDetailDO
import com.sdg.cmdb.domain.todo.TodoDetailVO
import groovy.text.SimpleTemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils

/**
 * Created by zxxiao on 2016/10/18.
 */
class TplUtils {

    public static String todoAddress = "http://cmdb.51xianqu.net/#/access/todo?id="

    public static String todoUrl = "https://work.52shangou.com/#/app/todo"

    /**
     * 获取新工单邮件模板最终结果
     * @param userDO
     * @param dailyVO
     * @return
     */
    public static String doNewTodoNotify(UserDO userDO, TodoDailyVO dailyVO, String notifyUserName) {
        def engine = new MarkupTemplateEngine(buildTplConfiguration());
        URL url = engine.resolveTemplate("emailTpl/todo_new.tpl");

        Map<String, Object> binding = new HashMap<>();
        binding.put("displayName", notifyUserName)
        binding.put("todoAddress", todoAddress + dailyVO.getId())
        binding.put("launchUser", userDO.getDisplayName())
        binding.put("todoType", dailyVO.getLevelOne().getConfigName() + " - " + dailyVO.getLevelTwo().getConfigName())
        binding.put("todoPrivacy", TodoDailyDO.TodoPrivacyEnum.getCodeDesc(dailyVO.getPrivacy()))
        binding.put("todoUrgent", TodoDailyDO.TodoUrgentEnum.getCodeDesc(dailyVO.getUrgents()))
        binding.put("todoStatus", TodoDailyDO.TodoStatusEnum.getCodeDesc(dailyVO.getTodoStatus()))
        binding.put("todoConfirm", TodoDailyDO.TodoConfirmEnum.getCodeDesc(dailyVO.getHasConfirm()))
        binding.put("gmtCreate", dailyVO.getGmtCreate())
        binding.put("todoContent", dailyVO.getContents())

        def simpleEngine = new SimpleTemplateEngine()
        def template = simpleEngine.createTemplate(url).make(binding);

        return template.toString();
    }

    /**
     * 获取工单反馈邮件模板最终结果
     * @param userDO
     * @param dailyVO
     * @return
     */
    public static String doInitFeedbackNotify(UserDO userDO, TodoDailyVO dailyVO, String notifyUserName) {
        def engine = new MarkupTemplateEngine(buildTplConfiguration());
        URL url = engine.resolveTemplate("emailTpl/todo_init_feedback.tpl");

        Map<String, Object> binding = new HashMap<>();
        binding.put("displayName", notifyUserName)
        binding.put("todoAddress", todoAddress + dailyVO.getId())
        binding.put("launchUser", userDO.getDisplayName())
        binding.put("todoType", dailyVO.getLevelOne().getConfigName() + " - " + dailyVO.getLevelTwo().getConfigName())
        binding.put("todoPrivacy", TodoDailyDO.TodoPrivacyEnum.getCodeDesc(dailyVO.getPrivacy()))
        binding.put("todoUrgent", TodoDailyDO.TodoUrgentEnum.getCodeDesc(dailyVO.getUrgents()))
        binding.put("todoStatus", TodoDailyDO.TodoStatusEnum.getCodeDesc(dailyVO.getTodoStatus()))
        binding.put("todoConfirm", TodoDailyDO.TodoConfirmEnum.getCodeDesc(dailyVO.getHasConfirm()))
        binding.put("gmtCreate", dailyVO.getGmtCreate())
        binding.put("gmtModify", dailyVO.getGmtModify())
        binding.put("todoContent", dailyVO.getContents())
        binding.put("todoFeedback", dailyVO.getFeedbackContent())

        def simpleEngine = new SimpleTemplateEngine()
        def template = simpleEngine.createTemplate(url).make(binding);

        return template.toString();
    }

    /**
     * 获取工单反馈邮件模板最终结果
     * @param userDO
     * @param dailyVO
     * @return
     */
    public static String doAcceptFeedbackNotify(UserDO userDO, TodoDailyVO dailyVO, String notifyUserName) {
        def engine = new MarkupTemplateEngine(buildTplConfiguration());
        URL url = engine.resolveTemplate("emailTpl/todo_accept_feedback.tpl");

        Map<String, Object> binding = new HashMap<>();
        binding.put("displayName", notifyUserName)
        binding.put("todoAddress", todoAddress + dailyVO.getId())
        binding.put("launchUser", userDO.getDisplayName())
        binding.put("todoType", dailyVO.getLevelOne().getConfigName() + " - " + dailyVO.getLevelTwo().getConfigName())
        binding.put("todoPrivacy", TodoDailyDO.TodoPrivacyEnum.getCodeDesc(dailyVO.getPrivacy()))
        binding.put("todoUrgent", TodoDailyDO.TodoUrgentEnum.getCodeDesc(dailyVO.getUrgents()))
        binding.put("todoStatus", TodoDailyDO.TodoStatusEnum.getCodeDesc(dailyVO.getTodoStatus()))
        binding.put("todoConfirm", TodoDailyDO.TodoConfirmEnum.getCodeDesc(dailyVO.getHasConfirm()))
        binding.put("gmtCreate", dailyVO.getGmtCreate())
        binding.put("gmtModify", dailyVO.getGmtModify())
        binding.put("todoContent", dailyVO.getContents())
        binding.put("todoFeedback", dailyVO.getFeedbackContent())

        def simpleEngine = new SimpleTemplateEngine()
        def template = simpleEngine.createTemplate(url).make(binding);

        return template.toString();
    }

    /**
     * 获取工单反馈邮件模板最终结果
     * @param userDO
     * @param dailyVO
     * @return
     */
    public static String doFinishNotify(UserDO userDO, TodoDailyVO dailyVO, String notifyUserName) {
        def engine = new MarkupTemplateEngine(buildTplConfiguration());
        URL url = engine.resolveTemplate("emailTpl/todo_finish.tpl");

        Map<String, Object> binding = new HashMap<>();
        binding.put("displayName", notifyUserName)
        binding.put("todoAddress", todoAddress + dailyVO.getId())
        binding.put("launchUser", userDO.getDisplayName())
        binding.put("todoType", dailyVO.getLevelOne().getConfigName() + " - " + dailyVO.getLevelTwo().getConfigName())
        binding.put("todoPrivacy", TodoDailyDO.TodoPrivacyEnum.getCodeDesc(dailyVO.getPrivacy()))
        binding.put("todoUrgent", TodoDailyDO.TodoUrgentEnum.getCodeDesc(dailyVO.getUrgents()))
        binding.put("todoStatus", TodoDailyDO.TodoStatusEnum.getCodeDesc(dailyVO.getTodoStatus()))
        binding.put("todoConfirm", TodoDailyDO.TodoConfirmEnum.getCodeDesc(dailyVO.getHasConfirm()))
        binding.put("gmtCreate", dailyVO.getGmtCreate())
        binding.put("gmtModify", dailyVO.getGmtModify())
        binding.put("todoContent", dailyVO.getContents())

        Date endDate = DateUtils.parseDate(dailyVO.getGmtModify().substring(0, 19), TimeUtils.timeFormat);
        Date headDate = DateUtils.parseDate(dailyVO.getGmtCreate().substring(0, 19), TimeUtils.timeFormat);
        binding.put("todoHaoshi", TimeUtils.calculateDateDifference(headDate, endDate))

        def simpleEngine = new SimpleTemplateEngine()
        def template = simpleEngine.createTemplate(url).make(binding);

        return template.toString();
    }

    private static TemplateConfiguration buildTplConfiguration() {
        TemplateConfiguration configuration = new TemplateConfiguration();
        configuration.setDeclarationEncoding("UTF-8");
        configuration.setExpandEmptyElements(true);
        configuration.setUseDoubleQuotes(true);
        configuration.setAutoNewLine(true);
        configuration.setAutoIndent(true);
        configuration.setAutoEscape(true);

        return configuration;
    }

    /**
     * 获取提交的工单邮件模板
     * @param todoDetailVO
     * @param notifyUserName
     * @return
     */
    public static String doSubmitTodoNotify(TodoDetailVO todoDetailVO, String notifyUserName) {
        def engine = new MarkupTemplateEngine(buildTplConfiguration());
        URL url = engine.resolveTemplate("emailTpl/todo_submit.tpl");

        Map<String, Object> binding = new HashMap<>();
        binding.put("displayName", notifyUserName)
        binding.put("todoAddress", todoUrl)
        binding.put("id", todoDetailVO.getId())
        binding.put("initiatorUser", todoDetailVO.getInitiatorUsername() + "<" + todoDetailVO.initiatorUserDO.displayName + ">")
        // 工单状态
        binding.put("todoStatus", TodoDetailDO.TodoStatusEnum.getTodoStatusName(todoDetailVO.getTodoStatus()));
       // binding.put("todoStatus", TodoDetailDO.TodoStatusEnum.getCodeDesc(todoDetailVO.getTodoStatus()))
        // 工单类型+名称
        binding.put("todoTitle", todoDetailVO.todoDO.getTitle())
        if(StringUtils.isEmpty(todoDetailVO.getGmtModify())){
            binding.put("gmtCreate", todoDetailVO.getGmtCreate())
        }else{
            binding.put("gmtCreate", todoDetailVO.getGmtModify())
        }
        binding.put("todoContent", todoDetailVO.getContent())

        def simpleEngine = new SimpleTemplateEngine()
        def template = simpleEngine.createTemplate(url).make(binding);

        return template.toString();
    }

    /**
     * 获取完成的工单邮件模板
     * @param todoDetailVO
     * @param notifyUserName
     * @return
     */
    public static String doCompleteTodoNotify(TodoDetailVO todoDetailVO, String notifyUserName) {
        def engine = new MarkupTemplateEngine(buildTplConfiguration());
        URL url = engine.resolveTemplate("emailTpl/todo_complete.tpl");

        Map<String, Object> binding = new HashMap<>();
        binding.put("displayName", notifyUserName)
        binding.put("todoAddress", todoUrl)
        binding.put("id", todoDetailVO.getId())
        binding.put("initiatorUser", todoDetailVO.getInitiatorUsername() + "<" + todoDetailVO.initiatorUserDO.displayName + ">")
        // 工单状态
        binding.put("todoStatus", TodoDetailDO.TodoStatusEnum.getTodoStatusName(todoDetailVO.getTodoStatus()));
        // 负责人
        binding.put("assigneeUser", todoDetailVO.getAssigneeUserDO().getUsername() +   "<" + todoDetailVO.getAssigneeUserDO().displayName + ">");
        // 工单类型+名称
        binding.put("todoTitle", todoDetailVO.todoDO.getTitle())
        if(StringUtils.isEmpty(todoDetailVO.getGmtModify())){
            binding.put("gmtCreate", todoDetailVO.getGmtCreate())
        }else{
            binding.put("gmtCreate", todoDetailVO.getGmtModify())
        }
        binding.put("todoContent", todoDetailVO.getContent())

        def simpleEngine = new SimpleTemplateEngine()
        def template = simpleEngine.createTemplate(url).make(binding);

        return template.toString();
    }

}
