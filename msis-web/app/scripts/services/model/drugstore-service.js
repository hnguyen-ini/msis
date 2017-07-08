angular.module('webappApp')
    .factory('DrugStoreService', ['RestService', 'GlobalService', function(RestService, GlobalService) {
        var service = {};
        service.save = save;
        service.getByCreator = getByCreator;
        service.search = search;
        service.del = del;
        service.saveStore = saveStore;
        service.updateStore = updateStore;
        service.deleteStore = deleteStore;
        service.getStoreByDrug = getStoreByDrug;
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

        function saveStore(drug, token) {
            return RestService.restPost(GlobalService.saveStore + '?accessToken=' + token, drug);
        }

        function updateStore(store, token) {
            return RestService.restPut(GlobalService.updateStore + '?accessToken=' + token, store);
        }

        function deleteStore(id, token) {
            return RestService.restDelete(GlobalService.deleteStore + id + '?accessToken=' + token);
        }

        function getStoreByDrug(drugId, token) {
            return RestService.restGet(GlobalService.getStoreByDrug + drugId + '?accessToken=' + token);
        }
    }
]);