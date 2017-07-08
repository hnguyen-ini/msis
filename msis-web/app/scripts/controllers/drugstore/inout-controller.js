/*
 * InOut Controller
 */
angular.module('webappApp')
    .controller('InOutController', ['NgTableParams', 'AuthenService', 'DrugStoreService', '$location', '$rootScope', '$uibModal', function(NgTableParams, AuthenService, DrugStoreService, $location, $rootScope, $uibModal) {
        var vm = this;
        vm.loadData = loadData;
        vm.edit = edit;
        vm.del = del;
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
                    d.output = -store.number;
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

        function edit(d) {
            var store = {};
            store.id = d.id;
            store.drugId = d.drugId;
            store.createAt = d.createAt;
            if (d.input == '') {
                store.number = -(d.output);
                store.pice = d.outPrice;
            } else {
                store.number = d.input;
                store.price = d.inPrice;
            }
            vm.modalInstance = $uibModal.open({
                templateUrl: '/views/drugstore/in-out-edit.html',
                controller: ModalInOutEditCtrl,
                resolve: {
                    stores: function () {
                        return vm.stores;
                    },
                    store: function() {
                        return store;
                    }
                }
            });
            vm.modalInstance.result.then(function (stores) {
                if (stores != null) {
                    vm.stores = stores;
                    renderData();
                }
            });
        }

        function del(d) {
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
                return false;
            }
            toastr.warning("<br /><button type='button' id='confirDelete' class='btn btn-danger'>Yes </button>&nbsp;<button type='button' id='cancelDelete' class='btn btn-default'>Cancel</button>",'Are you sure to delete item?', 
            {
                closeButton: false,
                allowHtml: true,
                onShown: function (toast) {
                    $("#confirDelete").click(function(){
                        DrugStoreService.deleteStore(d.id, $rootScope.currentUser.status).then(function(response) {
                            if (response.success) {
                                var index = vm.stores.indexOf(d);
                                vm.stores.splice(index, 1);
                                renderData();
                            } else {
                                toastr.error(response.message, 'Msis-Web');
                            }
                        });
                    });
                }
            });
        }
    }
]);

/*
* ModalInOutEditCtrl
*/
var ModalInOutEditCtrl = function ($scope, $uibModalInstance, stores, store, $rootScope, $location, AuthenService, DrugStoreService) {

    $scope.stores = stores;
    $scope.store = {};
    if (store != null)
        $scope.store = store;

    $scope.save = function() {
        if ($rootScope.currentUser == null) {
            toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
            AuthenService.clearCredentials();
            $location.path('/signin');
        } else {
            DrugStoreService.updateStore($scope.store, $rootScope.currentUser.status).then(function (response) {
                if (response.success) {
                    var found = false;
                    angular.forEach($scope.stores, function(sto, i){
                        if (sto.id === response.result.id) {
                            found = true;
                            $scope.stores[i] = response.result;
                        }
                    });
                    if (found == false) {
                        $scope.stores.push(response.result);
                    }
                    $uibModalInstance.close($scope.stores);
                } else {
                    toastr.error(response.message, 'Msis-Web');
                }
            });
        }
    };

    $scope.close = function() {
        $uibModalInstance.close(null);
    }
};