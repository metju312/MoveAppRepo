(function() {
    'use strict';
    angular
        .module('moveApp')
        .factory('FriendRequest', FriendRequest);

    FriendRequest.$inject = ['$resource'];

    function FriendRequest ($resource) {
        var resourceUrl =  'api/friend-requests/:id';

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
