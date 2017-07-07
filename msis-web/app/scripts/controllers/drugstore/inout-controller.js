/*
 * InOut Controller
 */
angular.module('webappApp')
    .controller('InOutController', ['NgTableParams', 'AuthenService', 'DrugStoreService', '$location', '$rootScope', '$uibModal', function(NgTableParams, AuthenService, DrugStoreService, $location, $rootScope, $uibModal) {
        var vm = this;
        vm.loadData = loadData;
        vm.drug = {};
        vm.stores = [];
        vm.data = {};

        function loadData() {
            if ($rootScope.drug != null) {
                vm.drug = $rootScope.drug;
            } else {
                toastr.warning('Please choose a drug from the list', 'Msis-Web');
                $location.path('/drugs');
                return false;
            }
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                DrugStoreService.getStoreByDrug(vm.drug.id, $rootScope.currentUser.status).then(function (response) {
                    if (response.success) {
                        vm.stores = response.result;
                        renderData();
                        vm.dataLoading = false;
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                        vm.dataLoading = false;
                    }
                });
            }
        }

        function renderData() {
            var data1 = [];
            angular.forEach(vm.stores, function(store, i) {
                var d = {};
                d.id = store.id;
                d.drugId = store.drugId;
                d.createAt = store.createAt;
                if (store.number < 0) {
                    d.output = store.number;
                    d.outPrice = store.price;
                    d.input = '';
                    d.inPrice = '';
                } else {
                    d.input = store.number;
                    d.inPrice = store.price;
                    d.output = '';
                    d.outPrice = '';
                }
                data1.push(d);
            });
            vm.data = data1;
            vm.tableParams = new NgTableParams({count:10}, {counts:[10,20,50,100], dataset: vm.data});
            vm.tableParams._settings.total = vm.data.length;
        }
    }
]);