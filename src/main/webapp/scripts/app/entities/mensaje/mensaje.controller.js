'use strict';

angular.module('plumbeerApp')
    .controller('MensajeController', function ($scope, $state, DataUtils, Mensaje, ParseLinks) {

        $scope.mensajes = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Mensaje.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.mensajes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.mensaje = {
                asunto: null,
                cuerpo: null,
                fecha: null,
                leido: null,
                id: null
            };
        };

        $scope.emisor = Mensaje.getMensajesEmisor();

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
