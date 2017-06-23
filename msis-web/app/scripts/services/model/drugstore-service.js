angular.module('webappApp')
    .factory('DrugStoreService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.save = save;
        service.getByCreator = getByCreator;
        service.search = search;
        service.del = del;
        return service;

        function save(drug, token) {
            return RestService.restPost(GlobalService.saveDrug + '?accessToken=' + token, drug);
        }

        function getByCreator(creator, token) {
            return RestService.restGet(GlobalService.getDrugByCreator + creator + '?accessToken=' + token);
        }

        function  search(search, creator, token) {
            return RestService.restGet(GlobalService.searchDrug + creator + '/name/' + search + '?accessToken=' + token);
        }

        function del(id, token) {
            return RestService.restDelete(GlobalService.deleteDrug + id + '?accessToken=' + token);
        }
    }
]);