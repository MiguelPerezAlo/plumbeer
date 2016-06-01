'use strict';

angular.module('plumbeerApp').controller('Voto_postDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Voto_post', 'Post', 'User',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Voto_post, Post, User) {

        $scope.voto_post = entity;
        $scope.posts = Post.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Voto_post.get({id : id}, function(result) {
                $scope.voto_post = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plumbeerApp:voto_postUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.voto_post.id != null) {
                Voto_post.update($scope.voto_post, onSaveSuccess, onSaveError);
            } else {
                Voto_post.save($scope.voto_post, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
}]);
