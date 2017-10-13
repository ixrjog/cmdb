package com.sdg.cmdb.dao;

import com.sdg.cmdb.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/1/13.
 */
@Component
public interface ZabbixServerDao {

    /**
     * 获取服务器数目
     *
     * @param serverList
     * @param serverName
     * @param useType
     * @param envType
     * @return
     */
    long getServerSize(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp);


    /**
     * 获取服务器详情
     *
     * @param serverList
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param pageStart
     * @param length
     * @return
     */
    List<ServerDO> getServerPage(
            @Param("list") List<Long> serverList,
            @Param("serverGroupId") long serverGroupId,
            @Param("serverName") String serverName,
            @Param("useType") int useType,
            @Param("envType") int envType,
            @Param("queryIp") String queryIp,
            @Param("pageStart") long pageStart, @Param("length") int length);



}
