'use strict';

angular.module('plumbeerApp')
    .controller('Voto_postController', function ($scope, $state, DataUtils, Voto_post) {

        $scope.voto_posts = [];
        $scope.loadAll = function() {
            Voto_post.query(function(result) {
               $scope.voto_posts = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.voto_post = {
                positivo: null,
                motivo: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
