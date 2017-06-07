angular.module('webappApp')
    .factory('AuthenService', ['$rootScope', '$http', '$localStorage', '$cookies', 'CryptoService', function($rootScope, $http, $localStorage, $cookies, CryptoService) {
        var service = {};
        service.setCredentials = setCredentials;
        service.clearCredentials = clearCredentials;

        return service;

        function setCredentials(user) {

            $rootScope.currentUser = user
            $rootScope.isSignin = true;
            $rootScope.userName = CryptoService.decrypt(user.firstName) + ' ' + CryptoService.decrypt(user.lastName);

            $localStorage.currentUser = user;
            
            // set default auth header for http requests
            //$http.defaults.headers.common['Authorization'] = 'Basic ' + user.token;

            // store user details in globals cookie that keeps user logged in for 1 week (or until they logout)
            var cookieExp = new Date();
            cookieExp.setDate(cookieExp.getDate() + 7);
            $cookies.putObject('webapApp', $rootScope.globals, { expires: cookieExp });
        }

        function clearCredentials() {
            $rootScope.currentUser = {};
            $rootScope.isSignin = false;
            $localStorage.currentUser = {};
            $cookies.remove('webwebApp');
            //$http.defaults.headers.common.Authorization = 'Basic';
        }

       
    }
]);