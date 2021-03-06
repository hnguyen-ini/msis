
/**
 * @ngdoc overview
 * @name webappApp
 * @description
 * # webappApp
 *
 * Main module of the application.
 */
var scotchApp = angular.module('webappApp', ['ngRoute', 'ngStorage', 'ngCookies', 'ngTable', 'ui.bootstrap', 'autoheight', 'angularjs-datetime-picker', 'angucomplete', 'ngFileUpload', 'ngFileSaver']);

    // configure our routes
    scotchApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider

            // route for the home page
            .when('/', {
                templateUrl : 'views/main.html',
                controller  : 'MainCtrl'
            })
            // patient
            .when('/patients', {
                templateUrl : 'views/patient/patients.html',
                controller  : 'PatientsController',
                controllerAs: 'vm'
            })
            .when('/patient', {
                templateUrl : 'views/patient/patient.html',
                controller  : 'PatientController',
                controllerAs: 'vm'
            })
            .when('/records', {
                templateUrl : 'views/patient/records.html',
                controller  : 'RecordsController',
                controllerAs: 'vm'
            })
            .when('/record', {
                templateUrl : 'views/patient/record.html',
                controller  : 'RecordController',
                controllerAs: 'vm'
            })

            // route for the contact page
            .when('/signin', {
                templateUrl : 'views/authen/signin.html',
                controller  : 'AuthenController',
                controllerAs: 'vm'
            })
            .when('/signup', {
                templateUrl : 'views/authen/signup.html',
                controller  : 'AuthenController',
                controllerAs: 'vm'
            })
            .when('/change-password', {
                templateUrl : 'views/authen/change-password.html',
                controller  : 'AuthenController',
                controllerAs: 'vm' 
            })
            .when('/token-validation', {
                templateUrl : 'views/authen/token-validation.html',
                controller  : 'TokenValidationController',
                controllerAs: 'vm' 
            })

            .when('/account', {
                templateUrl : 'views/authen/account.html',
                controller  : 'AccountController',
                controllerAs: 'vm' 
            })
            
            .when('/drugs', {
                templateUrl : 'views/drugstore/drugs.html',
                controller  : 'DrugsController',
                controllerAs: 'vm'
            })
            .when('/in-out', {
                templateUrl : 'views/drugstore/in-out.html',
                controller  : 'InOutController',
                controllerAs: 'vm'
            })

            .when('/reports', {
                templateUrl: 'views/report/reports.html',
                controller: 'reportsController'
            })

            .when('/documents', {
                templateUrl: 'views/documents.html',
                controller: 'docController'
            })

            .when('/contact', {
                templateUrl: 'views/contact.html',
                controller: 'contactController'
            });
    }]);

    scotchApp.run(['$rootScope', '$http', '$location', '$localStorage', '$cookies', function($rootScope, $http, $location, $localStorage, $cookies) { 
        // keep user logged in after page refresh
        // if ($localStorage.currentUser) {
        //     $http.defaults.headers.common.Authorization = 'Basic ' + $localStorage.currentUser.token;
        // }
        // redirect to login page if not logged in and trying to access a restricted page

        toastr.options.positionClass='toast-top-center';

        $rootScope.signining = false;

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            var restrictedPage = $.inArray($location.path(), ['/signin', '/signup', '/token-validation', '/contact', '/documents']) === -1;
            var loggedIn = $rootScope.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/signin');
            }
        });

       
    }]);

    // create the controller and inject Angular's $scope
    scotchApp.controller('homeController', function($scope) {
        // create a message to display in our view
        $scope.message = 'Everyone come and see how good I look!';
    });

    scotchApp.controller('reportsController', function($scope) {
        // create a message to display in our view
        $scope.message = 'Building...';
    });

    scotchApp.controller('docController', function($scope) {
        $scope.message = 'Building...';
    });

    scotchApp.controller('contactController', function($scope) {
        $scope.email = 'hnguyen.ini@gmail.com';
        $scope.phone = '+84 0909 284 595';
        $scope.name = 'Hien Nguyen';
    });

//  run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
//     function run($rootScope, $location, $cookies, $http) {
//         // keep user logged in after page refresh
//         $rootScope.globals = $cookies.getObject('globals') || {};
//         if ($rootScope.globals.currentUser) {
//             $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
//         }

//         $rootScope.$on('$locationChangeStart', function (event, next, current) {
//             // redirect to login page if not logged in and trying to access a restricted page
//             var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;
//             var loggedIn = $rootScope.globals.currentUser;
//             if (restrictedPage && !loggedIn) {
//                 $location.path('/login');
//             }
//         });
//     }