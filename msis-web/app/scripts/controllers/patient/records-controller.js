/*
 * Records Controller
 */
angular.module('webappApp')
    .controller('RecordsController', ['NgTableParams', 'AuthenService', 'PatientService', '$location', '$rootScope', function(NgTableParams, AuthenService, PatientService, $location, $rootScope) {
        var vm = this;
        vm.patient = {};

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
        }

        function addNewRecord() {
            $location.path('/record');
        }
    }
]);