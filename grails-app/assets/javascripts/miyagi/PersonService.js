(() => {

let PersonService = function() {

	let selectedPerson = {};

	this.selectPerson = function(person) {
		console.log("Selected Person:", person);
		selectedPerson = person;
	}

	this.getSelectedPerson = function() {
		return selectedPerson;
	}

}

angular.module('miyagiApp').service('PersonService', PersonService);

})();
