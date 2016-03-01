'use strict';

angular.module('plumbeerApp')
    .controller('ProductoController', function ($scope, $state, DataUtils, Producto) {

        $scope.productos = [];
        $scope.loadAll = function() {
            Producto.query(function(result) {
               $scope.productos = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.producto = {
                nombre: null,
                precio: null,
                descripcion: null,
                foto: null,
                fotoContentType: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
