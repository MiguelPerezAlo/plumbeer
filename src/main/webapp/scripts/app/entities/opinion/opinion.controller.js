'use strict';

angular.module('plumbeerApp')
    .controller('OpinionController', function ($scope, $state, DataUtils, Opinion) {

        $scope.opinions = [];
        $scope.loadAll = function() {
            Opinion.query(function(result) {
               $scope.opinions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.opinion = {
                titulo: null,
                comentario: null,
                fecha: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
