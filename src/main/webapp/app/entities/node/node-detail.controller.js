(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('NodeDetailController', NodeDetailController);

    NodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Node', 'Activity'];

    function NodeDetailController($scope, $rootScope, $stateParams, previousState, entity, Node, Activity) {
        var vm = this;

        vm.node = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moveApp:nodeUpdate', function(event, result) {
            vm.node = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
