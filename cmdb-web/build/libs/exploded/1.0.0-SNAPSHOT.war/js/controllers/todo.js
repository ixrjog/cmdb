/**
 * Created by liangjian on 2017/9/5.
 */

'use strict';

app.controller('todoCtrl', function ($scope, $uibModal, $state, $sce, $interval, $timeout, toaster, httpService) {
        $scope.authPoint = $state.current.data.authPoint;
        $scope.todoGroupList = [];
        $scope.todoDetailList = [];
        $scope.todoDetailCompleteList = [];
        $scope.myJobStatusOpen = true;

        $scope.queryMyJob = function () {
            var url = "/todo/queryMyJob";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.todoDetailList = data.body;
                    $scope.refreshInitiatorUserInfo();
                }
            });
        }

        $scope.queryCompleteJob = function () {
            var url = "/todo/queryCompleteJob";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.todoDetailCompleteList = data.body;
                    $scope.refreshCompleteInitiatorUserInfo();
                    $scope.refreshCompleteAssigneeUserInfo();
                }
            });
        }

        // 生成发起人信息
        $scope.refreshCompleteInitiatorUserInfo = function () {

            if ($scope.todoDetailCompleteList.length == 0) return;

            for (var i = 0; i < $scope.todoDetailCompleteList.length; i++) {
                var info = '<b style="color: #286090">申请人</b>';
                var item = $scope.todoDetailCompleteList[i].initiatorUserDO;
                // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
                var mobile = "";
                if (item.mobile != null && item.mobile != '') {
                    mobile = "<br/>" + item.mobile;
                }
                info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                    '<b style="color: #777"><' + item.displayName + "></b><br/>"
                    + '<b style="color: #286090">' + item.mail + "</b>"
                    + mobile
                item.initiatorUserInfo = $sce.trustAsHtml(
                    info
                );
            }
        }

        // 生成负责人信息
        $scope.refreshCompleteAssigneeUserInfo = function () {

            if ($scope.todoDetailCompleteList.length == 0) return;

            for (var i = 0; i < $scope.todoDetailCompleteList.length; i++) {
                var info = '<b style="color: #286090">负责人</b>';
                var item = $scope.todoDetailCompleteList[i].assigneeUserDO;
                // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
                var mobile = "";
                if (item.mobile != null && item.mobile != '') {
                    mobile = "<br/>" + item.mobile;
                }
                info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                    '<b style="color: #777"><' + item.displayName + "></b><br/>"
                    + '<b style="color: #286090">' + item.mail + "</b>"
                    + mobile
                item.assigneeUserInfo = $sce.trustAsHtml(
                    info
                );
            }
        }

        $scope.queryMyJob();
        $scope.queryCompleteJob();

        // 60秒刷新1次待办工单
        var timer1 = $interval(function () {
            $scope.queryMyJob();
        }, 30000);

        // 60秒刷新1次待办工单
        var timer2 = $interval(function () {
            $scope.queryCompleteJob();
        }, 60000);

        // 生成发起人信息
        $scope.refreshInitiatorUserInfo = function () {

            if ($scope.todoDetailList.length == 0) return;

            for (var i = 0; i < $scope.todoDetailList.length; i++) {
                var info = '<b style="color: #286090">申请人</b>';
                var item = $scope.todoDetailList[i].initiatorUserDO;
                // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

                var mobile = "";
                if (item.mobile != null && item.mobile != '') {
                    mobile = "<br/>" + item.mobile;
                }

                info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                    '<b style="color: #777"><' + item.displayName + "></b><br/>"
                    + '<b style="color: #286090">' + item.mail + "</b>"
                    + mobile

                item.initiatorUserInfo = $sce.trustAsHtml(
                    info
                );
            }

        }

        $scope.queryTodoGroup = function () {
            var url = "/todo/group/query";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var body = data.body;
                    $scope.todoGroupList = body;
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }

        $scope.queryTodoGroup();

        $scope.submitTodo = function (todoItem) {
            switch (todoItem.id) {
                case 1:
                    ////TODO 堡垒机权限申请
                    $scope.submitTodoKeyBox(todoItem);
                    break;
                case 2:
                    ////TODO 持续集成权限申请
                    $scope.submitTodoCiUserGroup(todoItem);
                    //
                    break;
                case 3:
                    ////TODO 平台权限申请
                    $scope.submitSystemAuth(todoItem);
                    //
                    break;
                default:
                //

            }
        }

        $scope.viewTodo = function (todoDetail) {
            switch (todoDetail.todoDO.id) {
                case 1:
                    ////TODO 堡垒机权限申请
                    $scope.viewTodoKeyBox(todoDetail);
                    break;
                case 2:
                    ////TODO 持续集成权限申请
                    $scope.viewTodoCiUserGroup(todoDetail);
                    break;
                case 3:
                    ////TODO 平台权限申请
                    $scope.viewSystemAuth(todoDetail);
                    break;
                default:
                //
            }
        }

        $scope.viewTodoKeyBox = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoKeyBoxModal',
                controller: 'todoKeyBoxInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });
        }

        $scope.viewTodoCiUserGroup = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoCiUserGroupModal',
                controller: 'todoCiUserGroupInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });
        }


        //// TODO 提交工单 品台权限申请
        $scope.viewSystemAuth = function (todoDetail) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoSystemAuthModal',
                controller: 'todoSystemAuthInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoDetail.todoDO;
                    },
                    todoDetail: function () {
                        return todoDetail;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //////////////////////////////////////////////////////

        //// TODO 提交工单 堡垒机权限申请
        $scope.submitTodoKeyBox = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoKeyBoxModal',
                controller: 'todoKeyBoxInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });
            // 关闭查询
            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 持续集成权限申请
        $scope.submitTodoCiUserGroup = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoCiUserGroupModal',
                controller: 'todoCiUserGroupInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //// TODO 提交工单 品台权限申请
        $scope.submitSystemAuth = function (todoItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'todoSystemAuthModal',
                controller: 'todoSystemAuthInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    todoItem: function () {
                        return todoItem;
                    },
                    todoDetail: function () {
                        return null;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.queryMyJob();
            }, function () {
                $scope.queryMyJob();
            });
        }

        //////////////////////////////////////////////////////

        // 撤销工单
        $scope.revokeTodoDetail = function (id) {
            var url = "/todo/revokeTodoDetail?id=" + id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "撤销成功!");
                    $scope.queryMyJob();
                } else {
                    toaster.pop("warning", "撤销失败!");
                }
            }, function (err) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = err;
            });
        }

        // 执行工单
        $scope.invokeTodoDetail = function (id) {
            var url = "/todo/invokeTodoDetail?id=" + id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "执行成功!");
                    $scope.queryMyJob();
                } else {
                    toaster.pop("warning", "执行失败!");
                }
            }, function (err) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = err;
            });
        }
    }
);

app.controller('todoKeyBoxInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    // 生成负责人信息
    $scope.refreshAssigneeUsersInfo = function () {

        var info = '<b style="color: #286090">负责人</b>';

        switch ($scope.todoItem.todoType) {
            case (0) :
                info += '<b class="pull-right" style="color: #777">DevOps</b>'
                break;
            case (1) :
                info += '<b class="pull-right" style="color: #777">DBA</b>'
                break;
            default:
        }


        for (var i = 0; i < $scope.todoDetail.assigneeUsers.length; i++) {
            var item = $scope.todoDetail.assigneeUsers[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

            var mobile = "";
            if (item.mobile != null && item.mobile != '') {
                mobile = "<br/>" + item.mobile;
            }

            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                '<b style="color: #777"><' + item.displayName + "></b><br/>"
                + '<b style="color: #286090">' + item.mail + "</b>"
                + mobile

        }

        $scope.assigneeUsersInfo = $sce.trustAsHtml(
            info
        );
    }

    var init = function () {
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 创建工单（复用）
    init();

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

    //////////////////////////////////////////////////////

    $scope.addItem = function (choose) {
        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择服务器组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/todo/todoDetail/addTodoKeybox";

        var requestBody = {
            todoDetailId: $scope.todoDetail.id,
            serverGroupId: $scope.nowServerGroup.selected.id,
            ciAuth: choose
        }
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.delItem = function (id) {
        var url = "/todo/todoDetail/delTodoKeybox?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.submitTodo = function () {

        if ($scope.todoDetail.todoKeyboxDetailList == null || $scope.todoDetail.todoKeyboxDetailList.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未指定服务器组!";
            return;
        }

        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/todo/todoDetail/query?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

});


app.controller('todoCiUserGroupInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];

    $scope.doQuery = function () {
        var url = "/todo/todoDetail/query?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    // 生成负责人信息
    $scope.refreshAssigneeUsersInfo = function () {

        var info = '<b style="color: #286090">负责人</b>';

        switch ($scope.todoItem.todoType) {
            case (0) :
                info += '<b class="pull-right" style="color: #777">DevOps</b>'
                break;
            case (1) :
                info += '<b class="pull-right" style="color: #777">DBA</b>'
                break;
            default:
        }


        for (var i = 0; i < $scope.todoDetail.assigneeUsers.length; i++) {
            var item = $scope.todoDetail.assigneeUsers[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

            var mobile = "";
            if (item.mobile != null && item.mobile != '') {
                mobile = "<br/>" + item.mobile;
            }

            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                '<b style="color: #777"><' + item.displayName + "></b><br/>"
                + '<b style="color: #286090">' + item.mail + "</b>"
                + mobile

        }

        $scope.assigneeUsersInfo = $sce.trustAsHtml(
            info
        );
    }

    var init = function () {
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 创建工单（复用）
    init();

    $scope.queryCiGroup = function (queryParam) {
        var url = "/ci/usergroup/page?page=0&length=10&envType=-1&groupName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.cigroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

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

    //////////////////////////////////////////////////////
    $scope.addItem = function () {
        if ($scope.nowCigroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择权限组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/todo/todoDetail/addTodoCiUserGroup";

        var requestBody = {
            todoDetailId: $scope.todoDetail.id,
            ciUserGroupId: $scope.nowCigroup.selected.id
        }
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.delItem = function (id) {
        var url = "/todo/todoDetail/delTodoCiUserGroup?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.submitTodo = function () {

        if ($scope.todoDetail.todoCiUserGroupDetailList == null || $scope.todoDetail.todoCiUserGroupDetailList.length == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未指定持续集成权限组!";
            return;
        }

        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

});


app.controller('todoSystemAuthInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, todoItem, todoDetail) {
    $scope.todoItem = todoItem;
    $scope.todoDetail = todoDetail;
    //$scope.todoDetail = {};
    $scope.initiatorUsername = "";
    $scope.assigneeUsersInfo = "";
    $scope.nowCigroup = {};
    $scope.cigroupList = [];

    $scope.doQuery = function () {
        var url = "/todo/todoDetail/query?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    // 生成负责人信息
    $scope.refreshAssigneeUsersInfo = function () {

        var info = '<b style="color: #286090">负责人</b>';

        switch ($scope.todoItem.todoType) {
            case (0) :
                info += '<b class="pull-right" style="color: #777">DevOps</b>'
                break;
            case (1) :
                info += '<b class="pull-right" style="color: #777">DBA</b>'
                break;
            default:
        }


        for (var i = 0; i < $scope.todoDetail.assigneeUsers.length; i++) {
            var item = $scope.todoDetail.assigneeUsers[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "

            var mobile = "";
            if (item.mobile != null && item.mobile != '') {
                mobile = "<br/>" + item.mobile;
            }

            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                '<b style="color: #777"><' + item.displayName + "></b><br/>"
                + '<b style="color: #286090">' + item.mail + "</b>"
                + mobile

        }

        $scope.assigneeUsersInfo = $sce.trustAsHtml(
            info
        );
    }

    var init = function () {
        if (todoDetail != null) {
            $scope.refreshAssigneeUsersInfo();
            return;
        }

        var url = "/todo/establish?todoId=" + $scope.todoItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.todoDetail = data.body;
                $scope.initiatorUsername = $scope.todoDetail.initiatorUsername;
                $scope.refreshAssigneeUsersInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 创建工单（复用）
    init();

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

    /////////////////////////////////////////////////

    $scope.setItem = function (id) {
        var url = "/todo/todoDetail/setTodoSystemAuth?id=" + id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "设置成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////

    $scope.submitTodo = function () {

        var check = false;

        if ($scope.todoDetail.todoSystemAuthGetway.need) {
            check = true;
        } else {
            for (var i = 0; i < $scope.todoDetail.todoSystemAuthDetailList.length; i++) {
                var item = $scope.todoDetail.todoSystemAuthDetailList[i];
                if (item.need == true) {
                    check = true;
                    break;
                }
            }
        }

        if (!check) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "工单未配置!";
            return;
        }


        var url = "/todo/todoDetail/submit?id=" + $scope.todoDetail.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "提交成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

});
