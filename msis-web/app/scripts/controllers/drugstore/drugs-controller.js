/*
 * Drugs Controller
 */
angular.module('webappApp')
    .controller('DrugsController', ['NgTableParams', 'AuthenService', 'DrugStoreService', '$location', '$rootScope', function(NgTableParams, AuthenService, PatientService, $location, $rootScope) {
        var vm = this;
        vm.loadData = loadData;
        vm.addNew = addNew;
        vm.search = search;
        vm.edit = edit;
        vm.del = del;
        vm.data = {};
        vm.searchText = '';
        vm.dataLoading = true;

        function loadData() {
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                DrugStoreService.getByCreator(currentUser.token, currentUser.status).then(function (response) {
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
            $location.path('/drug');
        }

        function search() {
            if (vm.searchText.length === 0) {
                toastr.warning("Please enter name to search!", "Msis-Web");
            } else {
                if ($rootScope.currentUser == null) {
                    toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                    AuthenService.clearCredentials();
                    $location.path('/signin');
                } else {
                    vm.dataLoading = true;
                    DrugStoreService.search(vm.searchText, $rootScope.currentUser.status).then(function (response) {
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

        function edit(d) {
            $rootScope.drug = d;
            $location.path('/drug');
        }

        function del(d) {
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                vm.dataLoading = true;
                DrugStoreService.del(d.id, $rootScope.currentUser.status).then(function (response) {
                    if (response.success) {
                        toastr.success("The drug is deleted", 'Msis-Web');
                        loadData();
                        vm.dataLoading = false;
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                        vm.dataLoading = false;
                    }
                });
            }
        }
    }
]);