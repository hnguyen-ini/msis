angular.module('webappApp')
    .controller('PatientController', ['PatientService', 'AuthenService', '$location', '$rootScope', function(PatientService, AuthenService, $location, $rootScope) {
        var vm = this;
        vm.save = save;
        vm.loadData = loadData;

        vm.patient = {};
        vm.edit = false;


        function loadData() {
            vm.patient = $rootScope.patient;
            if (vm.patient != null) {
                vm.edit = true;
                $rootScope.patient = null;
            } else {
                vm.patient = {};
                vm.patient.sex = 'Male';
            }
        }

        function save() {
            var currentUser = $rootScope.currentUser;
            if (currentUser == null) {
                toastr.error("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                vm.patient.creator = currentUser.token;
                if (vm.edit == false) {
                    PatientService.create(vm.patient, currentUser.status).then(function (response) {
                        if (response.success) {
                            toastr.success("The Patient is saved!", 'Msis-Web');
                            $location.path('/patients');
                        } else {
                            toastr.error(response.message, 'Msis-Web');
                        }
                    });
                } else {
                    PatientService.update(vm.patient, currentUser.status).then(function (response) {
                        if (response.success) {
                            toastr.success("The Patient is updated!", 'Msis-Web');
                            $location.path('/home');
                        } else {
                            toastr.error(response.message, 'Msis-Web');
                        }
                    });
                }
            }
        }

       
    }
]);