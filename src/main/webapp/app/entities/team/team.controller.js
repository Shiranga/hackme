(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Team'];

    function TeamController ($scope, $state, Team) {
        var vm = this;

        vm.teams = [];

        loadAll();

        function loadAll() {
            Team.query(function(result) {
                vm.teams = result;
                vm.searchQuery = null;
            });
        }
    }
})();
