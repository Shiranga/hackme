(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('ProposalDetailController', ProposalDetailController);

    ProposalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Proposal', 'Team', 'Category'];

    function ProposalDetailController($scope, $rootScope, $stateParams, previousState, entity, Proposal, Team, Category) {
        var vm = this;

        vm.proposal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackMeApp:proposalUpdate', function(event, result) {
            vm.proposal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
