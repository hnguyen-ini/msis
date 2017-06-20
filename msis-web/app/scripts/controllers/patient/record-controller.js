/*
 * Record Controller
 */
var app = angular.module('webappApp');
    app.controller('RecordController', ['$uibModal', 'NgTableParams', function($uibModal, NgTableParams) {
        var vm = this;
        vm.record = {};
        vm.record.test = {};
        vm.addNewTest = addNewTest;

        function addNewTest() {
            vm.modalInstance = $uibModal.open({
                templateUrl: '/views/patient/template-test.html',
                controller: ModalTestCtrl,
                resolve: {
                    test: function () {
                        return vm.record.test;
                    }
                }
            });
            vm.modalInstance.result.then(function (test) {
                vm.record.test = test;
                vm.tableParams = new NgTableParams({count:10}, {counts:[10,20,50,100], dataset: vm.record.test});
                vm.tableParams._settings.total = vm.record.test;
            });
        };
    }
]);
var ModalTestCtrl = function ($scope, $uibModalInstance, test) {

        $scope.record = {};
        $scope.record.test = test;

   $scope.saveTest = function() {
        $uibModalInstance.close($scope.record.test);
    };

};
