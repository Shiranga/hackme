(function() {
    'use strict';
    angular
        .module('hackMeApp')
        .factory('Event', Event);

    Event.$inject = ['$resource', 'DateUtils'];

    function Event ($resource, DateUtils) {
        var resourceUrl =  'api/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDateTime = DateUtils.convertDateTimeFromServer(data.startDateTime);
                        data.endDateTime = DateUtils.convertDateTimeFromServer(data.endDateTime);
                        data.proposalDeadline = DateUtils.convertDateTimeFromServer(data.proposalDeadline);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
