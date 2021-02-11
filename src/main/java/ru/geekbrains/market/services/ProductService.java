package ru.geekbrains.market.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAllByFilterAndPageSorted(Specification<Product> spec, Optional<Integer> page, Optional<Integer> size, Optional<String> sortField, Optional<String> sortOrder) {
        if (sortField.isPresent() && sortOrder.isPresent()) {
            return productRepository.findAll(spec, PageRequest.of(
                    page.orElse(1) - 1, size.orElse(5),
                    Sort.by(Sort.Direction.fromString(sortOrder.get()), sortField.get()))
            );
        }
        return productRepository.findAll(spec, PageRequest.of(page.orElse(1) - 1, size.orElse(5)));
    }

    public Page<Product> findAllByFilterAndPage(Specification<Product> spec, int page, int size) {
        return productRepository.findAll(spec, PageRequest.of(page, size));
    }

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public void deleteById(Long id){
        productRepository.deleteById(id);
    }

    public void deleteAll(){
        productRepository.deleteAll();
    }

    public Optional<Product> findByTitle(String title) {
        return productRepository.findOneByTitle(title);
    }
}
