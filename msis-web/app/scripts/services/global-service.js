angular.module('webappApp')
    .factory('GlobalService', function() {
        var service = {};
        service.s1 = "DFGSDG233DJDPR3S";
        service.s2 = "495ae7e34b9da43b9b4bdac644529aa7";
        service.s3 = "9609f89caaa074264acf4df75122dbdc";
        //service.host = "http://localhost:8080/msis-rest/rest/"; // home
        service.host = "http://localhost:8085/msis/rest/"; // company
        //service.host = "http://35.186.152.185:8080/msis-rest/rest/"; // prod
        //authen
        service.registerUri = service.host + "authen/reg";
        service.signinUri = service.host + "authen/signin";
        service.validateToken = service.host + "authen/validateToken";
        service.changePassword = service.host + "authen/changePassword";
        service.accountinfo = service.host + "authen/account/";

        // patient
        service.createPatient = service.host + "patient/create";
        service.updatePatient = service.host + "patient/update";
        service.updatePatientById = service.host + "patient/get/";
        service.updatePatientByIdn = service.host + "patient/get/idn/";
        service.getPatientByCreator = service.host + "patient/get/creator/";
        service.searchPatient = service.host + "patient/search/";

        // drugstore
        service.saveDrug = service.host + "drugstore/save/drug";
        service.deleteDrug = service.host + "drugstore/delete/drug/";
        service.getDrugByCreator = service.host + "drugstore/get/drug/creator/";
        service.searchDrug = service.host + "drugstore/get/drug/creator/";
        service.saveStore = service.host + "drugstore/save/store";
        service.updateStore = service.host + "drugstore/update/store";
        service.deleteStore = service.host + "drugstore/delete/store/";
        service.getStoreByDrug = service.host + "drugstore/get/store/drug/";

        // record
        service.getRecordsByPatientId = service.host + "record/get/patient/";
        service.saveRecord = service.host + "record/save";
        service.deleteRecord = service.host + "record/delete/"

        // content
        service.cdnUpload = service.host + "cdn/upload";
        service.cdnDelete = service.host + "cdn/delete";
        service.cdnGetByRecord = service.host + "cdn/gets";
        service.cdnDownload = service.host + "cdn/download";

        return service;
    });