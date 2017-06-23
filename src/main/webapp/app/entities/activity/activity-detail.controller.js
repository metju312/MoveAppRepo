(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('ActivityDetailController', ActivityDetailController);

    ActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Activity', 'Node', 'User'];

    function ActivityDetailController($scope, $rootScope, $stateParams, previousState, entity, Activity, Node, User) {
        var vm = this;

        vm.activity = entity;
        vm.previousState = previousState.name;



        var unsubscribe = $rootScope.$on('moveApp:activityUpdate', function(event, result) {
            vm.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);

        initMap();

        function initMap() {
            var myLatLng = {lat: 52.252778, lng: 20.899918};

            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 15,
                center: myLatLng
            });

            var marker = new google.maps.Marker({
                position: myLatLng,
                map: map,
                title: 'Hello World!'
            });
        }
    }
})();
