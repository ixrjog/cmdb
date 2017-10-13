package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.dao.ConfigCenterDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerPerfVO;
import com.sdg.cmdb.service.ConfigCenterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/5/26.
 */
@Service
public class ConfigCenterServiceImpl implements ConfigCenterService {

    public final static String DEFAULT_ENV = "online";

    private static final String CONFIG_CENTER_ITEMGROUP_KEY = "ConfigCenterItemGroup:";

    private static final String CONFIG_CENTER_ITEM_KEY = "ConfigCenterItem:";

    @Resource
    private ConfigCenterDao configCenterDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    /**
     * 插入指定key-value    有效期无限制
     */
    public void insert(HashMap<String, String> map, String name, String env) {
        String sp = JSON.toJSONString(map);
        redisTemplate.opsForValue().set(CONFIG_CENTER_ITEMGROUP_KEY + name + ":" + env, sp);
    }

    /**
     * 查询指定的itemGroup配置，并缓存
     */
    private HashMap<String, String> queryItemGroup(String itemGroup, String env) {
        Object value = redisTemplate.opsForValue().get(CONFIG_CENTER_ITEMGROUP_KEY + itemGroup + ":" + env);
        if (value == null) {
            List<ConfigCenterDO> configList = configCenterDao.queryConfigCenterByItemGroup(itemGroup, env);
            HashMap<String, String> map = new HashMap<String, String>();
            if (configList.size() == 0) return map;
            for (ConfigCenterDO configCenterDO : configList)
                map.put(configCenterDO.getItem(), configCenterDO.getValue());
            insert(map, itemGroup, env);
            return map;
        } else {
            String obj = value.toString();
            HashMap<String, String> map = JSON.parseObject(obj, HashMap.class);
            return map;
        }
    }

    @Override
    public HashMap<String, String> getItemGroup(String itemGroup) {
        HashMap<String, String> map = queryItemGroup(itemGroup, invokeEnv);
        // 若没匹配到当你env配置，则读取DEFAULT_ENV配置
        if (map.isEmpty() && !invokeEnv.equalsIgnoreCase(DEFAULT_ENV))
            return queryItemGroup(itemGroup, DEFAULT_ENV);
        return map;
    }

    @Override
    public String getItem(String itemGroup, String item) {
        HashMap<String, String> map = getItemGroup(itemGroup);
        if (map.containsKey(item)) {
            return map.get(item);
        } else {
            return "";
        }
    }

    @Override
    public TableVO<List<ConfigCenterDO>> getConfigCenterPage(String item, String itemGroup, String env, int page, int length) {
        long size = configCenterDao.getConfigCenterSize(item, itemGroup, env);
        List<ConfigCenterDO> configCenterDOList = configCenterDao.getConfigCenterPage(item, itemGroup, env, page * length, length);
        return new TableVO<>(size, configCenterDOList);
    }

    @Override
    public BusinessWrapper<Boolean> refreshCache(String itemGroup) {
        try {
            redisTemplate.delete(CONFIG_CENTER_ITEMGROUP_KEY + itemGroup + ":" + invokeEnv);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public boolean refreshCache(String itemGroup, String env) {
        try {
            redisTemplate.delete(CONFIG_CENTER_ITEMGROUP_KEY + itemGroup + ":" + env);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public BusinessWrapper<Boolean> saveConfigCenter(ConfigCenterDO configCenterDO) {
        try {
            //ConfigCenterDO configCenterItem = configCenterDao.queryConfigCenterByItem(configCenterDO.getItem(), configCenterDO.getEnv());
            if (configCenterDO.getId() == 0) {
                configCenterDao.addConfigCenter(configCenterDO);
            } else {
                configCenterDao.updateConfigCenter(configCenterDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delConfigCenter(long id) {
        try {
            configCenterDao.delConfigCenter(id);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

}
