(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('RuleDetailController', RuleDetailController);

    RuleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Rule', 'Event'];

    function RuleDetailController($scope, $rootScope, $stateParams, previousState, entity, Rule, Event) {
        var vm = this;

        vm.rule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackMeApp:ruleUpdate', function(event, result) {
            vm.rule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
