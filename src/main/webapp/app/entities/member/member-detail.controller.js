(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .controller('MemberDetailController', MemberDetailController);

    MemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Member', 'Team'];

    function MemberDetailController($scope, $rootScope, $stateParams, previousState, entity, Member, Team) {
        var vm = this;

        vm.member = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackMeApp:memberUpdate', function(event, result) {
            vm.member = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
