//= wrapped
//= require /angular/angular
//= require /angular/angular-route
//= require_self
//= require PersonController

(() => {
    let app = angular.module('miyagiApp', ['ngRoute']);

    app.config(($routeProvider) => {
        $routeProvider
            .when('/', {
                controller: 'PersonController',
                templateUrl: 'assets/miyagi/person.html'
            })
            .otherwise( { redirectTo: '/' } );
    });
})();
