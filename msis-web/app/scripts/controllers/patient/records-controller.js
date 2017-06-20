/*
 * Records Controller
 */
angular.module('webappApp')
    .controller('RecordsController', ['NgTableParams', 'AuthenService', 'PatientService', '$location', '$localStorage', '$rootScope', function(NgTableParams, AuthenService, PatientService, $location, $localStorage, $rootScope) {
        var vm = this;
        vm.addNewRecord = addNewRecord;

        function addNewRecord() {
            $location.path('/record');
        }
    }
]);