'use strict';

angular.module('plumbeerApp')
	.controller('AnuncioDeleteController', function($scope, $uibModalInstance, entity, Anuncio) {

        $scope.anuncio = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Anuncio.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
