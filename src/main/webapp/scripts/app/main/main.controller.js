'use strict';

angular.module('plumbeerApp')
    .controller('MainController', function ($scope, Principal, $http) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $http.get("/api/users").then(function(response) {
            $scope.users = response.data;
        });
        $http.get("/api/unread").then(function(response) {
            $scope.unread = response.data;
        });
    });

