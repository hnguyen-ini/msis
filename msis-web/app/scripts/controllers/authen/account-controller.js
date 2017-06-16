/*
 * Account Controller
 */
angular.module('webappApp')
    .controller('AccountController', ['$rootScope', '$location','UserService', 'AuthenService', function($rootScope, $location, UserService, AuthenService) {
        var vm = this;
        vm.account = {};
        
        (function() {
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                UserService.GetByToken($rootScope.currentUser).then(function(response) {
                    if (response.success) {
                        vm.account = response.result;
                    } else {
                    toastr.error(response.message, 'Msis-Web');
                    }
                });
            }
        })();
    }
]);