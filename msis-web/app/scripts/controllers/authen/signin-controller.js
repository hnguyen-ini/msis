/*
 * Login Controller
 */
angular.module('webappApp')
    .controller('SigninController', ['$timeout', 'UserService', 'AuthenService', function ($timeout, UserService, AuthenService) {
        var vm = this;
        vm.signin = signin;

        function signin() {
            vm.dataLoading = true;
            $timeout(function () {
                UserService.signin(vm.user).then(function (response) {
                    if (response.success) {
                        toastr.info("Signin successfully!!!", 'Msis-Web');
                        AuthenService.setCredentials(response.result);
                        $location.path('/');
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }, 1000);
            vm.dataLoading = false;
        };
    }
]);