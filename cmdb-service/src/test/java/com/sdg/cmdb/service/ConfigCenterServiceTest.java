package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.LdapItemEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.ZabbixItemEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.*;
import com.sdg.cmdb.domain.server.ServerDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by liangjian on 2017/5/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ConfigCenterServiceTest {

    @Resource
    private ConfigCenterService configCenterService;

    @Test
    public void testRefreshCache() {
        configCenterService.refreshCache(ConfigCenterItemGroupEnum.LDAP.getItemKey());

//        HashMap<String, String> map = configCenterService.getItemGroup(ConfigCenterItemGroupEnum.LDAP.getItemKey(), "online");
//        System.err.println(map.get(LdapItemEnum.LDAP_URL.getItemKey()));
//        System.err.println(map.get(LdapItemEnum.LDAP_BIND_DN.getItemKey()));
//        System.err.println(map.get(LdapItemEnum.LDAP_PASSWD.getItemKey()));
//        System.err.println(map.get(LdapItemEnum.LDAP_USER_DN.getItemKey()));
//        System.err.println(map.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey()));
//        System.err.println(map.get(LdapItemEnum.LDAP_GROUP_FILTER.getItemKey()));
//        System.err.println(map.get(LdapItemEnum.LDAP_GROUP_PREFIX.getItemKey()));
    }

    @Test
    public void testGetLdapItemGroup() {
        HashMap<String, String> map = configCenterService.getItemGroup(ConfigCenterItemGroupEnum.LDAP.getItemKey());
        System.err.println(map.get(LdapItemEnum.LDAP_URL.getItemKey()));
        System.err.println(map.get(LdapItemEnum.LDAP_BIND_DN.getItemKey()));
        System.err.println(map.get(LdapItemEnum.LDAP_PASSWD.getItemKey()));
        System.err.println(map.get(LdapItemEnum.LDAP_USER_DN.getItemKey()));
        System.err.println(map.get(LdapItemEnum.LDAP_GROUP_DN.getItemKey()));
        System.err.println(map.get(LdapItemEnum.LDAP_GROUP_FILTER.getItemKey()));
        System.err.println(map.get(LdapItemEnum.LDAP_GROUP_PREFIX.getItemKey()));
    }

    @Test
    public void testGetAnsibleItemGroup() {
        configCenterService.refreshCache(ConfigCenterItemGroupEnum.ANSIBLE.getItemKey());
        HashMap<String, String> map = configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ANSIBLE.getItemKey());
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_ALL_HOSTS_PATH.getItemKey()));
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_BIN.getItemKey()));
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_CREATE_CI_DEPLOY_DIR.getItemKey()));
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_GETWAY_ACCOUNT_PATH.getItemKey()));
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_GETWAY_GROUP.getItemKey()));
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_HOSTS_PATH.getItemKey()));
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_INIT_SYSTEM_INVOKE.getItemKey()));
        System.err.println(map.get(AnsibleItemEnum.ANSIBLE_LOGCLEANUP_INVOKE.getItemKey()));
    }

    @Test
    public void testGetZabbixItemGroup() {
        HashMap<String, String> map = configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ZABBIX.getItemKey());
        System.err.println(map.get(ZabbixItemEnum.ZABBIX_API_URL.getItemKey()));
        System.err.println(map.get(ZabbixItemEnum.ZABBIX_API_USER.getItemKey()));
        System.err.println(map.get(ZabbixItemEnum.ZABBIX_API_PASSWD.getItemKey()));
        System.err.println(map.get(ZabbixItemEnum.ZABBIX_API_KEY.getItemKey()));

    }

    @Test
    public void testGetAliyunEcsItemGroup() {
        configCenterService.refreshCache(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
        configCenterService.refreshCache(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
        HashMap<String, String> map = configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
        System.err.println(map.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_SECRET.getItemKey()));
        //System.err.println(map.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_KEY.getItemKey()));
        //System.err.println(map.get(AliyunEcsItemEnum.ALIYUN_ECS_IMAGE_ID.getItemKey()));
    }

    @Test
    public void testGetPublicItemGroup() {
        configCenterService.refreshCache(ConfigCenterItemGroupEnum.PUBLIC.getItemKey(),"online");
        HashMap<String, String> map = configCenterService.getItemGroup(ConfigCenterItemGroupEnum.PUBLIC.getItemKey());
        System.err.println(map.get(PublicItemEnum.OFFICE_DMZ_IP_NETWORK_ID.getItemKey()));
        System.err.println(map.get(PublicItemEnum.IPTABLES_WEBSERVICE_PATH.getItemKey()));
        System.err.println(map.get(PublicItemEnum.ADMIN_PASSWD.getItemKey()));
        System.err.println(map.get(PublicItemEnum.TOMCAT_CONFIG_NAME.getItemKey()));
        System.err.println(map.get(PublicItemEnum.DEPLOY_PATH.getItemKey()));
    }


    @Test
    public void testGetItem() {
        String value = configCenterService.getItem(ConfigCenterItemGroupEnum.LDAP.getItemKey(), "LDAP_URL");
        System.err.println(value);
    }


}
