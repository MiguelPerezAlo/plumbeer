'use strict';

angular.module('plumbeerApp')
    .controller('CategoriaController', function ($scope, $state, Categoria) {

        $scope.categorias = [];
        $scope.loadAll = function() {
            Categoria.query(function(result) {
               $scope.categorias = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.categoria = {
                nombre: null,
                id: null
            };
        };
    });
