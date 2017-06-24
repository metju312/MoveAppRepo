(function() {
    'use strict';

    angular
        .module('moveApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('friend-request', {
            parent: 'friends',
            url: '/friend-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.friendRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friend-request/friend-requests.html',
                    controller: 'FriendRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('friend-request-detail', {
            parent: 'friend-request',
            url: '/friend-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.friendRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friend-request/friend-request-detail.html',
                    controller: 'FriendRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FriendRequest', function($stateParams, FriendRequest) {
                    return FriendRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'friend-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('friend-request-detail.edit', {
            parent: 'friend-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-request/friend-request-dialog.html',
                    controller: 'FriendRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FriendRequest', function(FriendRequest) {
                            return FriendRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friend-request.new', {
            parent: 'friend-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-request/friend-request-dialog.html',
                    controller: 'FriendRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('friend-request', null, { reload: 'friend-request' });
                }, function() {
                    $state.go('friend-request');
                });
            }]
        })
        .state('friend-request.edit', {
            parent: 'friend-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-request/friend-request-dialog.html',
                    controller: 'FriendRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FriendRequest', function(FriendRequest) {
                            return FriendRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friend-request', null, { reload: 'friend-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friend-request.delete', {
            parent: 'friend-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-request/friend-request-delete-dialog.html',
                    controller: 'FriendRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FriendRequest', function(FriendRequest) {
                            return FriendRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friend-request', null, { reload: 'friend-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
