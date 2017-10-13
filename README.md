Welcome to the cmdb wiki!
### 开发者
* DevOps 白衣 
* Dev    飞雪

### 版本环境
* Centos 6.x
* JDK 8
* Tomcat 8
* Gradel 3
* Mysql5.6 或 aliyunRDS
* Redis 3.0.3
* LDAP : apacheDS(http://directory.apache.org)
* Zookeeper(启动服务即可)
* Zabbix3.0.3(API兼容即可)
* VCSA6(API兼容即可)
* Mail
* Ansible


### 服务器管理
* 服务器管理
* 阿里云ECS主机管理(自动获取ECS主机信息）
* 阿里云模版管理(自动创建ECS主机&项目扩容)
* VCSA(vCenter Server Virtual Appliance)主机管理（自动获取vm主机信息）
* 服务器属性管理／服务器组属性管理

### 监控管理
* 托管zabbix服务器，通过zabbix api控制
1. 一键添加主机监控（通过服务器表数据）
2. 自动添加主机组
3. 自动添加用户（sms/email告警配置）及用户组
4. 自动配置动作（Action）
5. 自动配置tomcat(jmx)监控

* 服务器监控仪表盘

### 任务管理
* 日志弹性清理（动态清理服务器日志，日志越小保留天数越长；基于zabbix监控数据和ansible）
* 服务器常用任务（服务器初始化等,通过ansible+shell实现）

### 统计
* 部署统计(提供ci部署接口)，记录部署操作，记录每个服务器的版本信息
* 部署统计消息推送（钉钉）
* 服务器统计（月报表），汇集每月新开通的ecs/vm
* 服务器性能统计，可按组统计当前服务器性能数据

### IP管理
* IP段&IP管理

### 配置管理
* shadowsocks用户配置管理，生成用户的配置文件，给每位员工提供国外代理服务；
* terminal堡垒机配置管理（内部功能）
* tomcat安装资源管理（内部功能）
* 持续集成接口管理（内部功能）
* ansible主机文件管理（自动分组）
* nginx配置管理，自动生成upstream/location配置（内部功能）
* 服务器白名单功能，自动生成iptables文件（内部功能）

### 工单管理
* 工单配置
* 日常工单

### 跳板机（不支持操作审计）
* web版跳板机（支持多服务器同时操作）
* terminal跳板机

### 日志服务
* Nginx日志查询(基于阿里云日志服务)
* 业务日志查询(基于阿里云日志服务)

### 订阅审核
* sql静态审核扫描xml配置，发现问题并预警（内部功能）

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E9%A6%96%E9%A1%B5-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E9%A6%96%E9%A1%B5-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E9%A6%96%E9%A1%B5-3.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E9%A6%96%E9%A1%B5-4.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E6%9C%8D%E5%8A%A1%E5%99%A8-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-%E6%9C%8D%E5%8A%A1%E5%99%A8-2.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-ECS-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-ECS-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/cmdb-ECS-3.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-VM-1.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-PS-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-PS-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-PS-3.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-3.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-CONFIGFILE-4.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-KEYBOX-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-KEYBOX-2.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGCLEAN-1.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGSERVICE-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGSERVICE-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-LOGSERVICE-3.jpeg)





![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-STATUS-CI.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-STATUS-PERF.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-TODO-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-TODO-2.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-TODO-3.jpeg)


![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-USER-1.jpeg)
![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-USER-2.jpeg)

![sec](http://cmdbstore.oss-cn-hangzhou.aliyuncs.com/github/CMDB-ZABBIX-1.jpeg)





