(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('ProposalDialogController', ProposalDialogController);

    ProposalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Proposal', 'Team', 'Category'];

    function ProposalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Proposal, Team, Category) {
        var vm = this;

        vm.proposal = entity;
        vm.clear = clear;
        vm.save = save;
        vm.teams = Team.query();
        vm.categories = Category.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.proposal.id !== null) {
                Proposal.update(vm.proposal, onSaveSuccess, onSaveError);
            } else {
                Proposal.save(vm.proposal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackMeApp:proposalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
