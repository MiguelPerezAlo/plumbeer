'use strict';

angular.module('plumbeerApp')
	.controller('Voto_postDeleteController', function($scope, $uibModalInstance, entity, Voto_post) {

        $scope.voto_post = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Voto_post.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
