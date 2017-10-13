package com.sdg.cmdb.service.control.configurationfile.impl;

import com.sdg.cmdb.dao.DnsDao;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.service.DnsService;

import java.util.Date;
import java.util.List;

import com.sdg.cmdb.service.control.configurationfile.DnsmasqService;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/7/11.
 */
@Service
public class DnsmasqServiceImpl implements DnsmasqService {


    @Resource
    private DnsDao dnsDao;

    @Override
    public String acqConfig(int type) {

        String result = getHeadInfo();
        List<DnsmasqDO> listSystemItems = dnsDao.queryDnsmasqByGroupIdAndItemType(type, DnsmasqDO.ItemTypeEnum.system.getCode());
        List<DnsmasqDO> listServerItems = dnsDao.queryDnsmasqByGroupIdAndItemType(type, DnsmasqDO.ItemTypeEnum.server.getCode());
        List<DnsmasqDO> listAddressItems = dnsDao.queryDnsmasqByGroupIdAndItemType(type, DnsmasqDO.ItemTypeEnum.address.getCode());

        for (DnsmasqDO dnsmasqDO : listSystemItems)
            result += buildItem(dnsmasqDO) ;
        result += "\n";

        for (DnsmasqDO dnsmasqDO : listServerItems)
            result += buildItem(dnsmasqDO);
        result += "\n";

        for (DnsmasqDO dnsmasqDO : listAddressItems)
            result += buildItem(dnsmasqDO);
        result += "\n";

        return result;
    }


    private String buildItem(DnsmasqDO dnsmasqDO) {
        String itemLine = "";
        if (!StringUtils.isEmpty(dnsmasqDO.getContent()))
            itemLine += "# " + dnsmasqDO.getContent() + "\n";
        itemLine += dnsmasqDO.getItem() + "=" + dnsmasqDO.getItemValue() + "\n";
        return itemLine;
    }

    private String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return "# Created by cmdb on " + fastDateFormat.format(new Date()) + "\n\n";
    }


}
