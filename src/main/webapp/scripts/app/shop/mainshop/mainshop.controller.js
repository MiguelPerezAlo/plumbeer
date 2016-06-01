/**
 * Created by miguel.perez on 13/04/2016.
 */
'use strict';

angular.module('plumbeerApp')
    .controller('MainShopController', function ($scope, Principal, $http) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $http.get("/api/users").then(function(response) {
            $scope.users = response.data;
        });
    });
