/**
 * Created by liangjian on 2017/1/16.
 */

'use strict';

app.controller('zabbixserverCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.userType = staticModel.userType;

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

    //监控状态
    $scope.zabbixStatus = staticModel.zabbixStatus;

    //主机监控
    $scope.zabbixMonitor = staticModel.zabbixMonitor;

    $scope.refresh = function () {
        var url = "/zabbixserver/refresh";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "数据已更新！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

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

    $scope.queryName = "";
    $scope.queryIp = "";
    $scope.nowType = 0;
    $scope.nowStatus = -1;
    $scope.nowMonitor = -1;
    $scope.nowEnv = -1;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryIp = "";
        $scope.nowType = 0;
        $scope.nowStatus = -1;
        $scope.nowMonitor = -1;
        $scope.nowEnv = -1;
        $scope.nowServerGroup = {};
        $scope.serverGroupList = [];
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
        var url = "/zabbixserver/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + $scope.queryName
            + "&useType=" + $scope.nowType
            + "&envType=" + $scope.nowEnv
            + "&queryIp=" + $scope.queryIp
            + "&zabbixStatus=" + $scope.nowStatus
            + "&zabbixMonitor=" + $scope.nowMonitor
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

    /**
     * 添加监控
     * @param item
     */
    $scope.addMonitor = function (item) {
        var url = "/zabbixserver/addMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "添加主机监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 删除监控
     * @param item
     */
    $scope.delMonitor = function (item) {
        var url = "/zabbixserver/delMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除主机监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 启用监控
     * @param item
     */
    $scope.enableMonitor = function (item) {
        var url = "/zabbixserver/enableMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "启用监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 禁用监控
     * @param item
     */
    $scope.disableMonitor = function (item) {
        var url = "/zabbixserver/disableMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "禁用监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }
    
    ///////////////////////////////////////////////////////////

    $scope.addServer = function () {
        var serverItem = {
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

        saveItem(serverItem);
    }

    $scope.editServer = function (item) {
        var serverItem = {
            id: item.id,
            serverGroupDO: item.serverGroupDO,
            serverName: item.serverName,
            serverType: item.serverType,
            loginType: item.loginType,
            loginUser: item.loginUser,
            envType: item.envType,
            area: item.area,
            publicIP: item.publicIP,
            insideIP: item.insideIP,
            serialNumber: item.serialNumber,
            ciGroup: item.ciGroup,
            content: item.content
        }

        saveItem(serverItem);
    }

    var saveItem = function (serverItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'serverInfo',
            controller: 'serverInstanceCtrl',
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

    /**
     * 删除server 信息
     * @param serverId
     */
    $scope.delServer = function (item) {
        var url = "/server/del?serverId=" + item.id;

        httpService.doDelete(url).then(function (data) {
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
});

app.controller('zabbixuserCtrl', function($scope, $state, $uibModal, toaster, httpService) {
    $scope.username = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function() {
        init();
    };

    /////////////////////////////////////////////////

    var init = function() {
        var url = "/users?"
            + "username=" + $scope.username
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    init();

    $scope.doQuery = function() {
        init();
    }

    /////////////////////////////////////////////////////////////

    $scope.syncZabbixUser = function() {
        var url = "/zabbixserver/user/sync";

        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "同步账户成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.createAllUserGroupConfigFile = function () {
        var url = "/zabbixserver/user/check";

        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "校验账户成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////////////////

    $scope.addZabbixAuth = function(username) {
        var url = "/zabbixserver/user/auth/add?username=" + username;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.delZabbixAuth = function(username) {
        var url = "/zabbixserver/user/auth/del?username=" + username;

        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////////

    $scope.serverList = function(username) {
        var modalInstance = $uibModal.open({
            templateUrl: 'keyBoxServerModal',
            controller: 'keyBoxServerInstanceCtrl',
            size : 'lg',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                username : function() {
                    return username;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function() {
            $scope.doQuery();
        });
    }
});