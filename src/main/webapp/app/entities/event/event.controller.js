(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('EventController', EventController);

    EventController.$inject = ['$scope', '$state', 'Event'];

    function EventController ($scope, $state, Event) {
        var vm = this;

        vm.events = [];

        loadAll();

        function loadAll() {
            Event.query(function(result) {
                vm.events = result;
                vm.searchQuery = null;
            });
        }
    }
})();
