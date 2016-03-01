'use strict';

angular.module('plumbeerApp')
	.controller('ProductoDeleteController', function($scope, $uibModalInstance, entity, Producto) {

        $scope.producto = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Producto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
