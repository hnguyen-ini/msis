
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
            .when('/login', {
                templateUrl : 'views/signin.html',
                controller  : 'contactController'
            })
            .when('/signup', {
                templateUrl : 'views/signup.html',
                controller  : 'contactController'
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
