angular.module('webappApp')
    .factory('RecordService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.create = create;
        service.update = update;
        service.getByPatientId = getByPatientId;
        service.search = search;
        return service;

        function create(record, token) {
            return RestService.restPost(GlobalService.createRecord + '?accessToken=' + token, record);
        }

        function update(patient, token) {
            return RestService.restPut(GlobalService.updatePatient + '?accessToken=' + token, patient);
        }

        function getByPatientId(patientId, token) {
            return RestService.restGet(GlobalService.getRecordsByPatientId + patientId + '?accessToken=' + token);
        }

        function  search(search, token) {
            return RestService.restGet(GlobalService.searchPatient + search + '?accessToken=' + token);
        }
    }
]);