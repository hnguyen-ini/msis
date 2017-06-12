/*
 * Patient Controller
 */
angular.module('webappApp')
    .controller('PatientsController', ['NgTableParams', 'AuthenService', 'PatientService', '$location', function(NgTableParams, AuthenService, PatientService, $location) {
        var vm = this;
        vm.loadData = loadData;
        vm.data = {};

        function loadData() {
            var cookies = AuthenService.getCookies();
            if (cookies == null) {
                toastr.error("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                PatientService.getByCreator(cookies.currentUser.token).then(function (response) {
                    if (response.success) {
                        vm.data = response.result;
                        vm.tableParams = new NgTableParams({count:10, sorting:{firstName:'asc', lastName:'asc'}, filter: {firstName: 'T'}}, {counts:[10,20,50,100], dataset: vm.data});
                        vm.tableParams._settings.total = vm.data.length;
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }

        }

        
    }
]);