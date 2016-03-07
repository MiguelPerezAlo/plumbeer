'use strict';

angular.module('plumbeerApp')
    .factory('Anuncio', function ($resource, DateUtils) {
        return $resource('api/anuncios/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.publicacion = DateUtils.convertDateTimeFromServer(data.publicacion);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
