'use strict';

angular.module('plumbeerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('voto_post', {
                parent: 'entity',
                url: '/voto_posts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.voto_post.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/voto_post/voto_posts.html',
                        controller: 'Voto_postController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('voto_post');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('voto_post.detail', {
                parent: 'entity',
                url: '/voto_post/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.voto_post.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/voto_post/voto_post-detail.html',
                        controller: 'Voto_postDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('voto_post');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Voto_post', function($stateParams, Voto_post) {
                        return Voto_post.get({id : $stateParams.id});
                    }]
                }
            })
            .state('voto_post.new', {
                parent: 'voto_post',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/voto_post/voto_post-dialog.html',
                        controller: 'Voto_postDialogController',
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
                        $state.go('voto_post', null, { reload: true });
                    }, function() {
                        $state.go('voto_post');
                    })
                }]
            })
            .state('voto_post.edit', {
                parent: 'voto_post',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/voto_post/voto_post-dialog.html',
                        controller: 'Voto_postDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Voto_post', function(Voto_post) {
                                return Voto_post.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('voto_post', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('voto_post.delete', {
                parent: 'voto_post',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/voto_post/voto_post-delete-dialog.html',
                        controller: 'Voto_postDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Voto_post', function(Voto_post) {
                                return Voto_post.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('voto_post', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
