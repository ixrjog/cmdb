package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterDO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/5/26.
 */
public interface ConfigCenterService {

    /**
     * 取配置项组
     *
     * @param itemGroup
     * @return
     */
    HashMap<String, String> getItemGroup(String itemGroup);


    /**
     * 取配置
     *
     * @param item
     * @return
     */
    String getItem(String itemGroup, String item);

    /**
     * 配置中心详情页
     *
     * @param item
     * @param itemGroup
     * @param env
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ConfigCenterDO>> getConfigCenterPage(String item, String itemGroup, String env, int page, int length);

    /**
     * 更新缓存
     *
     * @param itemGroup
     * @return
     */
    BusinessWrapper<Boolean> refreshCache(String itemGroup);

    boolean refreshCache(String itemGroup,String env);

    BusinessWrapper<Boolean> saveConfigCenter(ConfigCenterDO configCenterDO);

    BusinessWrapper<Boolean> delConfigCenter(long id);

}
