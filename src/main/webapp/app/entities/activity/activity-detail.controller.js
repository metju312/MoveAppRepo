(function() {
    'use strict';

    angular
        .module('moveApp')
        .controller('ActivityDetailController', ActivityDetailController);

    ActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Activity', 'Node', 'User', '$http'];

    function ActivityDetailController($scope, $rootScope, $stateParams, previousState, entity, Activity, Node, User, $http) {
        var vm = this;

        vm.activity = entity;
        vm.previousState = previousState.name;


        var unsubscribe = $rootScope.$on('moveApp:activityUpdate', function(event, result) {
            vm.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);


        initMap();

        function initMap() {
            $http({
            method:'GET',
            url:"/api/nodes/activity/" + vm.activity.id})
            .then(function (response) {
                vm.nodes = response.data;
                var destinations = [];
                var length = vm.nodes.length;
                var myLatLng = null;
                var sumX = 0;
                var sumY = 0;
                if (length == 0) {
                    myLatLng = {lat: 52.252778, lng: 20.899918};/*wat*/
                } else {
                    var node = null;
                    for (var i = 0; i < length; i++) {
                        node = vm.nodes[i];
                        destinations.push(new google.maps.LatLng(node.latitude, node.longitude));
                        sumX+=node.latitude;
                        sumY+=node.longitude;
                    }
                    var polylineOptions = {path: destinations, strokeColor:"#16aae9", strokeWeight:3};
                    var polyline = new google.maps.Polyline(polylineOptions);
                    myLatLng = {lat: sumX/length, lng: sumY/length}
                }
                var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 15,
                    center: myLatLng
                });

                if (length != 0) {
                    polyline.setMap(map);
                }
                /*marker - show center*/
                // var marker = new google.maps.Marker({
                //     position: myLatLng,
                //     map: map,
                //     title: 'Hello World!'
                // });
            }, function (reason) {
                $scope.error = reason;
            });



        }
    }
})();
