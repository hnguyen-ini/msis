angular.module('webappApp')
    .controller('PatientController', ['PatientService', 'AuthenService', '$location', function(PatientService, AuthenService, $location) {
        var vm = this;
        vm.save = save;

        function save() {
            var cookies = AuthenService.getCookies();
            if (cookies == null) {
                toastr.error("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                vm.patient.creator = cookies.token;
                PatientService.create(vm.patient).then(function (response) {
                    if (response.success) {
                        toastr.info("The Patient is saved!", 'Msis-Web');
                        $location.path('/patients');
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }
        }
    }
]);