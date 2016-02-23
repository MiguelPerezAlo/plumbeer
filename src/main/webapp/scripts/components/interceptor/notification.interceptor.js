 'use strict';

angular.module('plumbeerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-plumbeerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-plumbeerApp-params')});
                }
                return response;
            }
        };
    });
