(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('RuleDeleteController',RuleDeleteController);

    RuleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Rule'];

    function RuleDeleteController($uibModalInstance, entity, Rule) {
        var vm = this;

        vm.rule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Rule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
