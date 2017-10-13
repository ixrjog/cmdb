package com.sdg.cmdb.domain.logService.logServiceQuery;


public interface LogServiceQuery {

    String acqQueryDate();

    int acqToMinutes();


    // 0  nginx
    // 1  java
    int acqQueryType();

    LogServiceQueryCfg acqLogServiceQueryCfg();


}
