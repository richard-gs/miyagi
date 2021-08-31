(() => {

let DialogController = function($scope, $mdDialog) {
	$scope.states = ['AL','AK','AZ','AR','CA','CO','CT','DC','DE','FL','GA','HI','ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR','PA','PR','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY'];
	$scope.months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
	$scope.days = getDays();
	$scope.alertText = false;

	$scope.hide = function() {
		$scope.alertText = 
			   !$scope.user                // || $scope.user.$invalid
			|| !$scope.user.firstName      // || $scope.user.firstName.$invalid
			|| !$scope.user.lastName       // || $scope.user.lastName.$invalid
			|| !$scope.user.month          // || $scope.user.month.$invalid
			|| !$scope.user.day            // || $scope.user.day.$invalid
			|| !$scope.user.year           // || $scope.user.year.$invalid
			|| !$scope.user.address        // || $scope.user.address.$invalid
			|| !$scope.user.address.street // || $scope.user.address.street.$invalid
			|| !$scope.user.address.city   // || $scope.user.address.city.$invalid
			|| !$scope.user.address.state  // || $scope.user.address.state.$invalid
			|| !$scope.user.address.zip    // || $scope.user.address.zip.$invalid;

		if (!$scope.alertText) {
			$mdDialog.hide($scope.user);
		} else {
			$scope.alertText = "Please verify all fields are filled out correctly.";
		}
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

	// 1725 Desales St NW, Washington, DC 20036
	const GS_HQ_LAT =  38.9049526;
	const GS_HQ_LON = -77.0395802;

	function showToast(message) {
		$mdToast.show(
			$mdToast.simple()
				.textContent(message)
				.hideDelay(3000)
		);
	}
	
	$scope.addPerson = function(event) {
		$mdDialog.show({
			controller: DialogController,
			templateUrl: 'assets/miyagi/personDialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
		}).then(person => {
			console.log('Submitted Person:', person);
			person.dob = `${person.year}-${person.month}-${person.day}`;
			$http.post(
				'person', person
			).then(() => {
				showToast('Successfully added person!');
				loadPeople();
			}).catch(error => {
				showToast('Failed to add person.');
				console.error(error);
			}); 
		}).catch(err => {
			console.error("Modal Closed", err);
		})
	};

	function loadPeople() {
		$http.get(
			'person'
		).then(response => {
			$scope.people = response.data;
			countMonths();
			calcDistances($scope.people);
			console.log(response);
		}).catch(error => {
			console.error(error);
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

	$scope.search = function() {
		$http.get('person/search', {
			params: {
				name:      $scope.filter.name,
				startDate: jsDateToShortDate($scope.filter.startDate),
				endDate:   jsDateToShortDate($scope.filter.endDate)
			}
		}).then(response => {
			$scope.people = response.data;
			console.log(response);
		}).catch(error => {
			console.error(error);
		});
	}

	function jsDateToShortDate(d) {
		if (!d) { return d; }
		return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()}`
	}

	$scope.reset = function() {
		$scope.filter.name = '';
		$scope.filter.startDate = '';
		$scope.filter.endDate = '';
		loadPeople();
	}

	// https://cloud.google.com/blog/products/maps-platform/how-calculate-distances-map-maps-javascript-api
	function haversine_distance(a_lat, a_lon, b_lat, b_lon) {
		var R = 3958.8; // Radius of the Earth in miles
		var rlat1 = a_lat * (Math.PI/180); // Convert degrees to radians
		var rlat2 = b_lat * (Math.PI/180); // Convert degrees to radians
		var difflat = rlat2-rlat1; // Radian difference (latitudes)
		var difflon = (a_lon - b_lon) * (Math.PI/180); // Radian difference (longitudes)
  
		var d = 2 * R * Math.asin(Math.sqrt(Math.sin(difflat/2)*Math.sin(difflat/2)+Math.cos(rlat1)*Math.cos(rlat2)*Math.sin(difflon/2)*Math.sin(difflon/2)));
		return d;
	}

	function googleMapsApi(address, callback) {
		const GOOGLE_MAPS_API_KEY = 'AIzaSyCOiSlF5NaJj4UuMhb3hmncLWX3GwLN-tY';
		console.log(" === CALLING GOOGLE MAPS API === ");
		$http.get('https://maps.googleapis.com/maps/api/geocode/json', {
			params: {
				address: address,
				key:     GOOGLE_MAPS_API_KEY
			}
		}).then(response => {
			console.log(response);
			console.log(response.data.results[0].geometry.location);
			callback(response);
		}).catch(error => {
			console.error(error);
		});
	}

	function calcDistances(people) {
		console.log('calc distances people:', people);
		for (let person of people) {
			console.log('CALC DISTANCE FOR:', person);
			let addr = person.address;
			let addressStr = `${addr.street}, ${addr.city}, ${addr.state} ${addr.zip}`;
			console.log('address', addressStr)
			if (hardcoded(person, addressStr)) { continue; }
			googleMapsApi(addressStr, (response) => {
				person.lat = response.data.results[0].geometry.location.lat;
				person.lon = response.data.results[0].geometry.location.lng;
				person.dist = haversine_distance(GS_HQ_LAT, GS_HQ_LON, person.lat, person.lon);
			});
		}
	}

	// hardcoded distances to limit number of google maps api calls
	function hardcoded(person, address) {
		if (address === '1600 Pennsylvania Avenue NW, Washington, DC 20500') {
			console.log('hardcoded');
			person.lat  =  38.8976633;
			person.lon  = -77.0365739
			person.dist = haversine_distance(GS_HQ_LAT, GS_HQ_LON, person.lat, person.lon);
			return true;
		}
		return false;
	}

	loadPeople();
}

angular.module('miyagiApp').controller('PersonController', PersonController);

})();
