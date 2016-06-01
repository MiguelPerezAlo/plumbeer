'use strict';

describe('Controller Tests', function() {

    describe('Voto_post Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockVoto_post, MockPost, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockVoto_post = jasmine.createSpy('MockVoto_post');
            MockPost = jasmine.createSpy('MockPost');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Voto_post': MockVoto_post,
                'Post': MockPost,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("Voto_postDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'plumbeerApp:voto_postUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
