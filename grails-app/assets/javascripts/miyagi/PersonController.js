(() => {

let DialogController = function($scope, $mdDialog) {
	$scope.states = ['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY'];

	$scope.hide = function() {
		$mdDialog.hide($scope.user);
	};
	
	$scope.cancel = function() {
		$mdDialog.cancel();
	};
}

let PersonController = function($scope, $mdDialog, $mdToast, $http) {
	$scope.people = [];
	$scope.months = ['Jan', 'Feb', 'Mar', 'Apr',  'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

	function showToast(message) {
		$mdToast.show(
			$mdToast.simple()
				.textContent(message)
				.hideDelay(3000)
		);
	}
	
	$scope.showAdvanced = function(event) {
		$mdDialog.show({
			controller: DialogController,
			templateUrl: 'assets/miyagi/personDialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose: true,
		}).then(person => {
			console.log('Submitted Person:', person);
			$http.post('person', person)
			.then(response => {
				showToast('Successfully added person!');
				loadPeople();
			}, () => {
				showToast('Failed to add person.');
			});
		}, () => {
			showToast('Failed');
		});
	};

	function loadPeople() {
		$http.get('person')
			.then(response => {
				$scope.people = response.data;
				console.log(response);
			}, () => {
				console.error(response);
			});
	}

	loadPeople();
}

angular.module('miyagiApp').controller('PersonController', PersonController);

})();
