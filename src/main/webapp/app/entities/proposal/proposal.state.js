(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('proposal', {
            parent: 'entity',
            url: '/proposal',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackMeApp.proposal.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/proposal/proposals.html',
                    controller: 'ProposalController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('proposal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('proposal-detail', {
            parent: 'entity',
            url: '/proposal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackMeApp.proposal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/proposal/proposal-detail.html',
                    controller: 'ProposalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('proposal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Proposal', function($stateParams, Proposal) {
                    return Proposal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'proposal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('proposal-detail.edit', {
            parent: 'proposal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proposal/proposal-dialog.html',
                    controller: 'ProposalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Proposal', function(Proposal) {
                            return Proposal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('proposal.new', {
            parent: 'proposal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proposal/proposal-dialog.html',
                    controller: 'ProposalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                filePath: null,
                                comment: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('proposal', null, { reload: 'proposal' });
                }, function() {
                    $state.go('proposal');
                });
            }]
        })
        .state('proposal.edit', {
            parent: 'proposal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proposal/proposal-dialog.html',
                    controller: 'ProposalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Proposal', function(Proposal) {
                            return Proposal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('proposal', null, { reload: 'proposal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('proposal.delete', {
            parent: 'proposal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proposal/proposal-delete-dialog.html',
                    controller: 'ProposalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Proposal', function(Proposal) {
                            return Proposal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('proposal', null, { reload: 'proposal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
