(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('RuleController', RuleController);

    RuleController.$inject = ['$scope', '$state', 'Rule'];

    function RuleController ($scope, $state, Rule) {
        var vm = this;

        vm.rules = [];

        loadAll();

        function loadAll() {
            Rule.query(function(result) {
                vm.rules = result;
                vm.searchQuery = null;
            });
        }
    }
})();
