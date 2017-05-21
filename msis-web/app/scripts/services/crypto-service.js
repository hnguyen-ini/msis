angular.module('webappApp')
    .factory('CryptoService', ['GlobalService', function(GlobalService) {
        var s2 = CryptoJS.enc.Hex.parse(GlobalService.s2);
        var s3 = CryptoJS.enc.Hex.parse(GlobalService.s3);
        var key = CryptoJS.PBKDF2(GlobalService.s1, s2, { keySize: 128 / 32, iterations: 234 });
                   
        var service = {};
        service.encrypt = encrypt;
        service.decrypt = decrypt;

        return service;

        function encrypt(text) {
            var encrypted = CryptoJS.AES.encrypt(text, key, {iv: s3});
            return encrypted.toString();
        }

        function decrypt(encrypted) {
            var decrypted = CryptoJS.AES.decrypt(encrypted, key, {iv: s3});
            return decrypted.toString(CryptoJS.enc.Utf8);
        }

        // function getKeyAndIV(password) {

        //     var keyBitLength = 256;
        //     var ivBitLength = 128;
        //     var iterations = 234;

        //     var bytesInSalt = 128 / 8;
        //     var salt = CryptoJS.lib.WordArray.random(bytesInSalt);
        //     var iv = CryptoJS.lib.WordArray.random(bytesInSalt);

        //    // var iv128Bits = CryptoJS.PBKDF2(password, salt, { keySize: 128 / 32, iterations: iterations });
        //     var key256Bits = CryptoJS.PBKDF2(password, salt, { keySize: 128 / 32, iterations: iterations });
        //     console.log('salt  '+ salt );
        //     console.log('iv  '+ iv );
        //     console.log('key  '+ key256Bits );
        //     return {
        //         iv: iv,
        //         key: key256Bits
        //     };
            
        // }

        // function  generateKey(){
        //     var salt = CryptoJS.lib.WordArray.random(128/8);
        //     var iv = CryptoJS.lib.WordArray.random(128/8);
        //     console.log('salt  '+ salt );
        //     console.log('iv  '+ iv );
        //     var key128Bits100Iterations = CryptoJS.PBKDF2("Secret Passphrase", salt, { keySize: 128/32, iterations: 100 });
        //     console.log( 'key128Bits100Iterations '+ key128Bits100Iterations);
        //     var encrypted = CryptoJS.AES.encrypt("Message", key128Bits100Iterations, { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7  });
        //     console.log("encrypted: " + encrypted);
        // }

        // function  decrypt(){
        //     var salt = CryptoJS.enc.Hex.parse("4acfedc7dc72a9003a0dd721d7642bde");
        //     var iv = CryptoJS.enc.Hex.parse("69135769514102d0eded589ff874cacd");
        //     var encrypted = "PU7jfTmkyvD71ZtISKFcUQ==";
        //     var key = CryptoJS.PBKDF2("Secret Passphrase", salt, { keySize: 128/32, iterations: 100 });
        //     console.log( 'key '+ key);
        //     var decrypt = CryptoJS.AES.decrypt(encrypted, key, { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
        //     var ddd = decrypt.toString(CryptoJS.enc.Utf8); 
        //     console.log('ddd '+ddd);
        // }


    }
]);