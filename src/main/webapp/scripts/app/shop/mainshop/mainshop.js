/**
 * Created by miguel.perez on 13/04/2016.
 */
'use strict';

angular.module('plumbeerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('shop', {
                parent: 'shop',
                url: '/mainshop',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/mainshop/mainshop.html',
                        controller: 'MainShopController'
                    }
                },
                resolve: {
                    TranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('mainshop');
                        return $translate.refresh();
                    }]
                }
            });
    });
