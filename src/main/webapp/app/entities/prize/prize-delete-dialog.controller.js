(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('PrizeDeleteController',PrizeDeleteController);

    PrizeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prize'];

    function PrizeDeleteController($uibModalInstance, entity, Prize) {
        var vm = this;

        vm.prize = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prize.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
