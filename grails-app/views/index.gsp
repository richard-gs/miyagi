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
</head>
<body>
	<p>TITLE</p>
	<ng-view></ng-view>
	<asset:javascript src="/miyagi/index.js" />
</body>
</html>
