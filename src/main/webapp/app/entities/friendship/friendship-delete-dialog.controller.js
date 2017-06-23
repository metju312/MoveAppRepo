(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('FriendshipDeleteController',FriendshipDeleteController);

    FriendshipDeleteController.$inject = ['$uibModalInstance', 'entity', 'Friendship'];

    function FriendshipDeleteController($uibModalInstance, entity, Friendship) {
        var vm = this;

        vm.friendship = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Friendship.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
