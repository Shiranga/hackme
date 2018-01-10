(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('ProposalController', ProposalController);

    ProposalController.$inject = ['$scope', '$state', 'Proposal'];

    function ProposalController ($scope, $state, Proposal) {
        var vm = this;

        vm.proposals = [];

        loadAll();

        function loadAll() {
            Proposal.query(function(result) {
                vm.proposals = result;
                vm.searchQuery = null;
            });
        }
    }
})();
