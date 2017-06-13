angular.module('webappApp')
    .factory('PatientService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.create = create;
        service.getByCreator = getByCreator;
        return service;

        function create(patient) {
            return RestService.restPost(GlobalService.createPatient, patient);
        }

        function getByCreator(creator) {
            return RestService.restGet(GlobalService.getPatientByCreator + creator);
        }
    }
]);