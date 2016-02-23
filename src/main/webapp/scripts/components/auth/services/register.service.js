'use strict';

angular.module('plumbeerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


