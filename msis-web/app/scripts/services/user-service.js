angular.module('webappApp')
    .factory('UserService', ['$http', function($http) {
        var service = {};
        service.GetAll = GetAll;
        service.GetById = GetById;
        service.GetByUsername = GetByUsername;
        service.create = create;
        service.Update = Update;
        service.Delete = Delete;

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

        function getKeyAndIV(password) {

            var keyBitLength = 256;
            var ivBitLength = 128;
            var iterations = 234;

            var bytesInSalt = 128 / 8;
            var salt = CryptoJS.lib.WordArray.random(bytesInSalt);

            var iv128Bits = CryptoJS.PBKDF2(password, salt, { keySize: 128 / 32, iterations: iterations });
            var key256Bits = CryptoJS.PBKDF2(password, salt, { keySize: 256 / 32, iterations: iterations });

            return {
                iv: iv128Bits,
                key: key256Bits
            };
        };

        function create(user) {
            skey = getKeyAndIV("Password01");
            var text = "My Secret text";
            var key = CryptoJS.enc.Base64.parse("253D3FB468A0E24677C28A624BE0F939");
            var iv  = CryptoJS.enc.Base64.parse("                ");
            var encrypted = CryptoJS.AES.encrypt(text, skey.key, {iv: skey.iv});
            console.log(encrypted.toString());
            var decrypted = CryptoJS.AES.decrypt(encrypted, skey.key, {iv: skey.iv});
            console.log(decrypted.toString(CryptoJS.enc.Utf8));
            return $http.post('http://localhost:8085/msis/rest/authen/reg', user).then(handleSuccess, handleError('Error creating user'));
        }

        function Update(user) {
            return $http.put('/api/users/' + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }

        function Delete(id) {
            return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        // private functions : MAYBE NOT USAGE -> USE AT RESTSERVICE

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }
]);