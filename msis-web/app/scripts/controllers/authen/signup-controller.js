/*
 * SignUp Controller
 */
(function() {
    'use strick';
    angular.module('webappApp')
        .controller('SignupController', ['$rootScope', '$location', 'UserService', 'FlashService', function($rootScope, $location, UserService, FlashService) {
            var vm = this;
            vm.register = register;

            function register() {
                vm.dataLoading = true;
                UserService.create(vm.user).then(function (response) {
                    if (response.success) {
                        FlashService.Success('Registration successful', true);
                        $location.path('/signin');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
            }
        }
    ]);
})();