package ru.geekbrains.market.controllers.thymeleaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.market.entities.Category;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.entities.ProductImage;
import ru.geekbrains.market.entities.dto.websocket.Greeting;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.CategoryService;
import ru.geekbrains.market.services.ImageSaverService;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.ProductFilter;

import java.io.NotActiveException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/catalog")
@Profile("thymeleaf")
public class CatalogController {
    private ProductService productService;
    private CategoryService categoryService;
    private ImageSaverService imageSaverService;
    private CatalogControllerWS catalogControllerWS;

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
        return "edit-product";
    }


    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            String pathToSavedImage = imageSaverService.saveFile(file);
            ProductImage productImage = new ProductImage();
            productImage.setPath(pathToSavedImage);
            productImage.setProduct(product);
            product.addImage(productImage);
        }
        if(product.getId().equals(0L)) {
            new Thread(()->{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catalogControllerWS.sendMessage("/topic/add_to_catalog",
                        new Greeting("Добавлен новый товар: " + product.getTitle()));
            }).start();
        }
        productService.save(product);
        return "redirect:/catalog";
    }

    //ToDo узнать, почему не работал DeleteMapping
    @PostMapping("delete/{id}")
    public String deleteById(@PathVariable(value = "id") Long id) {
        logger.info("Delete product with id {}", id);
        System.out.println("Delete product with id {}" + id);
        if (productService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Product with id: " + id + " doesn't exists (for delete)");
        }
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