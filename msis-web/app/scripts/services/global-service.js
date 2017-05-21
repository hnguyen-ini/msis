angular.module('webappApp')
    .factory('GlobalService', function() {
        var service = {};
        service.s1 = "DFGSDG233DJDPR3S";
        service.s2 = "495ae7e34b9da43b9b4bdac644529aa7";
        service.s3 = "9609f89caaa074264acf4df75122dbdc";
        service.host = "http://localhost:8080/msis-rest/rest/";
        service.registerUri = service.host + "authen/reg";

        return service;
    });