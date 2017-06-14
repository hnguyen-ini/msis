angular.module('webappApp')
    .factory('PatientService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.create = create;
        service.update = update;
        service.getByCreator = getByCreator;
        service.search = search;
        return service;

        function create(patient) {
            return RestService.restPost(GlobalService.createPatient, patient);
        }

        function update(patient) {
            return RestService.restPut(GlobalService.updatePatient, patient);
        }

        function getByCreator(creator) {
            return RestService.restGet(GlobalService.getPatientByCreator + creator);
        }

        function  search(search) {
            return RestService.restGet(GlobalService.searchPatient + search);
        }
    }
]);