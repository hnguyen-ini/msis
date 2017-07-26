angular.module('webappApp')
    .factory('RecordService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.save = save;
        service.deleteRecord = deleteRecord;
        service.getByPatientId = getByPatientId;
        return service;

        function save(record, token) {
            return RestService.restPost(GlobalService.saveRecord + '?accessToken=' + token, record);
        }

        function deleteRecord(id, token) {
            return RestService.restDelete(GlobalService.deleteRecord + id + '?accessToken=' + token);
        }

        function getByPatientId(patientId, token) {
            return RestService.restGet(GlobalService.getRecordsByPatientId + patientId + '?accessToken=' + token);
        }

    }
]);