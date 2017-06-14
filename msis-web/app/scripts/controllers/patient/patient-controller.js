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
            }
        }

        function save() {
            var cookies = AuthenService.getCookies();
            if (cookies == null) {
                toastr.error("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                vm.patient.creator = cookies.token;
                if (vm.edit == false) {
                    PatientService.create(vm.patient).then(function (response) {
                        if (response.success) {
                            toastr.success("The Patient is saved!", 'Msis-Web');
                            $location.path('/patients');
                        } else {
                            toastr.error(response.message, 'Msis-Web');
                        }
                    });
                } else {
                    PatientService.update(vm.patient).then(function (response) {
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