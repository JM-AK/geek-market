package ru.geekbrains.market.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.exceptions.ProductNotFoundException;
import ru.geekbrains.market.services.ProductService;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/catalog")
@Api("Set of endpoints for CRUD operations for Products")
public class RestProductsController {
    private ProductService productService;

    @Autowired
    public RestProductsController(ProductService productsService) {
        this.productService = productsService;
    }

    @GetMapping(produces = "application/json")
    @ApiOperation("Returns list of all products")
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ApiOperation("Returns one product by id")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "demo", type = "String", required = false, paramType = "query")
//    })
    public ResponseEntity<?> getOneProduct(@PathVariable @ApiParam("Id of the product to be requested. Cannot be empty") Long id) {
        if (!productService.existsById(id)) {
            throw new ProductNotFoundException("Product not found, id: " + id);
        }
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation("Removes all products")
    public void deleteAllProducts() {
        productService.deleteAll();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Removes one product by id")
    public void deleteOneProducts(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates a new product")
    public Product saveNewProduct(@RequestBody Product product) {
        if (product.getId() != null) {
            product.setId(null);
        }
        return productService.save(product);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation("Modifies an existing product")
    public ResponseEntity<?> modifyProduct(@RequestBody Product product) {
        if (product.getId() == null || !productService.existsById(product.getId())) {
            throw new ProductNotFoundException("Product not found, id: " + product.getId());
        }
        if (product.getPrice() < 0.0) {
            return new ResponseEntity<>("Product's price can not be negative", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(ProductNotFoundException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }
}