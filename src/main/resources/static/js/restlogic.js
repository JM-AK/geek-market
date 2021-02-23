var app = angular.module('app', ['ngRoute', 'ngStorage']);
var contextPath = 'http://localhost:8181'

app.config(function ($routeProvider) {
    $routeProvider
        .when('/shop', {
            templateUrl: 'homepage.html'
        })
        .when('/shop/catalog', {
            templateUrl: 'catalog.html',
            controller: 'catalogController'
        })
        .when('/shop/add_or_edit_product', {
            templateUrl: 'add_or_edit_product.html',
            controller: 'addOrEditProductController'
        })
});

app.controller('mainController', function ($scope, $http, $localStorage) {
    $scope.tryToAuth = function () {
        $http.post(contextPath + '/auth', $scope.user)
            .then(function (response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.currentUser = { username: $scope.user.username, token: response.data.token };
                }
            });
    };

    $scope.tryToLogout = function () {
        delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
    };
});

app.controller('catalogController', function ($scope, $http, $localStorage) {
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    fillTable = function () {
        $http.get(contextPath + '/api/v1/catalog')
            .then(function (response) {
                $scope.ProductsList = response.data;
            });
    };

    fillTable();
});

app.controller('addOrEditProductController', function ($scope, $http, $routeParams, $localStorage) {
    const advertsPath = contextPath + '/api/v1/catalog';

    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    if ($routeParams.id != null) {
        $http.get(advertsPath + '/' + $routeParams.id).then(function (response) {
            $scope.productFromForm = response.data;
            console.log($scope.productFromForm);
        });
    }

    $scope.createOrUpdateProduct = function() {
        if($scope.productFromForm.id == null) {
            $http.post(contextPath + '/api/v1/catalog', $scope.productFromForm).then(function (response) {
                console.log(response);
                window.location.href = contextPath + '/index.html#/catalog';
                window.location.reload(true);
            });
        } else {
            $http.put(contextPath + '/api/v1/catalog', $scope.productFromForm).then(function (response) {
                console.log(response);
                window.location.href = contextPath + '/index.html#/catalog';
                window.location.reload(true);
            });
        }
    };
});