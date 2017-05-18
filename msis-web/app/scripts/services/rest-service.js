angular.module('webappApp')
    .factory('restService', ['$http', '$q', '$log', function($http, $q, $log) {
        function _get(uri) {
            $log.info('-> Get data from: ' + uri.substring(uri.lastIndexOf('/') + 1));
            var deferred = $q.defer();
            $http.get(uri).then(
                function getSuccess(response) {
                    deferred.resolve(response.data);
                },
                function getError(response) {
                    $log.error(response.statusText);
                    deferred.reject(response.statusText);
                }
            );
            return deferred.promiss;
        }

        function _post(uri, data) {
            $log.info('-> Post data to: ' + uri.substring(uri.lastIndexOf('/') + 1));
            var deferred = $q.defer();
            $http.post(uri, data)
                .then(function (response) {
                    deferred.resolve(response.data);
                },
                function getError(response) {
                    $log.error(response.statusText);
                    deferred.reject(response.statusText);
                }
            );
            return deferred.promise;
        }

        function _put(uri, data) {
            $log.info('-> Put data to: ' + uri.substring(uri.lastIndexOf('/') + 1));
            var deferred = $q.defer();
            $http.put(uri, data)
                .then(function (response) {
                    deferred.resolve(response.data);
                },
                function getError(response) {
                    $log.error(response.statusText);
                    deferred.reject(response.statusText);
                }
            );
            return deferred.promise;
        }

        function _delete(uri, id) {
            $log.info('-> Delete data from: ' + uri.substring(uri.lastIndexOf('/') + 1));
            var deferred = $q.defer();
            $http.delete(uri + '/' + id)
                .then(function (response) {
                    deferred.resolve(response.data);
                },
                function (response) {
                    $log.error(response.statusText);
                    deferred.reject(response.statusText);
                }
            );
            return deferred.promise;
        }

        return {
            restGet: _get,
            restPost: _post,
            restPut: _put,
            restDelete: _delete
        }
    }
]);