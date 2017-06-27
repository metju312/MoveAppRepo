(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('FriendRequestController', FriendRequestController);

    FriendRequestController.$inject = ['FriendRequest', 'ParseLinks', 'AlertService', 'paginationConstants', '$http'];

    function FriendRequestController(FriendRequest, ParseLinks, AlertService, paginationConstants, $http) {

        var vm = this;

        vm.friendRequests = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.candidatesForFriends = {};

        loadAll();

        vm.agree = agree;
        vm.deny = deny;
        function agree(candidateForFriend) {
            $http({
                method:'POST',//add friendship
                url:"/api/friendships/" + candidateForFriend})
                .then(function (response) {
                    $http({
                        method:'DELETE',//delete friendrequest
                        url:"/api//friend-requests/by-login/" + candidateForFriend})
                        .then(function (response) {

                        }, function (reason) {
                            $scope.error = reason;
                        });
                }, function (reason) {
                    $scope.error = reason;
                });
        }

        function deny(candidateForFriend) {
            $http({
                method:'DELETE',//delete friendrequest
                url:"/api//friend-requests/by-login/" + candidateForFriend})
                .then(function (response) {

                }, function (reason) {
                    $scope.error = reason;
                });
        }

        function loadAll () {
            $http({
                method:'GET',
                url:"/api/friend-requests/to-me/"})
                .then(function (response) {
                    vm.candidatesForFriends = response.data;
                }, function (reason) {
                    $scope.error = reason;
                });



            FriendRequest.query({
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
                    vm.friendRequests.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.friendRequests = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
