/*
 * Patient Controller
 */
angular.module('webappApp')
    .controller('PatientsController', ['NgTableParams', 'AuthenService', 'PatientService', '$http', function(NgTableParams, AuthenService, PatientService, $http) {
        var vm = this;
        vm.loadData = loadData;
        vm.data = {};

        function loadData() {
            var cookies = AuthenService.getCookies();
            if (cookies == null) {
                toastr.error("Session was expired. Please signin again!", 'Msis-Web');
                AuthenService.clearCredentials();
                $location.path('/signin');
            } else {
                PatientService.getByCreator(cookies.currentUser.token).then(function (response) {
                    if (response.success) {
                        vm.data = response.result;
                    } else {
                        toastr.error(response.message, 'Msis-Web');
                    }
                });
            }

            PatientService.getByCreator();
        }

        $http.get('data/data.json')
            .then(function(data) {
                vm.data = data.data.person;
                // vm.tableParams = new NgTableParams({count:8, sorting:{name:'asc'}}, 
                //     {
                //         getData: function($defer, pamrams) {
                //             vm.tableParams.data = $filter('orderBy')(vm.data.data.person, vm.tableParams._params.sorting.name);
                //             $defer.resolve(vm.tableParams.data);
                //         }});
                //vm.defaultConfigTableParams = new NgTableParams({}, {dataset: vm.data})
                vm.tableParams = new NgTableParams({count:10, sorting:{firstName:'asc', lastName:'asc'}, filter: {firstName: 'T'}}, {counts:[10,20,50,100], dataset: vm.data});
                vm.tableParams._settings.total = vm.data.length;
                // vm.tableParams._settings.paginationMinBlocks = 2;
                // vm.tableParams._settings.dataset = vm.data.data.person;
                // vm.tableParams.data = vm.data.data.person;

                //vm.tableParams = createUsingFullOptions();
                var test="";
                // function createUsingFullOptions() {
                //     var initialParams = {
                //         count: 5 // initial page size
                //     };
                //     var initialSettings = {
                //         // page size buttons (right set of buttons in demo)
                //         counts: [],
                //         // determines the pager buttons (left set of buttons in demo)
                //         paginationMaxBlocks: 13,
                //         paginationMinBlocks: 2,
                //         dataset: vm.data
                //     };
                //     return new NgTableParams(initialParams, initialSettings);
                // }
            });
    }
]);