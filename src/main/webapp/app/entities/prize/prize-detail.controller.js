(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('PrizeDetailController', PrizeDetailController);

    PrizeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Prize', 'Event'];

    function PrizeDetailController($scope, $rootScope, $stateParams, previousState, entity, Prize, Event) {
        var vm = this;

        vm.prize = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackMeApp:prizeUpdate', function(event, result) {
            vm.prize = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
