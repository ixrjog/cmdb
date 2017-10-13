'use strict';

// 用户
app.controller('keyboxManageUserCtrl', function($scope, $state, $uibModal, toaster, httpService) {
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

    $scope.createGlobalFile = function() {
        var url = "/box/user/group/global/create";

        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "创建成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
    
    $scope.createAllUserGroupConfigFile = function () {
        var url = "/box/user/group/createAll";

        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "创建成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////////////////

    $scope.addAuth = function(username) {
        var url = "/box/auth/add?username=" + username;

        httpService.doPost(url).then(function(data) {
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

    $scope.delAuth = function(username) {
        var url = "/box/auth/del?username=" + username;

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

    ///////////////////////////////////////////////////////////
    
    $scope.addUser = function () {
        var userItem = {
            id: 0,
            username: "",
            pwd: "",
            mail: "",
            mobile: "",
            displayName: "",
        }

        saveItem(userItem);
    }

    var saveItem = function (userItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'userInfo',
            controller: 'userInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                userItem: function () {
                    return userItem;
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

app.controller('userInstanceCtrl', function ($scope, $uibModalInstance, httpService, userItem) {
    
    $scope.userItem = userItem;

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
    
    /**
     * 保存user信息
     */
    $scope.saveUser = function () {
        var url = "/box/user/save";
        
        httpService.doPostWithJSON(url, $scope.userItem).then(function (data) {
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
});


// 用户-服务器组管理
app.controller('keyBoxServerInstanceCtrl', function ($scope, $uibModalInstance, httpService, username) {
    $scope.username = username ;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.queryServerGroup = function(queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.alert = {
        type : "",
        msg : ""
    };

    $scope.closeAlert = function() {
        $scope.alert = {
            type : "",
            msg : ""
        };
    }

    //////////////////////////////////////////////////////

    $scope.addItem = function(choose) {
        if($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择服务器组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/box/user/group/save";
        
        var requestBody = {
            username : username,
            serverGroupId : $scope.nowServerGroup.selected.id,
            ciChoose : choose
        }
        httpService.doPostWithJSON(url, requestBody).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.delItem = function(groupId) {
        var url = "/box/user/group/del?"
            + "groupId=" + groupId
            + "&username=" + username;

        httpService.doDelete(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        init();
    };

    /////////////////////////////////////////////////

    var init = function() {
        var url = "/box/user/group?"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        var queryItem = {
            username : username
        }
        httpService.doPostWithJSON(url, queryItem).then(function(data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    init();

    $scope.createFile = function() {
        var url = "/box/user/group/create?username=" + username;

        httpService.doPost(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "创建成功!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }
});

// 服务器组
app.controller('keyboxManageServerGroupCtrl', function($scope, $state, $uibModal, toaster, httpService) {
    $scope.queryName= "";

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
        var url = "/servergroup/keybox/page?"
            + "name=" + $scope.queryName
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

    $scope.checkUser = function() {
        var url = "/box/checkUser";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "校验用户成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }


    /////////////////////////////////////////////////////////////
    $scope.userList = function(id) {
        var modalInstance = $uibModal.open({
            templateUrl: 'keyBoxUserModal',
            controller: 'keyBoxUserInstanceCtrl',
            size : 'lg',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                serverGroupId : function() {
                    return id;
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
// 服务器组-用户管理
app.controller('keyBoxUserInstanceCtrl', function ($scope, $uibModalInstance, httpService, serverGroupId) {
    $scope.nowUser = {};
    $scope.userList = [];
    $scope.serverGroupId = serverGroupId;

    $scope.queryUser = function(queryParam) {
          var url = "/users?username=" + queryParam + "&page=0&length=10";
        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.userList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.alert = {
        type : "",
        msg : ""
    };

    $scope.closeAlert = function() {
        $scope.alert = {
            type : "",
            msg : ""
        };
    }

    //////////////////////////////////////////////////////

    $scope.addItem = function() {
        if($scope.nowUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择用户才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/box/user/group/save";

        var requestBody = {
            username : $scope.nowUser.selected.username,
            serverGroupId : $scope.serverGroupId
        }
        httpService.doPostWithJSON(url, requestBody).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.delItem = function(username) {
        var url = "/box/user/group/del?"
            + "groupId=" + $scope.serverGroupId
            + "&username=" + username;

        httpService.doDelete(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        init();
    };

    /////////////////////////////////////////////////

    var init = function() {
        var url = "/box/group/user/query?"
            + "serverGroupId="+ $scope.serverGroupId
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        var queryItem = {
            serverGroupId : serverGroupId
        }
        httpService.doPostWithJSON(url, queryItem).then(function(data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    init();

    $scope.createFile = function() {
        var url = "/box/user/group/create?username=" + username;

        httpService.doPost(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "创建成功!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }
});

// 密钥管理
app.controller('keyboxManageKeyCtrl', function($scope, $state, $uibModal, toaster, httpService) {
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
        var url = "/box/key/query";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.keyItem = body;
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

    $scope.saveKey = function() {
        var requestBody = {
            id :  $scope.keyItem.id,
            privateKey : $scope.keyItem.privateKey,
            publicKey : $scope.keyItem.publicKey,
            originalPrivateKey : $scope.keyItem.originalPrivateKey
        }

        var url = "/box/key/save";

        httpService.doPostWithJSON(url, requestBody).then(function(data) {
            if(data.success) {
                toaster.pop("success", "更新成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});



