/*
 * Drugs Controller
 */
angular.module('webappApp')
    .controller('DrugsController', ['NgTableParams', 'AuthenService', 'DrugStoreService', '$location', '$rootScope', '$uibModal', function(NgTableParams, AuthenService, DrugStoreService, $location, $rootScope, $uibModal) {
        var vm = this;
        vm.loadData = loadData;
        vm.addNew = addNew;
        vm.search = search;
        vm.edit = edit;
        vm.del = del;
        vm.inout = inout;
        vm.receipt = receipt;
        vm.renderData = renderData;
        vm.data = {};
        vm.drugs = [];
        vm.drug = {};
        vm.searchText = '';
        vm.dataLoading = true;

        function loadData() {
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                DrugStoreService.getByCreator($rootScope.currentUser.token, $rootScope.currentUser.status).then(function (response) {
                    if (response.success) {
                        vm.drugs = response.result;
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
            vm.data = vm.drugs;
            vm.tableParams = new NgTableParams({count:10}, {counts:[10,20,50,100], dataset: vm.data});
            vm.tableParams._settings.total = vm.data.length;
        }

        function addNew() {
            vm.modalInstance = $uibModal.open({
                templateUrl: '/views/drugstore/drug.html',
                controller: ModalDrugCtrl,
                resolve: {
                    drugs: function () {
                        return vm.drugs;
                    },
                    drug: function() {
                        return null;
                    }
                }
            });
            vm.modalInstance.result.then(function (drugs) {
                if (drugs != null) {
                    vm.drugs = drugs;
                    renderData();
                }
            });
        }

        function edit(d) {
            vm.modalInstance = $uibModal.open({
                templateUrl: '/views/drugstore/drug.html',
                controller: ModalDrugCtrl,
                resolve: {
                    drugs: function () {
                        return vm.drugs;
                    },
                    drug: function() {
                        return d;
                    }
                }
            });
            vm.modalInstance.result.then(function (drugs) {
                if (drugs != null) {
                    vm.drugs = drugs;
                    renderData();
                }
            });
        }

        function receipt() {
            vm.modalInstance = $uibModal.open({
                templateUrl: '/views/drugstore/receipt.html',
                controller: ModalReceiptCtrl,
                resolve: {
                    drugs: function () {
                        return vm.drugs;
                    }
                }
            });
            vm.modalInstance.result.then(function (drugs) {
                if (drugs != null) {
                    vm.drugs = drugs;
                    renderData();
                }
            });
        }

        function del(d) {
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                toastr.warning("<br /><button type='button' id='confirDelete' class='btn btn-danger'>Yes </button>&nbsp;<button type='button' id='cancelDelete' class='btn btn-default'>Cancel</button>",'Are you sure to delete item?', 
                {
                    closeButton: false,
                    allowHtml: true,
                    onShown: function (toast) {
                        $("#confirDelete").click(function(){
                            vm.dataLoading = true;
                            DrugStoreService.del(d.id, $rootScope.currentUser.status).then(function (response) {
                                if (response.success) {
                                    toastr.success("The drug is deleted", 'Msis-Web');
                                    var found = false;
                                    angular.forEach(vm.drugs, function(drug, i){
                                        if (drug.id === d.id) {
                                            vm.drugs.splice(i, 1);
                                            renderData();
                                        }
                                    });
                                    vm.dataLoading = false;
                                } else {
                                    toastr.error(response.message, 'Msis-Web');
                                    vm.dataLoading = false;
                                }
                            });

                        });
                    }
                });
            }
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
                    DrugStoreService.search(vm.searchText, $rootScope.currentUser.token, $rootScope.currentUser.status).then(function (response) {
                        if (response.success) {
                            vm.drugs = response.result; 
                            renderData();
                            vm.dataLoading = false;
                        } else {
                            toastr.error(response.message, 'Msis-Web');
                            vm.dataLoading = false;
                        }
                    });
                }
            }
        }

        function inout(d) {
            $rootScope.drug = d;
            $location.path('/in-out');
        }
    }
]);

/*
* ModelDrugCtrl
*/
var ModalDrugCtrl = function ($scope, $uibModalInstance, drugs, drug, $rootScope, $location, AuthenService, DrugStoreService) {

    $scope.drugs = drugs;
    $scope.drug = {};
    if (drug != null)
        $scope.drug = drug;

    $scope.saveDrug = function() {
        if ($rootScope.currentUser == null) {
            toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
            AuthenService.clearCredentials();
            $location.path('/signin');
        } else {
            $scope.drug.creator = $rootScope.currentUser.token;
            DrugStoreService.save($scope.drug, $rootScope.currentUser.status).then(function (response) {
                if (response.success) {
                    var found = false;
                    angular.forEach($scope.drugs, function(drug, i){
                        if (drug.id === response.result.id) {
                            found = true;
                            $scope.drugs[i] = response.result;
                        }
                    });
                    if (found == false) {
                        $scope.drugs.push(response.result);
                    }
                    $uibModalInstance.close($scope.drugs);
                } else {
                    toastr.error(response.message, 'Msis-Web');
                }
            });
        }
    };

    $scope.closeDrug = function() {
        $uibModalInstance.close(null);
    }
};

/*
* ModelReceiptCtrl
*/
var ModalReceiptCtrl = function ($scope, $uibModalInstance, drugs, $rootScope, $location, AuthenService, DrugStoreService) {

    $scope.drugs = drugs;
    $scope.drug = {};
    $scope.store = {};

    $scope.saveDrug = function() {
        if ($rootScope.currentUser == null) {
            toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
            AuthenService.clearCredentials();
            $location.path('/signin');
        } else {
            if ($scope.selectedObject == null) {
                toastr.warning('Please choose one in the list', 'Msis Web');
            } else {
                $scope.store.drugId = $scope.selectedObject.originalObject.id;
                $scope.store.number = $scope.drug.number;
                $scope.store.price = $scope.drug.inPrice;
                DrugStoreService.saveStore($scope.store, $rootScope.currentUser.status).then(function (response) {
                    if (response.success) {
                        $scope.drug = response.result;
                        angular.forEach($scope.drugs, function(drug, i){
                            if (drug.id === response.result.id) {
                                $scope.drugs[i] = response.result;
                            }
                        });
                        $uibModalInstance.close($scope.drugs);
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }
        }
    };

    $scope.closeDrug = function() {
        $uibModalInstance.close(null);
    }
};