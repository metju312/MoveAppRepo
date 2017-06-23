(function() {
    'use strict';

    angular
        .module('moveApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('friendship', {
            parent: 'entity',
            url: '/friendship',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.friendship.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendship/friendships.html',
                    controller: 'FriendshipController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendship');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('friendship-detail', {
            parent: 'friendship',
            url: '/friendship/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.friendship.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendship/friendship-detail.html',
                    controller: 'FriendshipDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendship');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Friendship', function($stateParams, Friendship) {
                    return Friendship.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'friendship',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('friendship-detail.edit', {
            parent: 'friendship-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendship-dialog.html',
                    controller: 'FriendshipDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friendship', function(Friendship) {
                            return Friendship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendship.new', {
            parent: 'friendship',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendship-dialog.html',
                    controller: 'FriendshipDialogController',
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
                    $state.go('friendship', null, { reload: 'friendship' });
                }, function() {
                    $state.go('friendship');
                });
            }]
        })
        .state('friendship.edit', {
            parent: 'friendship',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendship-dialog.html',
                    controller: 'FriendshipDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friendship', function(Friendship) {
                            return Friendship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendship', null, { reload: 'friendship' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendship.delete', {
            parent: 'friendship',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendship-delete-dialog.html',
                    controller: 'FriendshipDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Friendship', function(Friendship) {
                            return Friendship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendship', null, { reload: 'friendship' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
