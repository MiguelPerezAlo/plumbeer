'use strict';

angular.module('plumbeerApp')
    .controller('AnuncioController', function ($scope, $state, DataUtils, Anuncio) {

        $scope.anuncios = [];
        $scope.loadAll = function() {
            Anuncio.query(function(result) {
               $scope.anuncios = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.anuncio = {
                titulo: null,
                contenido: null,
                publicacion: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
