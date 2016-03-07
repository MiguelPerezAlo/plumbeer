'use strict';

angular.module('plumbeerApp')
	.controller('OpinionDeleteController', function($scope, $uibModalInstance, entity, Opinion) {

        $scope.opinion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Opinion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
