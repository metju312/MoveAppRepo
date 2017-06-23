(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('FriendshipDialogController', FriendshipDialogController);

    FriendshipDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Friendship', 'User'];

    function FriendshipDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Friendship, User) {
        var vm = this;

        vm.friendship = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.friendship.id !== null) {
                Friendship.update(vm.friendship, onSaveSuccess, onSaveError);
            } else {
                Friendship.save(vm.friendship, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moveApp:friendshipUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
