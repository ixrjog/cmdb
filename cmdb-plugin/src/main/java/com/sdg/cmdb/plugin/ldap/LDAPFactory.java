package com.sdg.cmdb.plugin.ldap;

import com.sdg.cmdb.domain.ldap.LdapDO;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxxiao on 2017/6/23.
 */
@Service
public class LDAPFactory {

    private Map<String, LdapTemplate> ldapTemplateMap = new ConcurrentHashMap<>();

    /**
     * 获取指定类型的ldapTemplate实例
     * @param ldapType
     * @return
     */
    public LdapTemplate getLdapTemplateInstanceByType(String ldapType) {
        if (ldapTemplateMap.containsKey(ldapType)) {
            return ldapTemplateMap.get(ldapType);
        } else {
            return null;
        }
    }

    /**
     * 构建ldapTemplate实例
     * @param ldapDO
     * @return
     */
    public LdapTemplate buildLdapTemplate(LdapDO ldapDO) {
        LdapTemplate ldapTemplate;
        if (ldapTemplateMap.containsKey(ldapDO.getLdapType())) {
            ldapTemplate = ldapTemplateMap.get(ldapDO.getLdapType());
        } else {
            LdapContextSource contextSource = buildLdapContextSource(ldapDO);
            TransactionAwareContextSourceProxy sourceProxy = buildTransactionAwareContextSourceProxy(contextSource);

            ldapTemplate = new LdapTemplate(sourceProxy);
            ldapTemplateMap.put(ldapDO.getLdapType(), ldapTemplate);
        }
        return ldapTemplate;
    }

    private static LdapContextSource buildLdapContextSource(LdapDO ldapDO) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapDO.getUrl());
        contextSource.setBase(ldapDO.getBase());
        contextSource.setUserDn(ldapDO.getUserDn());
        contextSource.setPassword(ldapDO.getPassword());
        contextSource.setPooled(true);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    private static TransactionAwareContextSourceProxy buildTransactionAwareContextSourceProxy(LdapContextSource contextSource) {
        TransactionAwareContextSourceProxy sourceProxy = new TransactionAwareContextSourceProxy(contextSource);
        return sourceProxy;
    }
}
