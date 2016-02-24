'use strict';

angular.module('plumbeerApp')
    .controller('CiudadController', function ($scope, $state, Ciudad) {

        $scope.ciudads = [];
        $scope.loadAll = function() {
            Ciudad.query(function(result) {
               $scope.ciudads = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ciudad = {
                nombre: null,
                id: null
            };
        };
    });
