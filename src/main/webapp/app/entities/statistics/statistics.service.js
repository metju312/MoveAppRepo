(function() {
    'use strict';
    angular
        .module('moveApp')
        .factory('Statistics', Statistics);

    Statistics.$inject = ['$resource', 'DateUtils'];

    function Statistics ($resource, DateUtils) {
        var resourceUrl =  'api/statistics/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.initialDate = DateUtils.convertLocalDateFromServer(data.initialDate);
                        data.finalDate = DateUtils.convertLocalDateFromServer(data.finalDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.initialDate = DateUtils.convertLocalDateToServer(copy.initialDate);
                    copy.finalDate = DateUtils.convertLocalDateToServer(copy.finalDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.initialDate = DateUtils.convertLocalDateToServer(copy.initialDate);
                    copy.finalDate = DateUtils.convertLocalDateToServer(copy.finalDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
