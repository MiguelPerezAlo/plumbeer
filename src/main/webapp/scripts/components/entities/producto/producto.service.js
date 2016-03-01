'use strict';

angular.module('plumbeerApp')
    .factory('Producto', function ($resource, DateUtils) {
        return $resource('api/productos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
