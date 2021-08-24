(() => {

let DialogController = function($scope, $mdDialog) {
	$scope.states = ['AL','AK','AZ','AR','CA','CO','CT','DC','DE','FL','GA','HI','ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR','PA','PR','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY'];
	$scope.months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
	$scope.days = getDays();

	$scope.hide = function() {
		$mdDialog.hide($scope.user);
	};
	
	$scope.cancel = function() {
		$mdDialog.cancel();
	};

	function getDays() {
		let days = [];
		for (let i = 0; i < 31; i++) {
			days.push(i+1);
		}
		return days;
	}
}

let PersonController = function($scope, $mdDialog, $mdToast, $http) {
	$scope.people = [];
	$scope.months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
	$scope.monthFreq = [];

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
		}).then(person => {
			console.log('Submitted Person:', person);
			person.dob = `${person.year}-${person.month}-${person.day}`;
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
				countMonths();
				console.log(response);
			}, () => {
				console.error(response);
			});
	}

	function countMonths() { // dob months frequencies
		resetMonthFreq();
		for (let person of $scope.people) {
			console.log(person.dob, person.dob.constructor.name);
			let date = new Date(Date.parse(person.dob));
			let monthIndex = date.getMonth();
			$scope.monthFreq[monthIndex]++;
		}
	}

	function resetMonthFreq() {
		$scope.monthFreq = [0,0,0,0,0,0,0,0,0,0,0,0];
	}

	loadPeople();
}

angular.module('miyagiApp').controller('PersonController', PersonController);

})();
