angular.module('webappApp')
    .factory('AuthenService', ['$rootScope', '$timeout', 'UserService', function($rootScope, $timeout, UserService) {
        var service = {};
        service.signin = signin;
        service.setCredentials = setCredentials;
        service.clearCredentials = clearCredentials;

        return service;

        function signin(user, callback) {
            $timeout(function () {
                UserService.signin(user).then(function (response) {
                    if (response.success) {
                        toastr.info("Signin successfully!!!", 'Msis-Web');
                        $location.path('/signup');
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }, 1000);

            /* Use this for real authentication
             ----------------------------------------------*/
            //$http.post('/api/authenticate', { username: username, password: password })
            //    .success(function (response) {
            //        callback(response);
            //    });

        }

        function setCredentials(username, password) {
            var authdata = Base64.encode(username + ':' + password);

            $rootScope.globals = {
                currentUser: {
                    username: username,
                    authdata: authdata
                }
            };

            // set default auth header for http requests
            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;

            // store user details in globals cookie that keeps user logged in for 1 week (or until they logout)
            var cookieExp = new Date();
            cookieExp.setDate(cookieExp.getDate() + 7);
            $cookies.putObject('globals', $rootScope.globals, { expires: cookieExp });
        }

        function clearCredentials() {
            $rootScope.globals = {};
            $cookies.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic';
        }
    }
]);