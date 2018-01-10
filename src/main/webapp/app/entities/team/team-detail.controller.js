(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Team', 'Event', 'Proposal'];

    function TeamDetailController($scope, $rootScope, $stateParams, previousState, entity, Team, Event, Proposal) {
        var vm = this;

        vm.team = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackMeApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
