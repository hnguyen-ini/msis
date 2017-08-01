angular.module('webappApp')
    .factory('RecordService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.save = save;
        service.deleteRecord = deleteRecord;
        service.getByPatientId = getByPatientId;
        service.deleteContent = deleteContent;
        service.getByRecord = getByRecord;
        service.downloadContent = downloadContent;
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

        function deleteContent(fileName, patientId, recordId, token) {
            return RestService.restPut(GlobalService.cdnDelete + '?accessToken=' + token + '&pid=' + patientId + '&recordId=' + recordId + '&fileName=' + fileName);
        }

        function downloadContent(fileName, patientId, recordId, token) {
            return RestService.restPost(GlobalService.cdnDownload + '?accessToken=' + token + '&pid=' + patientId + '&recordId=' + recordId + '&fileName=' + fileName);
        }

        function getByRecord(recordId, token) {
            return RestService.restGet(GlobalService.cdnGetByRecord + '?accessToken=' + token + '&recordId=' + recordId)
        }

    }
]);