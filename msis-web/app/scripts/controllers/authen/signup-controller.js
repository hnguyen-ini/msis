/*
 * SignUp Controller
 */
angular.module('webappApp')
    .controller('signupController' ['$scope', '$log', 'restService', function($scope, $log, restService) {
        $scope.user = {
            firstName: null,
            lastName: null,
            email: null,
            password: null,
            token: null,
            status: null,
        };

        var vm = this;

        vm.register = register;

        function register() {
            vm.dataLoading = true;
            UserService.Create(vm.user)
                .then(function (response) {
                    if (response.success) {
                        FlashService.Success('Registration successful', true);
                        $location.path('/login');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }
    }
]);