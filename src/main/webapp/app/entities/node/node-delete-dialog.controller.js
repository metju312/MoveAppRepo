(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('NodeDeleteController',NodeDeleteController);

    NodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Node'];

    function NodeDeleteController($uibModalInstance, entity, Node) {
        var vm = this;

        vm.node = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Node.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
