(function() {
    'use strict';
    angular
        .module('hackMeApp')
        .factory('Prize', Prize);

    Prize.$inject = ['$resource'];

    function Prize ($resource) {
        var resourceUrl =  'api/prizes/:id';

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
