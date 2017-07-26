/*
 * Record Controller
 */
var app = angular.module('webappApp');
    app.controller('RecordController', ['$rootScope', '$uibModal', '$location', 'DrugStoreService', 'RecordService', 'Upload', 'GlobalService', function($rootScope, $uibModal, $location, DrugStoreService, RecordService, Upload, GlobalService) {
        var vm = this;
        vm.record = {};
        vm.record.tests = [];
        vm.record.files = [];
        vm.record.contents = [];
        vm.record.treatments = [];
        vm.drugs = [];
        
        vm.addNewTest = addNewTest;
        vm.deleteTest = deleteTest;
        vm.addNewTreatment = addNewTreatment;
        vm.deleteTreatment = deleteTreatment;
        vm.loadData = loadData;
        vm.uploadFiles = uploadFiles;
        vm.addFiles = addFiles;
        vm.deleteFile = deleteFile;


        vm.cancel = cancel;
        vm.save = save;

        function loadData() {
            if ($rootScope.record != null) {
                vm.record = $rootScope.record;
                vm.record.tests = angular.fromJson(vm.record.test);
                vm.record.files = []; // TODO
                vm.record.contents = []; // TODO
                vm.record.treatments = angular.fromJson(vm.record.treatment);
                $rootScope.record = null;
            }
            loadDrugs();
        }

        function loadDrugs() {
            DrugStoreService.getByCreator($rootScope.currentUser.token, $rootScope.currentUser.status).then(function (response) {
                if (response.success) {
                    vm.drugs = response.result;
                } else {
                    toastr.error(response.message, 'Msis-Web');
                }
            });
        }

        function addNewTest() {
            vm.modalInstanceTest = $uibModal.open({
                templateUrl: '/views/patient/template-test.html',
                controller: ModalTestCtrl,
                resolve: {
                    tests: function () {
                        return vm.record.tests;
                    }
                }
            });
            vm.modalInstanceTest.result.then(function (tests) {
                if (tests != null) {
                    vm.record.tests = tests;
                }
            });
        };

        function deleteTest(item) {
            var index = vm.record.tests.indexOf(item);
            vm.record.tests.splice(index, 1);
        };

        function addNewTreatment() {
            if (vm.drugs.length == 0) {
                toastr.warning('No drug is found, please add new one!', 'Msis-web');
                return false;
            }
            vm.modalInstanceTreatment = $uibModal.open({
                templateUrl: '/views/patient/template-treatment.html',
                controller: ModalTreatmentCtrl,
                resolve: {
                    treatments: function() {
                        return vm.record.treatments;
                    },
                    drugs: function() {
                        return vm.drugs;
                    }
                }
            });
            vm.modalInstanceTreatment.result.then(function (treatments) {
                if (treatments != null) {
                    vm.record.treatments = treatments;
                }
            });
        };

        function deleteTreatment(item) {
            var index = vm.record.treatments.indexOf(item);
            vm.record.treatments.splice(index, 1);
        };

        function cancel() {
            $location.path('/records');
        }

        function save() {
            if ($rootScope.patient == null) {
                toastr.warning('Please choose a patient to continue', 'Msis-Web');
                $location.path('/patients');
                return false;
            }
            
            vm.record.pid = $rootScope.patient.id;
            vm.record.test = angular.toJson(vm.record.tests);
            vm.record.treatment = angular.toJson(vm.record.treatments);
            RecordService.save(vm.record, $rootScope.currentUser.status).then(function (response) {
                if (response.success) {
                    uploadFiles(response.result.id);
                    toastr.success('Record is saved', 'Msis-Web');
                    $location.path('/records');
                } else {
                    toastr.error(response.message, 'Msis-Web');
                }
            });
        };

        function uploadFiles(recordId) {
            if (vm.record.files && vm.record.files.length) {
                var uri = GlobalService.cdnUpload + '?accessToken=' + $rootScope.currentUser.status;
                uri += "&pid=" + vm.record.pid;
                uri += "&recordId=" + recordId;
                angular.forEach(vm.record.files, function(file, i) {
                    var url = uri + "&fileName=" + file.name;
                    Upload.upload({
                        method: 'POST',
                        url: url,
                        file: file
                    }).then(function (response) {
                        var str = response;

                    }, function (response) {
                        if (response.status > 0) {
                            toastr.warning(response.status + ': ' + response.data, 'Msis-Web');
                        }
                    });
                });
            }
        };

        function addFiles(files) {
            if (files && files.length) {
                angular.forEach(files, function(f, i) {
                    var found = false;
                    angular.forEach(vm.record.files, function(fi, j) {
                        if (f.name === fi.name) {
                            found = true;
                            vm.record.files[j] = f;
                        }
                    });
                    if (found == false) {
                        vm.record.files.push(f);
                    }
                });
            }
        };

        function deleteFile(file) {
            var index = vm.record.files.indexOf(file);
            vm.record.files.splice(index, 1);
        };
    }
]);

/*
* ModelTestCtrl
*/
var ModalTestCtrl = function ($scope, $uibModalInstance, tests) {
    
    $scope.tests = tests;
    $scope.test = {};

    $scope.saveTest = function() {
        var found = false;
        angular.forEach($scope.tests, function(t, i){
            if (t.item === $scope.test.item) {
                found = true;
                $scope.tests[i] = $scope.test;
            }
        });
        if (found == false) {
            $scope.tests.push($scope.test);
        }
        $uibModalInstance.close($scope.tests);
    };

    $scope.closeTest = function() {
        $uibModalInstance.close(null);
    }
};

/*
* ModelTreatmentCtrl
*/
var ModalTreatmentCtrl = function ($scope, $uibModalInstance, treatments, drugs) {

    $scope.drugs = drugs;
    $scope.treatments = treatments;
    $scope.treatment = {};

    $scope.saveTreatment = function() {
        if ($scope.selectedObject == null) {
            toastr.warning('Please choose one from the list', 'Msis Web');
            return false;
        }
        if ($scope.selectedObject.originalObject.inStock < $scope.treatment.number) {
            toastr.warning('The number is larger than inStock', 'Msis Web');
            return false;
        }
        $scope.treatment.id = $scope.selectedObject.originalObject.id;
        $scope.treatment.name = $scope.selectedObject.originalObject.name;
        var found = false;
        angular.forEach($scope.treatments, function(t, i){
            if (t.name === $scope.treatment.name) {
                found = true;
                $scope.treatments[i] = $scope.treatment;
            }
        });
        if (found == false) {
            $scope.treatments.push($scope.treatment);
        }
        $uibModalInstance.close($scope.treatments);
    };

    $scope.closeTreatment = function() {
        $uibModalInstance.close(null);
    }
};
