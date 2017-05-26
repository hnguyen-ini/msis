angular.module('webappApp')
    .factory('AuthenService', ['$rootScope', function($rootScope) {
        var service = {};
        service.setCredentials = setCredentials;
        service.clearCredentials = clearCredentials;

        return service;

        function setCredentials(user) {
            //var authdata = Base64.encode(user.token);

            $rootScope.globals = {
                currentUser: user
            };

            // set default auth header for http requests
            //$http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;

            // store user details in globals cookie that keeps user logged in for 1 week (or until they logout)
            // var cookieExp = new Date();
            // cookieExp.setDate(cookieExp.getDate() + 7);
            // $cookies.putObject('webwebApp', $rootScope.globals, { expires: cookieExp });
        }

        function clearCredentials() {
            $rootScope.globals = {};
            //$cookies.remove('webwebApp');
            //$http.defaults.headers.common.Authorization = 'Basic';
        }
    }
]);