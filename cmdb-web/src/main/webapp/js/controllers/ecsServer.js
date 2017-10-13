/**
 * Created by liangjian on 2017/8/29.
 */
'use strict';

app.controller('ecsServerCtrl', function ($scope, $state, $uibModal, $sce, httpService, toaster, staticModel) {

    $scope.userType = staticModel.userType;

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

    $scope.authPoint = $state.current.data.authPoint;

    //server表关联
    $scope.ecsStatus = staticModel.serverStatus;

    //ECS服务器计费类型
    $scope.ecsServerInternetChargeType = staticModel.ecsServerInternetChargeType;

    //ECS服务器是否io优化实例
    $scope.ecsServerIoOptimized = staticModel.ecsServerIoOptimized;

    $scope.ecsStatistics = function () {
        var url = "/server/ecsStatistics";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.ecsData = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.ecsStatistics();

    $scope.ecsRefresh = function () {
        var url = "/server/ecsRefresh";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "列表已更新！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.allocateIp = function (item) {
        var url = "/server/ecsAllocateIp?instanceId=" + item.instanceId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "列表已更新！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.ecsCheck = function () {
        var url = "/server/ecsCheck";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "校验完毕！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.ecsDump = function () {
        var url = "/server/ecsDump";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", data.msg);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    /**
     * 标记ecs状态为删除
     * @param serverId
     */
    $scope.setDelEcs = function (item) {
        var url = "/server/setStatus?insideIp=" + item.insideIp;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 标记ecs状态为删除
     * @param serverId
     */
    $scope.delEcs = function (item) {
        var url = "/server/delEcs?insideIp=" + item.insideIp;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }


    $scope.queryName = "";
    $scope.queryIp = "";
    //关联
    $scope.nowStatus = -1;
    $scope.nowPublicGroup = [];
    $scope.nowInternalGroup = [];

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryIp = "";
        //关联
        $scope.nowAssociate = 1;
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/server/ecsPage?"
            + "&serverName=" + $scope.queryName
            + "&queryIp=" + $scope.queryIp
            + "&status=" + ($scope.nowStatus == null ? -1 : $scope.nowStatus)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                $scope.refreshInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    $scope.refreshInfo = function () {
        for (var i = 0; i < $scope.pageData.length; i++) {
            var item = $scope.pageData[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
            var ioOptimized = "";
            if (item.ioOptimized) {
                ioOptimized = "<b style='color: green'>(I/O优化)</b><br/>";
            }

            var network = "";
            network = "网络类型:  " + item.networkTypePropertyDO.propertyValue + "<br/>";
            network += "安全组:  " + item.securityGroupPropertyDO.propertyValue + "<br/>";
            network += "镜像:  " + item.imagePropertyDO.propertyValue + "<br/>";
            if (item.networkTypePropertyDO.propertyValue == 'vpc') {
                network += ">VPC:  " + item.vpcPropertyDO.propertyValue + "<br/>";
                network += ">VSW:  " + item.vswitchPropertyDO.propertyValue + "<br/>";
            }

            var memory = item.memory / 1024;
            if (memory >= 8) {
                memory = "<b style='color: green'>" + memory + "GB</b><br/>";
            }

            var disk = "";
            disk = "系统盘:" + item.systemDiskSize + "GB";
            switch (item.systemDiskCategory) {
                case 'cloud':
                    disk += "(普通云盘)<br/>";
                    break;
                case 'cloud_efficiency':
                    disk += "<b style='color: green'>(高效云盘)</b><br/>";
                    2
                    break;
                case 'cloud_ssd':
                    disk += "<b style='color: #d99a53'>(SSD云盘)</b><br/>";
                    break;
                case 'ephemeral_ssd':
                    disk += "<b style='color: #d9534f'>(本地SSD盘)</b><br/>";
                    break;
                default:
            }

            disk += "数据盘:" + item.dataDiskSize + "GB";
            switch (item.dataDiskCategory) {
                case 'cloud':
                    disk += "(普通云盘)<br/>";
                    break;
                case 'cloud_efficiency':
                    disk += "<b style='color: green'>(高效云盘)</b><br/>";
                    2
                    break;
                case 'cloud_ssd':
                    disk += "<b style='color: #d99a53'>(SSD云盘)</b><br/>";
                    break;
                case 'ephemeral_ssd':
                    disk += "<b style='color: #d9534f'>(本地SSD盘)</b><br/>";
                    break;
                default:
            }

            item.info = $sce.trustAsHtml(
                "CPU:  " + item.cpu + "核<br/>"
                + "内存:   " + memory
                + "带宽:    " + item.internetMaxBandwidthOut + "Mbps<br/>"
                + ioOptimized
                + network
                + disk
            );
        }
    }

    ///////////////////////////////////////////////////////////

    $scope.addServer = function (item) {

        //$scope.queryInternalIPGroup();

        //$scope.queryPublicIPGroup();

        var serverItem = {
            id: 0,
            serverGroupDO: "",
            serverName: item.serverName,
            serverType: 2,
            loginType: 0,
            loginUser: "manage",
            envType: "",
            area: item.area,
            publicIP: "",
            insideIP: "",
            serialNumber: "",
            ciGroup: "",
            content: ""
        }

        saveItem(serverItem, item);
    }

    var saveItem = function (serverItem, item) {

        serverItem.publicIP = {
            ipNetworkDO: $scope.nowPublicGroup.selected,
            ip: item.publicIp
        }

        serverItem.insideIP = {
            ipNetworkDO: $scope.nowInternalGroup.selected,
            ip: item.insideIp
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'serverInfo',
            controller: 'ecsServerInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                userType: function () {
                    return $scope.userType;
                },
                envType: function () {
                    return $scope.envType;
                },
                logType: function () {
                    return $scope.logType;
                },
                serverType: function () {
                    return $scope.serverType;
                },
                serverItem: function () {
                    return serverItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    //////////////////////////////
    $scope.publicGroupList = [];

    /**
     * 查询公网网段
     * @param ipNetwork
     */
    $scope.queryPublicIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 0);
    }

    $scope.internalGroupList = [];

    /**
     * 查询内网网段
     * @param ipNetwork
     */
    $scope.queryInternalIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 1);
    }

    var queryIPGroup = function (ipNetwork, ipType) {
        var url = "/ipgroup/query?" + "page=" + 0 + "&length=" + 10;

        var queryBody = {
            ipNetwork: ipNetwork,
            serverGroupId: 0,
            ipType: ipType
        }
        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                if (ipType == 0) {   //公网
                    $scope.publicGroupList = body.data;
                    $scope.nowPublicGroup = {
                        selected: $scope.publicGroupList[0]
                    };
                } else if (ipType == 1) {   //内网
                    $scope.internalGroupList = body.data;
                    $scope.nowInternalGroup = {
                        selected: $scope.internalGroupList[0]
                    };
                }
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

});

app.controller('ecsServerInstanceCtrl', function ($scope, $uibModalInstance, httpService, userType, envType, logType, serverType, serverItem) {
    $scope.userType = userType;
    $scope.envType = envType;
    $scope.logType = logType;
    $scope.serverType = serverType;

    $scope.serverItem = serverItem;

    $scope.alert = {
        type: "",
        msg: ""
    };

    $scope.closeAlert = function () {
        $scope.alert = {
            type: "",
            msg: ""
        };
    }

    $scope.insideip = "";
    $scope.publicip = "";

    /**
     * 初始化环境
     */
    var initEnv = function () {
        if ($scope.serverItem.serverGroupDO == null) {
            $scope.nowServerGroup = {};
        } else {
            $scope.nowServerGroup = {
                selected: $scope.serverItem.serverGroupDO
            };
        }

        if ($scope.serverItem.publicIP == null) {
            $scope.nowPublicGroup = {};
        } else {
            $scope.nowPublicGroup = {
                selected: $scope.serverItem.publicIP.ipNetworkDO
            };
        }

        if ($scope.serverItem.insideIP == null) {
            $scope.nowInternalGroup = {};
        } else {
            $scope.nowInternalGroup = {
                selected: $scope.serverItem.insideIP.ipNetworkDO
            };
        }

        $scope.insideip = $scope.serverItem.insideIP != null ? $scope.serverItem.insideIP.ip : "";
        $scope.publicip = $scope.serverItem.publicIP != null ? $scope.serverItem.publicIP.ip : "";
    }

    initEnv();

    /**
     * 重置
     */
    $scope.resetServerItem = function () {
        $scope.serverItem = {
            id: 0,
            serverGroupDO: null,
            serverName: "",
            serverType: -1,
            loginType: -1,
            loginUser: "",
            envType: -1,
            area: "",
            publicIP: null,
            insideIP: null,
            serialNumber: "",
            ciGroup: "",
            content: ""
        }

        initEnv();
    }

    /**
     * 保存server item信息
     */
    $scope.saveServerItem = function (insideIP, publicIP) {
        var url = "/server/save";

        $scope.serverItem.serverGroupDO = $scope.nowServerGroup.selected;
        if ($scope.nowPublicGroup.selected != null) {
            $scope.serverItem.publicIP = {
                ipNetworkDO: $scope.nowPublicGroup.selected,
                ip: publicIP
            };
        }
        if ($scope.nowInternalGroup.selected != null) {
            $scope.serverItem.insideIP = {
                ipNetworkDO: $scope.nowInternalGroup.selected,
                ip: insideIP
            };
        }

        httpService.doPostWithJSON(url, $scope.serverItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    //////////////////////////////////////////////////////////

    $scope.serverGroupList = [];

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////

    $scope.publicGroupList = [];

    /**
     * 查询公网网段
     * @param ipNetwork
     */
    $scope.queryPublicIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 0);
    }

    $scope.internalGroupList = [];

    /**
     * 查询内网网段
     * @param ipNetwork
     */
    $scope.queryInternalIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 1);
    }

    var queryIPGroup = function (ipNetwork, ipType) {
        var url = "/ipgroup/query?" + "page=" + 0 + "&length=" + 10;

        var queryBody = {
            ipNetwork: ipNetwork,
            serverGroupId: 0,
            ipType: ipType
        }
        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                if (ipType == 0) {   //公网
                    $scope.publicGroupList = body.data;
                    $scope.nowPublicGroup = {
                        selected: $scope.publicGroupList[0]
                    };
                } else if (ipType == 1) {   //内网
                    $scope.internalGroupList = body.data;
                    $scope.nowInternalGroup = {
                        selected: $scope.internalGroupList[0]
                    };
                }
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.checkIPUse = function (groupId, ip) {
        if (ip == null || ip == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "IP未指定!";
            return;
        }

        var url = "/ip/use/check?"
            + "groupId=" + groupId
            + "&ip=" + ip
            + "&serverId=" + $scope.serverItem.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = ip + "未被其它服务器使用!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }
});

app.controller('ecsTemplateCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {

    $scope.zoneIds = staticModel.zoneIds;


    //服务器类型
    $scope.serverType = staticModel.serverType;

    $scope.authPoint = $state.current.data.authPoint;


    //ECS服务器计费类型
    $scope.ecsServerInternetChargeType = staticModel.ecsServerInternetChargeType;

    //ECS服务器是否io优化实例
    $scope.ecsServerIoOptimized = staticModel.ecsServerIoOptimized;

    $scope.allocatePublicIpAddress = staticModel.allocatePublicIpAddress;


    $scope.queryZoneId = "";

    $scope.reSet = function () {
        $scope.queryZoneId = "";
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/server/template/ecs/page?"
            + "&zoneId=" + $scope.queryZoneId
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    ///////////////////////////////////////////////////////////

    $scope.expansion = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'expansionEcsInstance',
            controller: 'expansionEcsInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                template: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    ///////////////////////////////////////////////////////////

    $scope.create = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'createEcsInstance',
            controller: 'createEcsInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                template: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }
});

app.controller('createEcsInstanceCtrl', function ($scope, $uibModalInstance, staticModel, httpService, template) {
    $scope.userType = staticModel.userType;
    $scope.envType = staticModel.envType;
    $scope.logType = staticModel.logType;
    $scope.serverType = staticModel.serverType;
    // $scope.networkConfig = staticModel.ecsExpansionNetworkConfig;
    $scope.allocatePublicIp = staticModel.allocatePublicIpAddress;

    $scope.templateConfig = 1;

    $scope.nowImage = {};
    $scope.nowNetwork = {};
    $scope.nowVpc = {};
    $scope.nowVswitch = {};
    $scope.nowSecurityGroup = {};
    $scope.nowServerGroup = {};
    $scope.template = template;

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    /**
     * 初始化环境
     */
    var initEnv = function () {
        var serverItem = {
            id: 0,
            serverGroupDO: null,
            serverName: "",
            serverType: 2,
            loginType: 0,
            loginUser: "manage",
            envType: 4,
            area: "",
            publicIP: null,
            insideIP: null,
            serialNumber: "",
            ciGroup: "",
            content: ""
        }

        var templateItem = {
            serverVO: serverItem,
            dataDiskSize: $scope.template.dataDiskSize,
            cnt: 1,
            ecsTemplateId: $scope.template.id,
            imageId: 0,
            networkType: "",
            vpcId: 0,
            vswitchId: 0,
            securityGroupId: 0
        }

        $scope.templateItem = templateItem;
    }

    initEnv();


    $scope.alert = {
        type: "",
        msg: ""
    };

    $scope.closeAlert = function () {
        $scope.alert = {
            type: "",
            msg: ""
        };
    }

    $scope.insideip = "";
    $scope.publicip = "";


    /**
     * 扩容
     */
    $scope.createServer = function () {
        var url = "/server/template/ecs/create";

        $scope.templateItem.serverVO.serverGroupDO = $scope.nowServerGroup.selected;
        if ($scope.nowPublicGroup.selected != null) {
            $scope.templateItem.serverVO.publicIP = {
                ipNetworkDO: $scope.nowPublicGroup.selected,
                ip: "255.255.255.255"
            }
        }
        if ($scope.nowInternalGroup.selected != null) {
            $scope.templateItem.serverVO.insideIP = {
                ipNetworkDO: $scope.nowInternalGroup.selected,
                ip: "255.255.255.255"
            }
        }

        if ($scope.nowImage.selected != null) {
            $scope.templateItem.imageId = $scope.nowImage.selected.id
        }


        // 设置网络属性
        if ($scope.nowNetwork.selected != null) {
            $scope.templateItem.networkType = $scope.nowNetwork.selected
            if ($scope.templateItem.networkType == "vpc") {
                // vpc
                if ($scope.nowVpc.selected != null) {
                    $scope.templateItem.vpcId = $scope.nowVpc.selected.id
                }
                // vswitch
                if ($scope.nowVswitch.selected != null) {
                    $scope.templateItem.vswitchId = $scope.nowVswitch.selected.id
                }
                // securityGroup
                if ($scope.nowSecurityGroup.selected != null) {
                    $scope.templateItem.securityGroupId = $scope.nowSecurityGroup.selected.id
                }
            }
        }


        httpService.doPostWithJSON(url, $scope.templateItem).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;

                $scope.templateConfig = 0
            }
            else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////

    $scope.imageList = [];

    $scope.queryImage = function (queryParam) {
        var url = "/aliyun/image?queryName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.imageList = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.networkList = [];

    $scope.queryNetwork = function () {
        var url = "/aliyun/network";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.networkList = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryNetwork();

    //////////////////////////////////////////////////////////

    $scope.vpcList = [];

    $scope.queryVpc = function (queryParam) {
        var url = "/aliyun/vpc?"
            + "networkType=" + ($scope.nowNetwork.selected == null ? "" : $scope.nowNetwork.selected)
            + "&queryName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.vpcList = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    /**
     * 触发联动
     */
    $scope.changeVpc = function () {
        $scope.queryVswitch("");
        $scope.querySecurityGroup("");
    }

    //////////////////////////////////////////////////////////

    $scope.vswitchList = [];

    $scope.queryVswitch = function (queryParam) {
        var url = "/aliyun/vswitch?"
            + "vpcId=" + ($scope.nowVpc.selected == null ? -1 : $scope.nowVpc.selected.id)
            + "&queryName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.vswitchList = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////
    $scope.securityGroupList = [];

    $scope.querySecurityGroup = function (queryParam) {
        var url = "/aliyun/securityGroup?"
            + "vpcId=" + ($scope.nowVpc.selected == null ? -1 : $scope.nowVpc.selected.id)
            + "&queryName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.securityGroupList = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }
    //////////////////////////////////////////////////////////

    $scope.serverGroupList = [];

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////

    $scope.publicGroupList = [];

    /**
     * 查询公网网段
     * @param ipNetwork
     */
    $scope.queryPublicIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 0);
    }

    $scope.internalGroupList = [];

    /**
     * 查询内网网段
     * @param ipNetwork
     */
    $scope.queryInternalIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 1);
    }

    var queryIPGroup = function (ipNetwork, ipType) {
        var url = "/ipgroup/query?" + "page=" + 0 + "&length=" + 10;

        var queryBody = {
            ipNetwork: ipNetwork,
            serverGroupId: 0,
            ipType: ipType
        }
        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                if (ipType == 0) {   //公网
                    $scope.publicGroupList = body.data;
                    $scope.nowPublicGroup = {
                        selected: $scope.publicGroupList[0]
                    };
                } else if (ipType == 1) {   //内网
                    $scope.internalGroupList = body.data;
                    $scope.nowInternalGroup = {
                        selected: $scope.internalGroupList[0]
                    };
                }
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.checkIPUse = function (groupId, ip) {
        if (ip == null || ip == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "IP未指定!";
            return;
        }

        var url = "/ip/use/check?"
            + "groupId=" + groupId
            + "&ip=" + ip
            + "&serverId=" + $scope.serverItem.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = ip + "未被其它服务器使用!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }
});