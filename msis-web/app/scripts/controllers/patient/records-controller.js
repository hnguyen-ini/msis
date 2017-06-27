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
        vm.edit = edit;
        vm.del = del;

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

        function edit(r) {
            $rootScope.record = r;
            $location.path('/record');
        }

        function del(r) {
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
                        RecordService.deleteRecord(r.id, $rootScope.currentUser.status).then(function(response) {
                            if (response.success) {
                                var index = vm.patient.records.indexOf(r);
                                vm.patient.records.splice(index, 1);
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