(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('FriendshipDetailController', FriendshipDetailController);

    FriendshipDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Friendship', 'User'];

    function FriendshipDetailController($scope, $rootScope, $stateParams, previousState, entity, Friendship, User) {
        var vm = this;

        vm.friendship = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('moveApp:friendshipUpdate', function(event, result) {
            vm.friendship = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
