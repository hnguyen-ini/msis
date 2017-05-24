angular.module('webappApp')
    .controller('TokenValidationController', ['$location','UserService', function ($location, UserService) {
        var vm = this;
        var token = $location.url().substring($location.url().indexOf('?') +1 );
        vm.user = {
            token: token
        };

        (function() {
            vm.loading = true;
            UserService.validateToken(vm.user).then(function(response){
                if (response.success) {
                    vm.message = 'Congratulation, your account is active now!'
                } else {
                    vm.message = response.message;
                }
            });
            vm.loading = false;
        })();

        
    }
]);