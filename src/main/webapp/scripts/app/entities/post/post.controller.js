'use strict';

angular.module('plumbeerApp')
    .controller('PostController', function ($scope, $state, DataUtils, Post) {

        $scope.posts = [];
        $scope.loadAll = function() {
            Post.query(function(result) {
               $scope.posts = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.post = {
                titulo: null,
                contenido: null,
                fecha: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
