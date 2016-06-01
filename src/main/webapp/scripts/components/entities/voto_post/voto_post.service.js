'use strict';

angular.module('plumbeerApp')
    .factory('Voto_post', function ($resource, DateUtils) {
        return $resource('api/voto_posts/:id', {}, {
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
