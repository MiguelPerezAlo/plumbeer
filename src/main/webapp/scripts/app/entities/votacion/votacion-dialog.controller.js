'use strict';

angular.module('plumbeerApp').controller('VotacionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Votacion', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Votacion, User) {

        $scope.votacion = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Votacion.get({id : id}, function(result) {
                $scope.votacion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerApp:votacionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.votacion.id != null) {
                Votacion.update($scope.votacion, onSaveSuccess, onSaveError);
            } else {
                Votacion.save($scope.votacion, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
}]);
