/*
 * Authen Controller
 */
angular.module('webappApp')
    .controller('AuthenController', ['$rootScope', '$timeout', '$location', 'AuthenService', 'UserService', function($rootScope, $timeout, $location, AuthenService, UserService) {
        var vm = this;
        vm.signin = signin;
        vm.signup = signup;
        vm.signout = signout;

        function signup() {
            vm.dataLoading = true;
            UserService.create(vm.user).then(function (response) {
                if (response.success) {
                    toastr.info("Register successfully! Please check email to active your account (It will be expired after 24h)", 'Msis-Web');
                    $location.path('/signin');
                } else {
                    toastr.error(response.message, 'Msis-Web');
                    vm.dataLoading = false;
                }
            });
        }
        
        function signin() {
            vm.dataLoading = true;
            $timeout(function () {
                UserService.signin(vm.user).then(function (response) {
                    if (response.success) {
                        toastr.info("Signin successfully!!!", 'Msis-Web');
                        AuthenService.setCredentials(response.result);
                        $rootScope.signining = false;
                        $location.path('/');
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }, 1000);
            vm.dataLoading = false;
        };

        function signout() {
            AuthenService.clearCredentials();
            $rootScope.signining = true;
            $location.path('/signin');
        }
    }
]);