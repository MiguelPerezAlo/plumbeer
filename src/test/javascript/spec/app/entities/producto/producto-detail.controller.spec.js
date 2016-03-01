'use strict';

describe('Controller Tests', function() {

    describe('Producto Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProducto, MockCategoria, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProducto = jasmine.createSpy('MockProducto');
            MockCategoria = jasmine.createSpy('MockCategoria');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Producto': MockProducto,
                'Categoria': MockCategoria,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ProductoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'plumbeerApp:productoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
