/**
 * Created by miguel.perez on 27/04/2016.
 */
'use strict';
angular.module('plumbeerApp')
    .controller('SidebarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
