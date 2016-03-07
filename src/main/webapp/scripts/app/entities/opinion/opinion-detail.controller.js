'use strict';

angular.module('plumbeerApp')
    .controller('OpinionDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Opinion, User, Anuncio) {
        $scope.opinion = entity;
        $scope.load = function (id) {
            Opinion.get({id: id}, function(result) {
                $scope.opinion = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:opinionUpdate', function(event, result) {
            $scope.opinion = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
