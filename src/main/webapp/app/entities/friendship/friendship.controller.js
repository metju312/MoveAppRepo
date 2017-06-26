(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('FriendshipController', FriendshipController);

    FriendshipController.$inject = ['Friendship', 'ParseLinks', 'AlertService', 'paginationConstants', '$http'];

    function FriendshipController(Friendship, ParseLinks, AlertService, paginationConstants, $http) {

        var vm = this;

        vm.friendships = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        function loadAll () {
            $http({
                method:'GET',
                url:"api/friendships/logins"})
                .then(function (response) {
                    vm.friends = response.data;
                }, function (reason) {
                    $scope.error = reason;
                });





            Friendship.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.friendships.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.friendships = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
