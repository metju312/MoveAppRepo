(function() {
    'use strict';
    angular
        .module('moveApp')
        .factory('Friendship', Friendship);

    Friendship.$inject = ['$resource'];

    function Friendship ($resource) {
        var resourceUrl =  'api/friendships/:id';

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
