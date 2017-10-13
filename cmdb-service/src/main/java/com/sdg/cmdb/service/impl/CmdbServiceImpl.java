package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.api.CmdbService;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2016/11/1.
 */
@Service("cmdbService")
public class CmdbServiceImpl implements CmdbService {

    private static final Logger logger = LoggerFactory.getLogger(CmdbServiceImpl.class);

    @Resource
    private AuthService authService;

    @Override
    public BusinessWrapper<Boolean> leaving(String username) {
        return authService.leaving(username);
    }
}
