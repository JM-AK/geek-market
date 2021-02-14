package ru.geekbrains.market.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.market.entities.Category;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.entities.ProductImage;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.CategoryService;
import ru.geekbrains.market.services.ImageSaverService;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.Cart;
import ru.geekbrains.market.utils.ProductFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.NotActiveException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
    private ProductService productService;
    private CategoryService categoryService;
    private ImageSaverService imageSaverService;

    private Cart cart;

    private static final int PAGE_SIZE = 5;

    private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setImageSaverService(ImageSaverService imageSaverService) {
        this.imageSaverService = imageSaverService;
    }

    @GetMapping
    public String catalogPage(Model model,
                           @RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam Map<String, String> params,
                           @RequestParam(name = "categories", required = false) List<Long> categoriesIds
    ) {
        List<Category> categoriesFilter = null;
        if (categoriesIds != null) {
            categoriesFilter = categoryService.getCategoriesByIds(categoriesIds);
        }

        ProductFilter productFilter = new ProductFilter(params, categoriesFilter);
        Page<Product> products = productService.findAllByFilterAndPage(productFilter.getSpec(), page, Optional.of(PAGE_SIZE));

        model.addAttribute("products", products);
        model.addAttribute("filters", productFilter.getFilterDefinition());
        return "catalog-page";
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

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable(value = "id") Long id, Model model) throws NotActiveException {
        logger.info("Edit product with id {}", id);
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            product = new Product();
            product.setId(0L);
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit_product";
    }


    @PostMapping("/edit")
    public String editProduct(@Valid Product product, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file) {
        if (product.getId() == 0 && productService.findByTitle(product.getTitle()).isPresent()) {
            bindingResult.addError(new ObjectError("product.title", "Товар с таким названием уже существует")); // todo не отображает сообщение
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit_product";
        }

        if (!file.isEmpty()) {
            String pathToSavedImage = imageSaverService.saveFile(file);
            ProductImage productImage = new ProductImage();
            productImage.setPath(pathToSavedImage);
            productImage.setProduct(product);
            product.addImage(productImage);
        }
        productService.save(product);
        return "redirect:/catalog";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable(value = "id") Long id) {
        logger.info("Delete product with id {}", id);
        productService.deleteById(id);
        return "redirect:/catalog";
    }

    @DeleteMapping
    public void deleteAll(){
        productService.deleteAll();
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex){
        ModelAndView modelAndView = new ModelAndView("not found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}