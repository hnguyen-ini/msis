angular.module('webappApp')
    .factory('PatientService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.create = create;
        service.update = update;
        service.getByCreator = getByCreator;
        service.search = search;
        return service;

        function create(patient, token) {
            return RestService.restPost(GlobalService.createPatient + '?accessToken=' + token, patient);
        }

        function update(patient, token) {
            return RestService.restPut(GlobalService.updatePatient + '?accessToken=' + token, patient);
        }

        function getByCreator(creator, token) {
            return RestService.restGet(GlobalService.getPatientByCreator + creator + '?accessToken=' + token);
        }

        function  search(search, token) {
            return RestService.restGet(GlobalService.searchPatient + search + '?accessToken=' + token);
        }
    }
]);