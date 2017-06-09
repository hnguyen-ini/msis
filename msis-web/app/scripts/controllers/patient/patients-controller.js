/*
 * Patient Controller
 */
angular.module('webappApp')
    .controller('PatientsController', ['$scope', '$http', 'NgTableParams', function($scope, $http, NgTableParams) {
        var vm = this;
        vm.data = {};
        $http.get('data/data.json')
            .then(function(data) {
                vm.data = data;
                //vm.defaultConfigTableParams = new NgTableParams({}, {dataset: vm.data})
                vm.tableParams = createUsingFullOptions();

                function createUsingFullOptions() {
                    var initialParams = {
                        count: 5 // initial page size
                    };
                    var initialSettings = {
                        // page size buttons (right set of buttons in demo)
                        counts: [],
                        // determines the pager buttons (left set of buttons in demo)
                        paginationMaxBlocks: 13,
                        paginationMinBlocks: 2,
                        dataset: vm.data
                    };
                    return new NgTableParams(initialParams, initialSettings);
                }
            });
    }
]);