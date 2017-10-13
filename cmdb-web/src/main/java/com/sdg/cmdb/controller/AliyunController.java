package com.sdg.cmdb.controller;

import com.sdg.cmdb.dao.AliyunDao;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.service.AliyunService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/6/12.
 */
@Controller
@RequestMapping("/aliyun")
public class AliyunController {

    @Resource
    private AliyunService aliyunService;

    /**
     * 获取EcsImage列表
     *
     * @return
     */
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryImage(@RequestParam String queryName) {
        List<AliyunEcsImageVO> listVO = aliyunService.queryAliyunImage(queryName);
        return new HttpResult(listVO);
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    @RequestMapping(value = "/network", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryNetwork() {
        List<AliyunNetworkDO> list = aliyunService.getAliyunNetwork();
        return new HttpResult(list);
    }

    /**
     * 获取vpc列表
     *
     * @return
     */
    @RequestMapping(value = "/vpc", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVpc(@RequestParam String networkType,@RequestParam String queryName) {
        List<AliyunVpcVO> list = aliyunService.queryAliyunVpc(networkType,queryName);
        return new HttpResult(list);
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    @RequestMapping(value = "/vswitch", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVswitch(@RequestParam long vpcId, @RequestParam String queryName) {
        List<AliyunVswitchVO> list = aliyunService.queryAliyunVswitch(vpcId, queryName);
        return new HttpResult(list);
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    @RequestMapping(value = "/securityGroup", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVpcSecurityGroup(@RequestParam long vpcId, @RequestParam String queryName) {
        List<AliyunVpcSecurityGroupVO> list = aliyunService.querySecurityGroup(vpcId, queryName);
        return new HttpResult(list);
    }

}
