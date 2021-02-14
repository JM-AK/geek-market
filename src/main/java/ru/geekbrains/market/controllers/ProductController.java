package ru.geekbrains.market.controllers;

import lombok.AllArgsConstructor;
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
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.entities.ProductImage;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.CategoryService;
import ru.geekbrains.market.services.ImageSaverService;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.ProductFilter;

import javax.validation.Valid;
import java.io.NotActiveException;
import java.util.Map;
import java.util.Optional;


/*
* Контроллер страницы управления Каталогом товаров
*
* */

@Controller
@RequestMapping("/catalog")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;
    private CategoryService categoryService;
    private ImageSaverService imageSaverService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setImageSaverService(ImageSaverService imageSaverService) {
        this.imageSaverService = imageSaverService;
    }

    @GetMapping
    public String indexProductPage(Model model,
                                   @RequestParam(name = "page") Optional<Integer> page,
                                   @RequestParam(name = "size") Optional<Integer> size,
                                   @RequestParam Map<String, String> params
                                   ) {
        logger.info("Product page update");
        ProductFilter productFilter = new ProductFilter(params,null);
        Page<Product> products = productService.findAllByFilterAndPage(productFilter.getSpec(), page, size);
        model.addAttribute("products", products);
        model.addAttribute("filters", productFilter.getFilterDefinition());
        return "catalog";
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
