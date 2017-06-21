/*
 * Record Controller
 */
var app = angular.module('webappApp');
    app.controller('RecordController', ['$uibModal', '$location', function($uibModal, $location) {
        var vm = this;
        vm.record = {};
        vm.record.test = [];
        vm.record.treatment = [];
        
        vm.addNewTest = addNewTest;
        vm.deleteTest = deleteTest;
        vm.addNewTreatment = addNewTreatment;
        vm.deleteTreatment = deleteTreatment;

        vm.cancel = cancel;

        function addNewTest() {
            vm.modalInstanceTest = $uibModal.open({
                templateUrl: '/views/patient/template-test.html',
                controller: ModalTestCtrl,
                resolve: {
                    test: function () {
                        return vm.record.test;
                    }
                }
            });
            vm.modalInstanceTest.result.then(function (test) {
                if (test != null) {
                    vm.record.test.push(test);
                }
            });
        };

        function deleteTest(item) {
            var index = vm.record.test.indexOf(item);
            vm.record.test.splice(index, 1);
        };

        function addNewTreatment() {
            vm.modalInstanceTreatment = $uibModal.open({
                templateUrl: '/views/patient/template-treatment.html',
                controller: ModalTreatmentCtrl,
                resolve: {
                    treatment: function () {
                        return vm.record.treatment;
                    }
                }
            });
            vm.modalInstanceTreatment.result.then(function (treatment) {
                if (treatment != null) {
                    vm.record.treatment.push(treatment);
                }
            });
        };

        function deleteTreatment(item) {
            var index = vm.record.treatment.indexOf(item);
            vm.record.treatment.splice(index, 1);
        };

        function cancel() {
            $location.path('/records');
        }
    }
]);

/*
* ModelTestCtrl
*/
var ModalTestCtrl = function ($scope, $uibModalInstance, test) {

    $scope.test = {};

    $scope.saveTest = function() {
        $uibModalInstance.close($scope.test);
    };

    $scope.closeTest = function() {
        $uibModalInstance.close(null);
    }
};

/*
* ModelTreatmentCtrl
*/
var ModalTreatmentCtrl = function ($scope, $uibModalInstance, treatment) {

    $scope.treatment = {};

    $scope.saveTreatment = function() {
        $uibModalInstance.close($scope.treatment);
    };

    $scope.closeTreatment = function() {
        $uibModalInstance.close(null);
    }
};
