
/**
 * @ngdoc overview
 * @name webappApp
 * @description
 * # webappApp
 *
 * Main module of the application.
 */
var scotchApp = angular.module('webappApp', ['ngRoute']);

    // configure our routes
    scotchApp.config(['$routeProvider',function($routeProvider) {
        $routeProvider

            // route for the home page
            .when('/', {
                templateUrl : 'views/main.html',
                controller  : 'MainCtrl'
            })

            .when('/home', {
                templateUrl : 'views/home.html',
                controller  : 'homeController'
            })

            // route for the about page
            .when('/about', {
                templateUrl : 'views/about.html',
                controller  : 'AboutCtrl'
            })

            // route for the contact page
            .when('/signin', {
                templateUrl : 'views/authen/signin.html',
                controller  : 'SigninController',
                controllerAs: 'vm'
            })
            .when('/signup', {
                templateUrl : 'views/authen/signup.html',
                controller  : 'SignupController',
                controllerAs: 'vm'
            });
    }]);

    // create the controller and inject Angular's $scope
    scotchApp.controller('homeController', function($scope) {
        // create a message to display in our view
        $scope.message = 'Everyone come and see how good I look!';
    });

    scotchApp.controller('mainController', function($scope) {
        // create a message to display in our view
        $scope.message = 'Everyone come and see how good I look!';
    });

    scotchApp.controller('aboutController', function($scope) {
        $scope.message = 'Look! I am an about page.';
    });

    scotchApp.controller('contactController', function($scope) {
        $scope.message = 'Contact us! JK. This is just a demo.';
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