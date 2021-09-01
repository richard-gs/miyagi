//= wrapped
//= require_self
//= require PersonService
//= require PersonController

(() => {
    let app = angular.module('miyagiApp', ['ngRoute', 'ngMaterial']);

    app.config(($routeProvider) => {
        $routeProvider
            .when('/', {
                controller: 'PersonController',
                templateUrl: 'assets/miyagi/person.html'
            })
            .otherwise( { redirectTo: '/' } );
    });
})();
