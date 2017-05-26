angular.module('webappApp')
    .controller('TokenValidationController', ['$scope', '$location','UserService', function ($scope, $location, UserService) {
        var vm = this;
        var token = $location.url().substring($location.url().indexOf('?') +1 );
        vm.user = {
            token: token
        };

        (function() {
            $scope.message = 'Please wait for validation token..'
            $scope.loading = true;
            UserService.validateToken(vm.user).then(function(response){
                if (response.success) {
                    $scope.message = 'Congratulation, your account is active now!'
                } else {
                    $scope.message = response.message;
                }
            });
            $scope.loading = false;
        })();

        
    }
]);