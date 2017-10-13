package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;

/**
 * Created by liangjian on 2017/4/27.
 */
public interface DingtalkService {


    /**
     * 部署消息通知-钉钉群
     * @param ciDeployStatisticsDO
     * @throws Exception
     */
    boolean sendCiDeployMsg(CiDeployStatisticsDO ciDeployStatisticsDO) throws Exception;
}
