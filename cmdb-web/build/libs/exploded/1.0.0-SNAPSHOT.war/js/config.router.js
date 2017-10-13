'use strict';


/**
 * Config for the router
 */
angular.module('app')
    .run(
        ['$rootScope', '$state', '$stateParams', 'httpService', '$localStorage',
            function ($rootScope, $state, $stateParams, httpService, $localStorage) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;

                $rootScope.$on('$stateChangeStart', function (event, toState) {
                        if (toState.name != 'access.signin' && ($localStorage.settings.user == null || $localStorage.settings.user.token == null)) {
                            $state.transitionTo("access.signin");
                            event.preventDefault();
                        } else if (toState.permission) {
                            var checkResult = httpService.doAuthCheck(toState.name, toState.authGroup == null ? "" : JSON.stringify(toState.authGroup));
                            var status = checkResult.status;
                            var body = checkResult.body;

                            if (!status) {
                                if (status.code == '0002' || status.code == '0003') {
                                    $state.transitionTo("access.signin");
                                } else {
                                    $state.transitionTo("app.noAuth");
                                }

                                event.preventDefault();
                            } else {
                                if (toState.data != null && toState.data != undefined) {
                                    toState.data.authPoint = body;
                                }
                            }
                        } else if (toState.name != 'app.noAuth' && (toState.permission == undefined || toState.permission == null || !toState.permission)) {
                            //不处理
                        } else {
                            if (toState.name == 'app.noAuth') {
                                var checkResult = httpService.doAuthCheck("app.dashboard");
                                var status = checkResult.status;

                                if (status) {
                                    $state.transitionTo("app.dashboard");
                                } else {
                                    if (checkResult.code == '0002' || checkResult.code == '0003' || checkResult.code == '0004') {
                                        $state.transitionTo("access.signin");
                                    }
                                }
                                event.preventDefault();
                            }
                        }
                    }
                );
            }
        ]
    )
    .config(
        ['$stateProvider', '$urlRouterProvider',
            function ($stateProvider, $urlRouterProvider) {

                $urlRouterProvider
                    .otherwise('/app/dashboard');
                $stateProvider
                    .state('app', {
                        abstract: true,
                        url: '/app',
                        templateUrl: 'tpl/app.html'
                    })
                    .state('app.404', {
                        url: '/404',
                        templateUrl: 'tpl/page_404.html',
                        permission: false
                    })
                    .state('app.noAuth', {
                        url: '/noAuth',
                        templateUrl: 'tpl/app_noAuth.html',
                        permission: false
                    })


                    .state('app.dashboard', {
                        url: '/dashboard',
                        templateUrl: 'tpl/app_dashboard.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/dashboard.js',
                                                'vendor/libs/highchart/highcharts.js'
                                            ]);
                                        }
                                    )
                                        .then(function () {
                                            return $ocLazyLoad.load([
                                                'vendor/libs/highchart/highcharts-more.js'
                                            ]);
                                        })
                                        .then(function () {
                                            return $ocLazyLoad.load([
                                                'vendor/libs/highchart/modules/solid-gauge.js'
                                            ]);
                                        })
                                        ;
                                }]
                        }
                    })

                    .state('app.servergroup', {
                        url: '/servergroup',
                        templateUrl: 'tpl/app_servergroup.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/servergroup.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["servergroup", "server", "property"],
                        data: {
                            authPoint: {}
                        }
                    })
                    .state('app.server', {
                        url: '/server',
                        templateUrl: 'tpl/app_server.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/server.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["server", "property"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.ecsServer', {
                        url: '/ecsServer',
                        templateUrl: 'tpl/app_ecs_server.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/ecsServer.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["server", "ecsTemplate"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.vmServer', {
                        url: '/vmServer',
                        templateUrl: 'tpl/app_vm_server.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/vmServer.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["server"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.physicalServer', {
                        url: '/physicalServer',
                        templateUrl: 'tpl/app_physical_server.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/physicalServer.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["server", "property"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.property', {
                        url: '/property',
                        templateUrl: 'tpl/app_property.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/property.js']);
                                        }
                                    );
                                }]
                        }
                    })
                    .state('app.zabbixserver', {
                        url: '/zabbixserver',
                        templateUrl: 'tpl/app_zabbixserver.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/zabbixserver.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["zabbixserver", "server", "servergroup"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.servermonitor', {
                        url: '/servermonitor',
                        templateUrl: 'tpl/app_servermonitor.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/servermonitor.js',
                                                'vendor/libs/highchart/highcharts.js'
                                            ]);
                                        }
                                    )
                                        .then(function () {
                                            return $ocLazyLoad.load([
                                                'vendor/libs/highchart/highcharts-more.js'
                                            ]);
                                        })
                                        .then(function () {
                                            return $ocLazyLoad.load([
                                                'vendor/libs/highchart/modules/solid-gauge.js'
                                            ]);
                                        })
                                        ;
                                }]
                        },
                        authGroup: ["zabbixserver", "server", "servergroup"]
                    })

                    .state('app.logcleanup', {
                        url: '/logcleanup',
                        templateUrl: 'tpl/app_logcleanup.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/logcleanup.js'
                                            ]);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["task"],
                        data: {
                            authPoint: {}
                        }
                    })
                    .state('app.servertask', {
                        url: '/servertask',
                        templateUrl: 'tpl/app_servertask.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/servertask.js'
                                            ]);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["task"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.statistics', {
                        url: '/statistics',
                        templateUrl: 'tpl/app_statistics.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/statistics.js'
                                            ]);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["statistics"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.serverstatistics', {
                        url: '/serverstatistics',
                        templateUrl: 'tpl/app_serverstatistics.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/statistics.js'
                                            ]);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["statistics"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.serverperfstatistics', {
                        url: '/serverperfstatistics',
                        templateUrl: 'tpl/app_serverperfstatistics.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/statistics.js'
                                            ]);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["statistics"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.product', {
                        url: '/product',
                        templateUrl: 'tpl/app_product.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load([
                                                'js/controllers/product.js'
                                            ]);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.ipgroup', {
                        url: '/ipgroup',
                        templateUrl: 'tpl/app_ipgroup.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/ipgroup.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["ipgroup"],
                        data: {
                            authPoint: {}
                        }
                    })
                    .state('app.ip', {
                        url: '/ip',
                        templateUrl: 'tpl/app_ip.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/ip.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["ip"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.configFile', {
                        url: '/configFile',
                        templateUrl: 'tpl/app_configFile.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/configFile.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["configFile"],
                        data: {
                            authPoint: {}
                        }
                    })
                    .state('app.configFileGroup', {
                        url: '/configFileGroup',
                        templateUrl: 'tpl/app_configFileGroup.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/configFileGroup.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["configFile"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.configCenter', {
                        url: '/configCenter',
                        templateUrl: 'tpl/app_config_center.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/configCenter.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["configCenter"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.dns', {
                        url: '/dns',
                        templateUrl: 'tpl/app_dns.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/dns.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["dns"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.resource', {
                        url: '/resource',
                        templateUrl: 'tpl/app_resource.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/resource.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.role', {
                        url: '/role',
                        templateUrl: 'tpl/app_role.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/role.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.user', {
                        url: '/user',
                        templateUrl: 'tpl/app_user.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/user.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.system', {
                        url: '/system',
                        templateUrl: 'tpl/app_system.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/system.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.users', {
                        url: '/users',
                        templateUrl: 'tpl/app_cmdb_users.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/cmdbUsers.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.usersLeave', {
                        url: '/usersLeave',
                        templateUrl: 'tpl/app_cmdb_users_leave.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/cmdbUsersLeave.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.ciusers', {
                        url: '/ciusers',
                        templateUrl: 'tpl/app_ciusers.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/ciusers.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.cigroups', {
                        url: '/cigroups',
                        templateUrl: 'tpl/app_cigroups.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/cigroups.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.userDetail', {
                        url: '/userDetail',
                        templateUrl: 'tpl/app_userDetail.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/userDetail.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["user"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.explain', {
                        url: '/explain',
                        templateUrl: 'tpl/app_explain.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/explain.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: [],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.todoConfig', {
                        url: '/todoConfig',
                        templateUrl: 'tpl/app_todoConfig.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/todoConfig.js']);
                                        }
                                    );
                                }]
                        }
                    })
                    .state('app.todo', {
                        url: '/todo',
                        templateUrl: 'tpl/app_todo.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/todo.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["todo"],
                        data: {
                            authPoint: {}
                        }
                    })
                    .state('app.todoDaily', {
                        url: '/todoDaily',
                        templateUrl: 'tpl/app_todoDaily.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/todoDaily.js']);
                                        }
                                    );
                                }]
                        },
                        authGroup: ["todo"],
                        data: {
                            authPoint: {}
                        }
                    })

                    .state('app.logService', {
                        url: '/logService',
                        templateUrl: 'tpl/app_logService.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/logService.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.javaLogService', {
                        url: '/javaLogService',
                        templateUrl: 'tpl/app_java_logService.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/javaLogService.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.javaLogServiceManage', {
                        url: '/javaLogServiceMange',
                        templateUrl: 'tpl/app_java_logService_manage.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/javaLogServiceManage.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('app.keyboxmanage', {
                        url: '/keybox/manage',
                        templateUrl: 'tpl/app_keybox_manage.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/keyboxmanage.js']);
                                        }
                                    );
                                }]
                        }
                    })
                    .state('app.keybox', {
                        url: '/keybox',
                        templateUrl: 'tpl/app_keybox.html',
                        permission: true,
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/keybox.js']);
                                        }
                                    );
                                }]
                        }
                    })

                    .state('access', {
                        url: '/access',
                        template: '<div ui-view class="fade-in-right-big smooth"></div>'
                    })
                    .state('access.signin', {
                        url: '/signin',
                        templateUrl: 'tpl/page_signin.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/signin.js']);
                                }]
                        }
                    })
                    .state('access.signup', {
                        url: '/signup',
                        templateUrl: 'tpl/page_signup.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/signup.js']);
                                }]
                        }
                    })

                    .state('access.system', {
                        url: '/system',
                        templateUrl: 'tpl/page_system.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/system.js']);
                                        }
                                    );
                                }]
                        }
                    })
                    .state('access.todo', {
                        url: '/todo',
                        templateUrl: 'tpl/page_todo.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['ui.select', 'toaster']).then(
                                        function () {
                                            return $ocLazyLoad.load(['js/controllers/accessTodo.js']);
                                        }
                                    );
                                }]
                        }
                    })
            }
        ]
    );