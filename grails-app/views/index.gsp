<!doctype html>
<html ng-app="miyagiApp">
<head>
	<title>Mr. Miyagi</title>
	<style type="text/css">
		[ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
			display: none !important;
		}
	</style>
	<asset:stylesheet src="application.css"/>

	<!-- Angular Material CSS now available via Google CDN; version 1.2.1 used here -->
	<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/angular_material/1.2.1/angular-material.min.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

</body>
</head>
<body>
	<ng-view></ng-view>

	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-aria.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-animate.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-messages.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-route.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angular_material/1.2.1/angular-material.min.js"></script>

	<asset:javascript src="/miyagi/index.js" />
</body>
</html>
