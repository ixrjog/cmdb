package com.sdg.cmdb.api;

import com.sdg.cmdb.domain.BusinessWrapper;

/**
 * Created by zxxiao on 2016/11/1.
 */
public interface CmdbService {

    /**
     * 指定用户离职
     * @param username
     */
    BusinessWrapper<Boolean> leaving(String username);
}
