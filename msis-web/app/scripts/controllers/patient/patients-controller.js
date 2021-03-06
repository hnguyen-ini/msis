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
        vm.record = record;
        vm.data = {};
        vm.searchText = '';
        vm.dataLoading = true;

        function loadData() {
            var currentUser = $rootScope.currentUser;
            // var loggedIn = $localStorage.currentUser;
            // var cookies = AuthenService.getCookies();
            if (currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                PatientService.getByCreator(currentUser.token, currentUser.status).then(function (response) {
                    if (response.success) {
                        vm.data = response.result;
                        vm.tableParams = new NgTableParams({count:10}, {counts:[10,20,50,100], dataset: vm.data});
                        vm.tableParams._settings.total = vm.data.length;
                        vm.dataLoading = false;
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                        vm.dataLoading = false;
                    }
                });
            }
        }

        function addNew() {
            $location.path('/patient');
        }

        function search() {
            if (vm.searchText.length === 0) {
                toastr.warning("Please enter ID or name to search!", "Msis-Web");
            } else {
                var currentUser = $rootScope.currentUser;
                if (currentUser == null) {
                    toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                    AuthenService.clearCredentials();
                    $location.path('/signin');
                } else {
                    vm.dataLoading = true;
                    PatientService.search(vm.searchText, currentUser.status).then(function (response) {
                        if (response.success) {
                            vm.data = response.result;
                            vm.tableParams = new NgTableParams({count:10}, {counts:[10,20,50,100], dataset: vm.data});
                            vm.tableParams._settings.total = vm.data.length;
                            vm.dataLoading = false;
                        } else {
                            toastr.error(response.message, 'Msis-Web');
                            vm.dataLoading = false;
                        }
                    });
                }
            }
        }

        function edit(p) {
            $rootScope.patient = p;
            $location.path('/patient');
        }

        function record(p) {
            $rootScope.patient = p;
            $location.path('/records');
        }
    }
]);