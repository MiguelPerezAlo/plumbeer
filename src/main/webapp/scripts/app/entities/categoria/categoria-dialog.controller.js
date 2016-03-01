'use strict';

angular.module('plumbeerApp').controller('CategoriaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categoria', 'Producto',
        function($scope, $stateParams, $uibModalInstance, entity, Categoria, Producto) {

        $scope.categoria = entity;
        $scope.productos = Producto.query();
        $scope.load = function(id) {
            Categoria.get({id : id}, function(result) {
                $scope.categoria = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerApp:categoriaUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.categoria.id != null) {
                Categoria.update($scope.categoria, onSaveSuccess, onSaveError);
            } else {
                Categoria.save($scope.categoria, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
