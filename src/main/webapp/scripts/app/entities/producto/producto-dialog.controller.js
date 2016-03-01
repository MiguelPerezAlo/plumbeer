'use strict';

angular.module('plumbeerApp').controller('ProductoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Producto', 'Categoria', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Producto, Categoria, User) {

        $scope.producto = entity;
        $scope.categorias = Categoria.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Producto.get({id : id}, function(result) {
                $scope.producto = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerApp:productoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.producto.id != null) {
                Producto.update($scope.producto, onSaveSuccess, onSaveError);
            } else {
                Producto.save($scope.producto, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.setFoto = function ($file, producto) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        producto.foto = base64Data;
                        producto.fotoContentType = $file.type;
                    });
                };
            }
        };
}]);
