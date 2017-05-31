angular.module('webappApp')
    .factory('AuthenService', ['$rootScope', '$http', '$localStorage', '$cookies', function($rootScope, $http, $localStorage, $cookies) {
        var service = {};
        service.setCredentials = setCredentials;
        service.clearCredentials = clearCredentials;
        service.isSignined = isSignined;

        return service;

        function setCredentials(user) {

            $rootScope.globals = {
                currentUser: user
            };

            $localStorage.currentUser = user;
            
            // set default auth header for http requests
            //$http.defaults.headers.common['Authorization'] = 'Basic ' + user.token;

            // store user details in globals cookie that keeps user logged in for 1 week (or until they logout)
            var cookieExp = new Date();
            cookieExp.setDate(cookieExp.getDate() + 7);
            $cookies.putObject('webwebApp', $rootScope.globals, { expires: cookieExp });
        }

        function clearCredentials() {
            $rootScope.globals = {};
            $localStorage.currentUser = {};
            $cookies.remove('webwebApp');
            $http.defaults.headers.common.Authorization = 'Basic';
        }

        function isSignined() {
            if ($rootScope.globals != null && $rootScope.globals.currentUser != null)
                return true;
            return false;
        }


    }
]);