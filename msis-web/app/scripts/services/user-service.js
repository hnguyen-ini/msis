angular.module('webappApp')
    .factory('UserService', ['$http', 'RestService', 'CryptoService', 'GlobalService', function($http, RestService, CryptoService, GlobalService) {
        var service = {};
        service.GetAll = GetAll;
        service.GetById = GetById;
        service.GetByUsername = GetByUsername;
        service.create = create;
        service.validateToken = validateToken;
        service.Update = Update;
        service.Delete = Delete;
        service.signin = signin;

        return service;

        function GetAll() {
            return $http.get('/api/users').then(handleSuccess, handleError('Error getting all users'));
            //return restService._get('/admin/user/gets');
        }

        function GetById(id) {
            return $http.get('/api/users/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function GetByUsername(username) {
            return $http.get('/api/users/' + username).then(handleSuccess, handleError('Error getting user by username'));
        }

        function create(user) {
            var enUser = encryptUser(user);
            console.log('Create user: ' + angular.toJson(enUser));
            return RestService.restPost(GlobalService.registerUri, enUser);
        }

        function signin(user) {
            var enUser = {};
            enUser.email = CryptoService.encrypt(user.email);
            enUser.password = CryptoService.encrypt(user.password);
            return RestService.restPut(GlobalService.signinUri, enUser);
        }

        function validateToken(user) {
            var enUser = {};
            enUser.token = CryptoService.encrypt(user.token);
            return RestService.restPut(GlobalService.validateToken, enUser);
        }

        function Update(user) {
            return $http.put('/api/users/' + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }

        function Delete(id) {
            return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        // private functions : MAYBE NOT USAGE -> USE AT RESTSERVICE

        function handleSuccess(res) {
            console.log('Before: ' + res.data.result.email + ', token: ' + res.data.result.token);
            console.log('After: ' + CryptoService.decrypt(res.data.result.email) +', token: '+ CryptoService.decrypt(res.data.result.token));
            return res.data;
        }

        function handleError(error) {
            var msg = error.data.status.code + ": " + error.data.status.status;
            return { success: false, message: msg };
            
        }

        function encryptUser(user) {
            var enUser = {};
            enUser.firstName = CryptoService.encrypt(user.firstName);
            enUser.lastName = CryptoService.encrypt(user.lastName);
            enUser.email = CryptoService.encrypt(user.email);
            enUser.password = CryptoService.encrypt(user.password);
            return enUser;
        }
    }
]);