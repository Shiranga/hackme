(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('PrizeController', PrizeController);

    PrizeController.$inject = ['$scope', '$state', 'Prize'];

    function PrizeController ($scope, $state, Prize) {
        var vm = this;

        vm.prizes = [];

        loadAll();

        function loadAll() {
            Prize.query(function(result) {
                vm.prizes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
