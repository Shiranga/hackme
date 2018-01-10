(function() {
    'use strict';

    angular
        .module('hackMeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prize', {
            parent: 'entity',
            url: '/prize',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackMeApp.prize.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prize/prizes.html',
                    controller: 'PrizeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prize');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prize-detail', {
            parent: 'entity',
            url: '/prize/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackMeApp.prize.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prize/prize-detail.html',
                    controller: 'PrizeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prize');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Prize', function($stateParams, Prize) {
                    return Prize.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prize',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prize-detail.edit', {
            parent: 'prize-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prize/prize-dialog.html',
                    controller: 'PrizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prize', function(Prize) {
                            return Prize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prize.new', {
            parent: 'prize',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prize/prize-dialog.html',
                    controller: 'PrizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                place: null,
                                prize: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prize', null, { reload: 'prize' });
                }, function() {
                    $state.go('prize');
                });
            }]
        })
        .state('prize.edit', {
            parent: 'prize',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prize/prize-dialog.html',
                    controller: 'PrizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prize', function(Prize) {
                            return Prize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prize', null, { reload: 'prize' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prize.delete', {
            parent: 'prize',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prize/prize-delete-dialog.html',
                    controller: 'PrizeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prize', function(Prize) {
                            return Prize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prize', null, { reload: 'prize' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
