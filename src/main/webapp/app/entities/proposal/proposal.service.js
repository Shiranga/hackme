(function() {
    'use strict';
    angular
        .module('hackMeApp')
        .factory('Proposal', Proposal);

    Proposal.$inject = ['$resource'];

    function Proposal ($resource) {
        var resourceUrl =  'api/proposals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
