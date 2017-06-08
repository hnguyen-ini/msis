angular.module('webappApp')
    .factory('GlobalService', function() {
        var service = {};
        service.s1 = "DFGSDG233DJDPR3S";
        service.s2 = "495ae7e34b9da43b9b4bdac644529aa7";
        service.s3 = "9609f89caaa074264acf4df75122dbdc";
        service.host = "http://localhost:8080/msis-rest/rest/"; // home
        //service.host = "http://localhost:8085/msis/rest/"; // company
        //authen
        service.registerUri = service.host + "authen/reg";
        service.signinUri = service.host + "authen/signin";
        service.validateToken = service.host + "authen/validateToken";
        service.changePassword = service.host + "authen/changePassword";
        service.accountinfo = service.host + "authen/account/";
        return service;
    });