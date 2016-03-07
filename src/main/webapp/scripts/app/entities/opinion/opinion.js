'use strict';

angular.module('plumbeerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('opinion', {
                parent: 'entity',
                url: '/opinions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.opinion.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/opinion/opinions.html',
                        controller: 'OpinionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('opinion');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('opinion.detail', {
                parent: 'entity',
                url: '/opinion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.opinion.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/opinion/opinion-detail.html',
                        controller: 'OpinionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('opinion');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Opinion', function($stateParams, Opinion) {
                        return Opinion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('opinion.new', {
                parent: 'opinion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/opinion/opinion-dialog.html',
                        controller: 'OpinionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    titulo: null,
                                    comentario: null,
                                    fecha: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('opinion', null, { reload: true });
                    }, function() {
                        $state.go('opinion');
                    })
                }]
            })
            .state('opinion.edit', {
                parent: 'opinion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/opinion/opinion-dialog.html',
                        controller: 'OpinionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Opinion', function(Opinion) {
                                return Opinion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('opinion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('opinion.delete', {
                parent: 'opinion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/opinion/opinion-delete-dialog.html',
                        controller: 'OpinionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Opinion', function(Opinion) {
                                return Opinion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('opinion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
