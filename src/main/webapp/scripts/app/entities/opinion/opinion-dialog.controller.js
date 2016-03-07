'use strict';

angular.module('plumbeerApp').controller('OpinionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Opinion', 'User', 'Anuncio',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Opinion, User, Anuncio) {

        $scope.opinion = entity;
        $scope.users = User.query();
        $scope.anuncios = Anuncio.query();
        $scope.load = function(id) {
            Opinion.get({id : id}, function(result) {
                $scope.opinion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerApp:opinionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.opinion.id != null) {
                Opinion.update($scope.opinion, onSaveSuccess, onSaveError);
            } else {
                Opinion.save($scope.opinion, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
        $scope.datePickerForFecha = {};

        $scope.datePickerForFecha.status = {
            opened: false
        };

        $scope.datePickerForFechaOpen = function($event) {
            $scope.datePickerForFecha.status.opened = true;
        };
}]);
