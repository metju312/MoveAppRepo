(function() {
    'use strict';
    angular
        .module('moveApp')
        .factory('Node', Node);

    Node.$inject = ['$resource'];

    function Node ($resource) {
        var resourceUrl =  'api/nodes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
