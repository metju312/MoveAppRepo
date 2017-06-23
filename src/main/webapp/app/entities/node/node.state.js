(function() {
    'use strict';

    angular
        .module('moveApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('node', {
            parent: 'entity',
            url: '/node',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.node.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/node/nodes.html',
                    controller: 'NodeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('node');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('node-detail', {
            parent: 'node',
            url: '/node/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.node.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/node/node-detail.html',
                    controller: 'NodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('node');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Node', function($stateParams, Node) {
                    return Node.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'node',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('node-detail.edit', {
            parent: 'node-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node/node-dialog.html',
                    controller: 'NodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Node', function(Node) {
                            return Node.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('node.new', {
            parent: 'node',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node/node-dialog.html',
                    controller: 'NodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                latitude: null,
                                longitude: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('node', null, { reload: 'node' });
                }, function() {
                    $state.go('node');
                });
            }]
        })
        .state('node.edit', {
            parent: 'node',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node/node-dialog.html',
                    controller: 'NodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Node', function(Node) {
                            return Node.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('node', null, { reload: 'node' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('node.delete', {
            parent: 'node',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node/node-delete-dialog.html',
                    controller: 'NodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Node', function(Node) {
                            return Node.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('node', null, { reload: 'node' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
