(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('StatisticsDeleteController',StatisticsDeleteController);

    StatisticsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Statistics'];

    function StatisticsDeleteController($uibModalInstance, entity, Statistics) {
        var vm = this;

        vm.statistics = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Statistics.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
