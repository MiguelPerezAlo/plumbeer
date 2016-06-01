'use strict';

angular.module('plumbeerApp')
    .controller('PostDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Post, User) {
        $scope.post = entity;
        $scope.load = function (id) {
            Post.get({id: id}, function(result) {
                $scope.post = result;
            });
        };
        var unsubscribe = $rootScope.$on('plumbeerApp:postUpdate', function(event, result) {
            $scope.post = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
