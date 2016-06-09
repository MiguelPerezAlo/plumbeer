'use strict';

angular.module('plumbeerApp')
    .factory('Mensaje', function ($resource, DateUtils) {
        return $resource('api/mensajes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getMensajesEmisor':{
                method: 'GET',
                isArray: true,
                url : 'api/emisor'
            }
        });
    });
