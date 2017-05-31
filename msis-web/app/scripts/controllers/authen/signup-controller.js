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
                toastr.options.positionClass='toast-top-center';
           
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
        }
    ]);
})();