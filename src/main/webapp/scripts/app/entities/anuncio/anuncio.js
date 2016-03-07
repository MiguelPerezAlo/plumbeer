'use strict';

angular.module('plumbeerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('anuncio', {
                parent: 'entity',
                url: '/anuncios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.anuncio.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/anuncio/anuncios.html',
                        controller: 'AnuncioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('anuncio');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('anuncio.detail', {
                parent: 'entity',
                url: '/anuncio/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.anuncio.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/anuncio/anuncio-detail.html',
                        controller: 'AnuncioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('anuncio');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Anuncio', function($stateParams, Anuncio) {
                        return Anuncio.get({id : $stateParams.id});
                    }]
                }
            })
            .state('anuncio.new', {
                parent: 'anuncio',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/anuncio/anuncio-dialog.html',
                        controller: 'AnuncioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    titulo: null,
                                    contenido: null,
                                    publicacion: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('anuncio', null, { reload: true });
                    }, function() {
                        $state.go('anuncio');
                    })
                }]
            })
            .state('anuncio.edit', {
                parent: 'anuncio',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/anuncio/anuncio-dialog.html',
                        controller: 'AnuncioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Anuncio', function(Anuncio) {
                                return Anuncio.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('anuncio', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('anuncio.delete', {
                parent: 'anuncio',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/anuncio/anuncio-delete-dialog.html',
                        controller: 'AnuncioDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Anuncio', function(Anuncio) {
                                return Anuncio.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('anuncio', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
