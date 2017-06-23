(function() {
    'use strict';

    angular
        .module('moveApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('statistics', {
            parent: 'entity',
            url: '/statistics?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.statistics.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statistics/statistics.html',
                    controller: 'StatisticsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statistics');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('statistics-detail', {
            parent: 'statistics',
            url: '/statistics/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'moveApp.statistics.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statistics/statistics-detail.html',
                    controller: 'StatisticsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statistics');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Statistics', function($stateParams, Statistics) {
                    return Statistics.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'statistics',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('statistics-detail.edit', {
            parent: 'statistics-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-dialog.html',
                    controller: 'StatisticsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Statistics', function(Statistics) {
                            return Statistics.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statistics.new', {
            parent: 'statistics',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-dialog.html',
                    controller: 'StatisticsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                initialDate: null,
                                finalDate: null,
                                distance: null,
                                duration: null,
                                steps: null,
                                caloriesBurnt: null,
                                averageSpeed: null,
                                maxSpeed: null,
                                numberOfActivities: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('statistics', null, { reload: 'statistics' });
                }, function() {
                    $state.go('statistics');
                });
            }]
        })
        .state('statistics.edit', {
            parent: 'statistics',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-dialog.html',
                    controller: 'StatisticsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Statistics', function(Statistics) {
                            return Statistics.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statistics', null, { reload: 'statistics' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statistics.delete', {
            parent: 'statistics',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-delete-dialog.html',
                    controller: 'StatisticsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Statistics', function(Statistics) {
                            return Statistics.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statistics', null, { reload: 'statistics' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
