(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('NodeDialogController', NodeDialogController);

    NodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Node', 'Activity'];

    function NodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Node, Activity) {
        var vm = this;

        vm.node = entity;
        vm.clear = clear;
        vm.save = save;
        vm.activities = Activity.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.node.id !== null) {
                Node.update(vm.node, onSaveSuccess, onSaveError);
            } else {
                Node.save(vm.node, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('moveApp:nodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
