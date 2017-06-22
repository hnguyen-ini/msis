angular.module('webappApp')
    .factory('DrugStoreService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.create = create;
        service.update = update;
        service.getByCreator = getByCreator;
        service.search = search;
        service.del = del;
        return service;

        function create(drug, token) {
            return RestService.restPost(GlobalService.createDrug + '?accessToken=' + token, drug);
        }

        function update(drug, token) {
            return RestService.restPut(GlobalService.updateDrug + '?accessToken=' + token, drug);
        }

        function getByCreator(creator, token) {
            return RestService.restGet(GlobalService.getDrugByCreator + creator + '?accessToken=' + token);
        }

        function  search(search, token) {
            return RestService.restGet(GlobalService.searchDrug + search + '?accessToken=' + token);
        }

        function del(id, token) {
            return RestService.restGet(GlobalService.deleteDrug + id + '?accessToken=' + token);
        }
    }
]);