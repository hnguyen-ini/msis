angular.module('webappApp')
    .config(function($sceDelegateProvider) {
        $sceDelegateProvider.resourceUrlWhitelist([
            'self',
            'http://35.186.152.185:8080/msis-rest/rest/**'
        ]);
    });