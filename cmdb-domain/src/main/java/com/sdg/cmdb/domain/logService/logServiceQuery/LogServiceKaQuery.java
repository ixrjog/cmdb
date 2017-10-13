package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceCfgDO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQuery;

import java.io.Serializable;

public class LogServiceKaQuery implements LogServiceQuery, Serializable {
    private static final long serialVersionUID = 8267632330712572854L;

    private LogServiceCfgDO LogServiceCfg;

    private String args;

    private String mobile;

    private String queryDate;

    private String queryTime;

    private String requestTime;

    private String sourceIp;

    private String status;

    private int toMinutes;

    private String uri;

    @Override
    public String acqQueryDate() {
        return getQueryDate() + " " + getQueryTime();
    }

    @Override
    public int acqToMinutes() {
        return toMinutes;
    }

    @Override
    public int acqQueryType(){
        return 0;
    }


    @Override
    public LogServiceQueryCfg acqLogServiceQueryCfg() {
        return getLogServiceCfg();
    }


    public LogServiceCfgDO getLogServiceCfg() {
        return LogServiceCfg;
    }

    public void setLogServiceCfg(LogServiceCfgDO logServiceCfg) {
        LogServiceCfg = logServiceCfg;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getToMinutes() {
        return toMinutes;
    }

    public void setToMinutes(int toMinutes) {
        this.toMinutes = toMinutes;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
