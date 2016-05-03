'use strict';

angular.module('plumbeerApp')
    .controller('VotacionDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Votacion, User) {
        $scope.votacion = entity;
        $scope.load = function (id) {
            Votacion.get({id: id}, function(result) {
                $scope.votacion = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:votacionUpdate', function(event, result) {
            $scope.votacion = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
