(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('StatisticsDetailController', StatisticsDetailController);

    StatisticsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Statistics', 'User'];

    function StatisticsDetailController($scope, $rootScope, $stateParams, previousState, entity, Statistics, User) {
        var vm = this;

        vm.statistics = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moveApp:statisticsUpdate', function(event, result) {
            vm.statistics = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
