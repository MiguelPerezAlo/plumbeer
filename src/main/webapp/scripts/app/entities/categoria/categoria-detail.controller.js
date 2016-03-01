'use strict';

angular.module('plumbeerApp')
    .controller('CategoriaDetailController', function ($scope, $rootScope, $stateParams, entity, Categoria, Producto) {
        $scope.categoria = entity;
        $scope.load = function (id) {
            Categoria.get({id: id}, function(result) {
                $scope.categoria = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:categoriaUpdate', function(event, result) {
            $scope.categoria = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
