(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('StatisticsDialogController', StatisticsDialogController);

    StatisticsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Statistics', 'User'];

    function StatisticsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Statistics, User) {
        var vm = this;

        vm.statistics = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.statistics.id !== null) {
                Statistics.update(vm.statistics, onSaveSuccess, onSaveError);
            } else {
                Statistics.save(vm.statistics, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moveApp:statisticsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.initialDate = false;
        vm.datePickerOpenStatus.finalDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
