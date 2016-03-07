'use strict';

angular.module('plumbeerApp').controller('AnuncioDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Anuncio', 'User', 'Opinion',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Anuncio, User, Opinion) {

        $scope.anuncio = entity;
        $scope.users = User.query();
        $scope.opinions = Opinion.query();
        $scope.load = function(id) {
            Anuncio.get({id : id}, function(result) {
                $scope.anuncio = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerApp:anuncioUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.anuncio.id != null) {
                Anuncio.update($scope.anuncio, onSaveSuccess, onSaveError);
            } else {
                Anuncio.save($scope.anuncio, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
        $scope.datePickerForPublicacion = {};

        $scope.datePickerForPublicacion.status = {
            opened: false
        };

        $scope.datePickerForPublicacionOpen = function($event) {
            $scope.datePickerForPublicacion.status.opened = true;
        };
}]);
