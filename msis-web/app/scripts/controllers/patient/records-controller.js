/*
 * Records Controller
 */
angular.module('webappApp')
    .controller('RecordsController', ['NgTableParams', 'AuthenService', 'RecordService', '$location', '$rootScope', function(NgTableParams, AuthenService, RecordService, $location, $rootScope) {
        var vm = this;
        vm.data = {};
        vm.patient = {};
        vm.patient.records = [];

        vm.loadData = loadData;
        vm.addNewRecord = addNewRecord;

        function loadData() {
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
                return false;
            }
            if ($rootScope.patient == null) {
                toastr.warning("Please choose a patient", 'Msis-Web');
                $location.path('/patients');
                return false;
            }
            vm.patient = $rootScope.patient;
            RecordService.getByPatientId($rootScope.patient.id, $rootScope.currentUser.status).then(function (response) {
                if (response.success) {
                    vm.patient.records = response.result;
                    renderData();
                    vm.dataLoading = false;
                } else {
                    vm.patient.records = [];
                    toastr.error(response.message, 'Msis-Web');
                    vm.dataLoading = false;
                }
            });
        }

        function renderData() {
            vm.data = vm.patient.records;
            vm.tableParams = new NgTableParams({count:10}, {counts:[10,20,50,100], dataset: vm.data});
            vm.tableParams._settings.total = vm.data.length;
        }

        function addNewRecord() {
            $location.path('/record');
        }
    }
]);