angular.module('webappApp')
    .factory('RestService', ['$http', '$q', '$log', '$rootScope', function($http, $q, $log, $rootScope) {
        function _get(uri) {
            $log.info('-> Get data from: ' + uri.substring(uri.lastIndexOf('/') + 1));
            // TODO: check signin/reg
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                return $http.get(uri).then(successCallback, errorCallback);
            }
            return false;
        }

        function _post(uri, data) {
            $log.info('-> Post data to: ' + uri.substring(uri.lastIndexOf('/') + 1));
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                return $http.post(uri, data).then(successCallback, errorCallback);
            }
            return false;
        }

        function _put(uri, data) {
            $log.info('-> Put data to: ' + uri.substring(uri.lastIndexOf('/') + 1));
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                return $http.put(uri, data).then(successCallback, errorCallback);
            }
            return false;
        }

        function _delete(uri, id) {
            $log.info('-> Delete data from: ' + uri.substring(uri.lastIndexOf('/') + 1));
            if ($rootScope.currentUser == null) {
                toastr.warning("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                return $http.delete(uri + '/' + id).then(successCallback, errorCallback);
            }
            return false;
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