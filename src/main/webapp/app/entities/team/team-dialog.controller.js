(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Team', 'Event', 'Proposal'];

    function TeamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Team, Event, Proposal) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.save = save;
        vm.events = Event.query();
        vm.proposals = Proposal.query({filter: 'team-is-null'});
        $q.all([vm.team.$promise, vm.proposals.$promise]).then(function() {
            if (!vm.team.proposalId) {
                return $q.reject();
            }
            return Proposal.get({id : vm.team.proposalId}).$promise;
        }).then(function(proposal) {
            vm.proposals.push(proposal);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackMeApp:teamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
