/*
 * Authen Controller
 */
angular.module('webappApp')
    .controller('AuthenController', ['$rootScope', '$timeout', '$location', 'AuthenService', 'UserService', function($rootScope, $timeout, $location, AuthenService, UserService) {
        var vm = this;
        vm.signin = signin;
        vm.signup = signup;
        vm.signout = signout;
        vm.changePassword = changePassword;

        vm.dataLoading = false;

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
                        vm.dataLoading = false;

                    }
                });
            }, 1000);
        };

        function signout() {
            AuthenService.clearCredentials();
            $rootScope.signining = true;
            $location.path('/signin');
        }

        function changePassword() {
            vm.dataLoading = true;
            $timeout(function () {
                var currentUser = $rootScope.currentUser;
                if (currentUser == null) {
                    toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                    AuthenService.clearCredentials();
                    $location.path('/signin');
                    return false;
                }
                vm.user.token = currentUser.token;
                vm.user.password = vm.user.currentPassword + ":" + vm.user.password;
                vm.user.status = currentUser.status;

                UserService.changePassword(vm.user).then(function (response) {
                    if (response.success) {
                        toastr.info("Change password successfully!!!", 'Msis-Web');
                        AuthenService.clearCredentials();
                        $location.path('/signin');
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }, 1000);
            vm.dataLoading = false;
        }
    }
]);