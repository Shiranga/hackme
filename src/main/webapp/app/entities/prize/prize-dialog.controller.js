(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('PrizeDialogController', PrizeDialogController);

    PrizeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prize', 'Event'];

    function PrizeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Prize, Event) {
        var vm = this;

        vm.prize = entity;
        vm.clear = clear;
        vm.save = save;
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.prize.id !== null) {
                Prize.update(vm.prize, onSaveSuccess, onSaveError);
            } else {
                Prize.save(vm.prize, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackMeApp:prizeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
