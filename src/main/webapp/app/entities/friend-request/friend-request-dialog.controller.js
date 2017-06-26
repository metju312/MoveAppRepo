(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('FriendRequestDialogController', FriendRequestDialogController);

    FriendRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'FriendRequest', 'User', '$http'];

    function FriendRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, FriendRequest, User, $http) {
        var vm = this;

        vm.friendRequest = entity;
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
            if (vm.friendRequest.id !== null) {
                FriendRequest.update(vm.friendRequest, onSaveSuccess, onSaveError);
            } else {
                // vm.friendRequest.user2.login = vm.user.login;
                // FriendRequest.save(vm.friendRequest, onSaveSuccess, onSaveError);

                $http({
                    method:'POST',
                    url:"/api/friend-requests/" + vm.user.login})
                    .then(function (response) {

                    }, function (reason) {
                        $scope.error = reason;
                    });
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moveApp:friendRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
