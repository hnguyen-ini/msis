(function(){
    'use strick';
    angular.module('webappApp')
        .factory('UploadService', ['$http', function($http) {
            var service = {};
            service.upload = upload;
            return service;

            function upload(file, uri) {
                var fd = new FormData();
                fd.append('file', file);
            
                $https.post(uri, fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(function(){
                })
                .error(function(){
                });
            }
        }]);
})();