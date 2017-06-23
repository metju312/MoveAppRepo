(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('ActivityDetailController', ActivityDetailController);

    ActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Activity', 'Node', 'User'];

    function ActivityDetailController($scope, $rootScope, $stateParams, previousState, entity, Activity, Node, User) {
        var vm = this;

        vm.activity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moveApp:activityUpdate', function(event, result) {
            vm.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
