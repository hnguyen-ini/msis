/*
 * Login Controller
 */
angular.module('webappApp')
    .controller('signinController', ['$scope', '$log', 'restService', function ($scope, $log, restService) {
        var vm = this;
        vm.signin = signin;

        function signin() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.username, vm.password, function (response) {
                if (response.success) {
                    AuthenticationService.SetCredentials(vm.username, vm.password);
                    $location.path('/');
                } else {
                    FlashService.Error(response.message);
                    vm.dataLoading = false;
                }
            });
        };
    }
]);