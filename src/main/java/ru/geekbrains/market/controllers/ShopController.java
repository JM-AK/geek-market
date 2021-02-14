package ru.geekbrains.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.market.entities.Category;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.CategoryService;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.Cart;
import ru.geekbrains.market.utils.ProductFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int PAGE_SIZE = 5;

    private ProductService productService;
    private Cart cart;
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String shopPage(Model model,
                           @RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam Map<String, String> params,
                           @RequestParam(name = "categories", required = false) List<Long> categoriesIds
    ) {
        Integer pageNumber = page.orElse(1);
        List<Category> categoriesFilter = null;
        if (categoriesIds != null) {
            categoriesFilter = categoryService.getCategoriesByIds(categoriesIds);
        }

        ProductFilter productFilter = new ProductFilter(params, categoriesFilter);
        Page<Product> products = productService.findAllByFilterAndPage(productFilter.getSpec(), pageNumber, PAGE_SIZE);

        model.addAttribute("products", products);
        model.addAttribute("filters", productFilter.getFilterDefinition());
        return "shop-page";
    }

    @GetMapping("/cart/add/{product_id}")
    public void addToCart(
            @PathVariable(name = "product_id") Long productId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        Product p = productService.findById(productId).orElseThrow(
                () -> new NotFoundException());
        cart.add(p);
        response.sendRedirect(request.getHeader("referer"));
    }
}
