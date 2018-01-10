(function() {
    'use strict';
    angular
        .module('hackMeApp')
        .factory('Rule', Rule);

    Rule.$inject = ['$resource'];

    function Rule ($resource) {
        var resourceUrl =  'api/rules/:id';

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
