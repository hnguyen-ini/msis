/*
 * Account Controller
 */
angular.module('webappApp')
    .controller('AccountController', ['$rootScope', '$location','UserService', function($rootScope, $location, UserService) {
        var vm = this;
        vm.account = {};
        
        (function() {
            if ($rootScope.currentUser == null || $rootScope.currentUser.token == null) {
                 $location.path('/signin');
            } else {
                UserService.GetByToken($rootScope.currentUser.token).then(function(response) {
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