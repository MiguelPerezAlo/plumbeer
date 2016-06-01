'use strict';

angular.module('plumbeerApp')
    .controller('Voto_postDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Voto_post, Post, User) {
        $scope.voto_post = entity;
        $scope.load = function (id) {
            Voto_post.get({id: id}, function(result) {
                $scope.voto_post = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:voto_postUpdate', function(event, result) {
            $scope.voto_post = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
