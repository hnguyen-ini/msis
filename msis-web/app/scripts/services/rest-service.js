angular.module('webappApp')
    .factory('RestService', ['$http', '$log', function($http, $log) {
        function _get(uri) {
            $log.info('-> Get data from: ' + uri.substring(uri.lastIndexOf('/') + 1));
            return $http.get(uri).then(successCallback, errorCallback);
        }

        function _post(uri, data) {
            $log.info('-> Post data to: ' + uri.substring(uri.lastIndexOf('/') + 1));
            return $http.post(uri, data).then(successCallback, errorCallback);
        }

        function _put(uri, data) {
            $log.info('-> Put data to: ' + uri.substring(uri.lastIndexOf('/') + 1));
            return $http.put(uri, data).then(successCallback, errorCallback);
        }

        function _delete(uri, id) {
            $log.info('-> Delete data from: ' + uri.substring(uri.lastIndexOf('/') + 1));
            return $http.delete(uri + '/' + id).then(successCallback, errorCallback);
        }

        function successCallback(response) {
           return response.data;
        }

        function errorCallback(response) {
            var msg = response.data.status.code + ": " + response.data.status.status;
            return { success: false, message: msg };
        }

        return {
            restGet: _get,
            restPost: _post,
            restPut: _put,
            restDelete: _delete
        }
    }
]);