/*
 * Login Controller
 */
angular.module('webappApp')
    .controller('SigninController', ['AuthenService', function (AuthenService) {
        var vm = this;
        vm.signin = signin;

        function signin() {
            vm.dataLoading = true;
            AuthenService.signin(vm.user);
            vm.dataLoading = false;
        };
    }
]);