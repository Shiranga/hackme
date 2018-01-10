(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('RuleDialogController', RuleDialogController);

    RuleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Rule', 'Event'];

    function RuleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Rule, Event) {
        var vm = this;

        vm.rule = entity;
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
            if (vm.rule.id !== null) {
                Rule.update(vm.rule, onSaveSuccess, onSaveError);
            } else {
                Rule.save(vm.rule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackMeApp:ruleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
