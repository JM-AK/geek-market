angular.module('app').controller('catalogController', function ($scope, $http) {
    const contextPath = 'http://localhost:8181/shop';

    fillTable = function () {
        $http.get(contextPath + '/api/v1/catalog')
            .then(function (response) {
                $scope.ProductsList = response.data;
            });
    };

    // $scope.submitCreateNewBook = function () {
    //     $http.post(contextPath + '/api/v1/books', $scope.newBook)
    //         .then(function (response) {
    //             $scope.BooksList.push(response.data);
    //         });
    // };
    //
    // $scope.applyFilter = function () {
    //     $http({
    //         url: contextPath + '/api/v1/books',
    //         method: "GET",
    //         params: {book_title: $scope.bookFilter.title}
    //     }).then(function (response) {
    //         $scope.BooksList = response.data;
    //     });
    // }

    $scope.addToCartFunction = function(product) {
        $http({
            url: contextPath + '/api/v1/cart/add/' + product.id,
            method: "GET"
        }).then(function (response) {
            console.log('added');
        });
    }

    fillTable();
});