'use strict';

describe('Controller Tests', function() {

    describe('Opinion Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOpinion, MockUser, MockAnuncio;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOpinion = jasmine.createSpy('MockOpinion');
            MockUser = jasmine.createSpy('MockUser');
            MockAnuncio = jasmine.createSpy('MockAnuncio');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Opinion': MockOpinion,
                'User': MockUser,
                'Anuncio': MockAnuncio
            };
            createController = function() {
                $injector.get('$controller')("OpinionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'plumbeerApp:opinionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
