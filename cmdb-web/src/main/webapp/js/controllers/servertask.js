'use strict';

app.controller('servertaskCtrl', function ($scope, $state, $uibModal, $localStorage, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.userType = staticModel.userType;

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

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
    $scope.nowEnv = -1;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryIp = "";
        $scope.nowType = 0;
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
        var url = "/server/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + $scope.queryName
            + "&useType=" + $scope.nowType
            + "&envType=" + $scope.nowEnv
            + "&queryIp=" + $scope.queryIp
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
     * 初始化服务器
     * @param serverId
     */
    $scope.initializationSystem = function (item) {
        var url = "/task/servertask/initializationSystem?serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功" ,data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    //////////////////////////////////////////////////////////////////////////

    /**
     * 更新表格行列
     * @param idx
     * @param item
     */
    var rowsUpdate = function(idx, item, serverList) {
        if($scope.nowDetailIdx == -1) {
            $scope.nowDetailIdx = idx + 1;
        } else if ($scope.nowDetailIdx == (idx + 1)) {
            return;
        } else {
            $scope.pageData.splice($scope.nowDetailIdx, 1);

            if($scope.nowDetailIdx <= idx) {
                $scope.nowDetailIdx = idx == 0 ? 1 : idx;
            } else {
                $scope.nowDetailIdx = idx == 0 ? 1 : (idx + 1);
            }
        }

        var detail = {
            detail : true,
            serverGroup : item,
            serverList : serverList
        }
        $scope.pageData.splice($scope.nowDetailIdx, 0, detail);
    }

    $scope.nowDetailIdx = -1;

    $scope.lookDetail = function(idx, item) {
        rowsUpdate(idx, item);

        initServer();
        login(item.id);
    }

    //////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////// ws管道 /////////////////////////////////////
    var server = null;

    var login = function(body) {
        var subContext = {
            id : $localStorage.settings.user.token,
            token : $localStorage.settings.user.token,
            requestType : "taskChain",
            body : body
        };

        server.send(JSON.stringify(subContext));
    }

    /**
     * 初始化server ws
     */
    var initServer = function() {
        if (server != null) {
            return;
        }

        server = httpService.wsInstance();

        server.onMessage(function(data) {
            //回调数据渲染前端
        });

        server.onError(function(data) {
            toaster.pop("error", "WS异常连接关闭");
            server = null;
        });

        server.onClose(function(data) {
            toaster.pop("warning", "WS连接关闭");
            server = null;
        });
    }
    ///////////////////////////////////// ws管道 /////////////////////////////////////
});


