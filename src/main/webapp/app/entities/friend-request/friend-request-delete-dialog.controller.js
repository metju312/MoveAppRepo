(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('FriendRequestDeleteController',FriendRequestDeleteController);

    FriendRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'FriendRequest'];

    function FriendRequestDeleteController($uibModalInstance, entity, FriendRequest) {
        var vm = this;

        vm.friendRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FriendRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
