angular.module('app').controller('createOrderController', function ($scope, $http, $location) {
    const contextPath = 'http://localhost:8181/shop';

    fillTable = function () {
        $http.get(contextPath + '/api/v1/cart')
            .then(function (response) {
                $scope.CartList = response.data;
            });
    };

    $scope.confirmOrder = function () {
        console.log($scope.userinfo.address);
        $http({
            url: contextPath + '/api/v1/orders/confirm',
            method: "POST",
            params: {address: $scope.userinfo.address}
        }).then(function () {
            $location.path('/create_order_result');
        });
    }

    fillTable();
});