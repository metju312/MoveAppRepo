(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('ActivityDialogController', ActivityDialogController);

    ActivityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Activity', 'Node', 'User','Principal'];

    function ActivityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Activity, Node, User, Principal) {
        var vm = this;

        vm.activity = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.nodes = Node.query();
        vm.users = User.query();

        // Principal.identity().then(function(account) {
        //     vm.account = account;
        // });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            // vm.activity.user.id = vm.account.id;
            if (vm.activity.id !== null) {
                Activity.update(vm.activity, onSaveSuccess, onSaveError);
            } else {
                Activity.save(vm.activity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moveApp:activityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
