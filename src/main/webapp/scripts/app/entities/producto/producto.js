'use strict';

angular.module('plumbeerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('producto', {
                parent: 'entity',
                url: '/productos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.producto.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/producto/productos.html',
                        controller: 'ProductoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('producto');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('producto.detail', {
                parent: 'entity',
                url: '/producto/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plumbeerApp.producto.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/producto/producto-detail.html',
                        controller: 'ProductoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('producto');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Producto', function($stateParams, Producto) {
                        return Producto.get({id : $stateParams.id});
                    }]
                }
            })
            .state('producto.new', {
                parent: 'producto',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/producto/producto-dialog.html',
                        controller: 'ProductoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombre: null,
                                    precio: null,
                                    descripcion: null,
                                    foto: null,
                                    fotoContentType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('producto', null, { reload: true });
                    }, function() {
                        $state.go('producto');
                    })
                }]
            })
            .state('producto.edit', {
                parent: 'producto',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/producto/producto-dialog.html',
                        controller: 'ProductoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Producto', function(Producto) {
                                return Producto.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('producto', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('producto.delete', {
                parent: 'producto',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/producto/producto-delete-dialog.html',
                        controller: 'ProductoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Producto', function(Producto) {
                                return Producto.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('producto', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
