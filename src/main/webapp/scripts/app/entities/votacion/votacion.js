'use strict';

angular.module('plumbeerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('votacion', {
                parent: 'entity',
                url: '/votacions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.votacion.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/votacion/votacions.html',
                        controller: 'VotacionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('votacion');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('votacion.detail', {
                parent: 'entity',
                url: '/votacion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.votacion.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/votacion/votacion-detail.html',
                        controller: 'VotacionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('votacion');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Votacion', function($stateParams, Votacion) {
                        return Votacion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('votacion.new', {
                parent: 'votacion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/votacion/votacion-dialog.html',
                        controller: 'VotacionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    positivo: null,
                                    motivo: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('votacion', null, { reload: true });
                    }, function() {
                        $state.go('votacion');
                    })
                }]
            })
            .state('votacion.edit', {
                parent: 'votacion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/votacion/votacion-dialog.html',
                        controller: 'VotacionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Votacion', function(Votacion) {
                                return Votacion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('votacion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('votacion.delete', {
                parent: 'votacion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/votacion/votacion-delete-dialog.html',
                        controller: 'VotacionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Votacion', function(Votacion) {
                                return Votacion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('votacion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
