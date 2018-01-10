(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('ProposalDeleteController',ProposalDeleteController);

    ProposalDeleteController.$inject = ['$uibModalInstance', 'entity', 'Proposal'];

    function ProposalDeleteController($uibModalInstance, entity, Proposal) {
        var vm = this;

        vm.proposal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Proposal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
