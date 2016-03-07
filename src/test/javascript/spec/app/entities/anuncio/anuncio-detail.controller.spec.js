'use strict';

describe('Controller Tests', function() {

    describe('Anuncio Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAnuncio, MockUser, MockOpinion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAnuncio = jasmine.createSpy('MockAnuncio');
            MockUser = jasmine.createSpy('MockUser');
            MockOpinion = jasmine.createSpy('MockOpinion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Anuncio': MockAnuncio,
                'User': MockUser,
                'Opinion': MockOpinion
            };
            createController = function() {
                $injector.get('$controller')("AnuncioDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'plumbeerApp:anuncioUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
