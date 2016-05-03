'use strict';

angular.module('plumbeerApp')
	.controller('VotacionDeleteController', function($scope, $uibModalInstance, entity, Votacion) {

        $scope.votacion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Votacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
