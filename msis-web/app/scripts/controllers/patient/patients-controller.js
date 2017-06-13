/*
 * Patient Controller
 */
angular.module('webappApp')
    .controller('PatientsController', ['NgTableParams', 'AuthenService', 'PatientService', '$location', '$localStorage', '$rootScope', function(NgTableParams, AuthenService, PatientService, $location, $localStorage, $rootScope) {
        var vm = this;
        vm.loadData = loadData;
        vm.addNew = addNew;
        vm.search = search;
        vm.edit = edit;
        vm.del = del;
        vm.data = {};

        function loadData() {
            var gedIn = $rootScope.currentUser;
            var loggedIn = $localStorage.currentUser;
            var cookies = AuthenService.getCookies();
            if (cookies == null) {
                toastr.error("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                PatientService.getByCreator(cookies.token).then(function (response) {
                    if (response.success) {
                        vm.data = response.result;
                        vm.tableParams = new NgTableParams({count:10}, {counts:[10,20,50,100], dataset: vm.data});
                        vm.tableParams._settings.total = vm.data.length;
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }
        }

        function addNew() {
            $location.path('/patient');
        }

        function search() {
            toastr.info("Search...", "Msis-Web");
        }

        function edit(id) {
            toastr.warn("Edit " + id, 'Msis-Web');
        }

        function del(id) {
            toastr.warn("Delete " + id, 'Msis-Web');
        }
    }
]);