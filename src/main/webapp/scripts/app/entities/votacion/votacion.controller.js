'use strict';

angular.module('plumbeerApp')
    .controller('VotacionController', function ($scope, $state, DataUtils, Votacion) {

        $scope.votacions = [];
        $scope.loadAll = function() {
            Votacion.query(function(result) {
               $scope.votacions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.votacion = {
                positivo: null,
                motivo: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
