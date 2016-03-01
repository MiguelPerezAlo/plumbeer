'use strict';

angular.module('plumbeerApp')
    .controller('ProductoDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Producto, Categoria, User) {
        $scope.producto = entity;
        $scope.load = function (id) {
            Producto.get({id: id}, function(result) {
                $scope.producto = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:productoUpdate', function(event, result) {
            $scope.producto = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
