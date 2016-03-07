'use strict';

angular.module('plumbeerApp')
    .controller('AnuncioDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Anuncio, User, Opinion) {
        $scope.anuncio = entity;
        $scope.load = function (id) {
            Anuncio.get({id: id}, function(result) {
                $scope.anuncio = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:anuncioUpdate', function(event, result) {
            $scope.anuncio = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
