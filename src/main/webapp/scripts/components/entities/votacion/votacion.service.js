'use strict';

angular.module('plumbeerApp')
    .factory('Votacion', function ($resource, DateUtils) {
        return $resource('api/votacions/:id', {}, {
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
