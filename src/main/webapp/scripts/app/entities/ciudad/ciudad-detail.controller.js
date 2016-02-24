'use strict';

angular.module('plumbeerApp')
    .controller('CiudadDetailController', function ($scope, $rootScope, $stateParams, entity, Ciudad, User) {
        $scope.ciudad = entity;
        $scope.load = function (id) {
            Ciudad.get({id: id}, function(result) {
                $scope.ciudad = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:ciudadUpdate', function(event, result) {
            $scope.ciudad = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
