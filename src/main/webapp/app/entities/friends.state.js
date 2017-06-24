(function() {
    'use strict';

    angular
        .module('moveApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('friends', {
            abstract: true,
            parent: 'app'
        });
    }
})();
