'use strict';

angular.module('plumbeerApp').controller('PostDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Post', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Post, User) {

        $scope.post = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Post.get({id : id}, function(result) {
                $scope.post = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerApp:postUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.post.id != null) {
                Post.update($scope.post, onSaveSuccess, onSaveError);
            } else {
                Post.save($scope.post, onSaveSuccess, onSaveError);
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
