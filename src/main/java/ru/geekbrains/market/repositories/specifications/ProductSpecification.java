package ru.geekbrains.market.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.market.entities.Category;
import ru.geekbrains.market.entities.Product;



public class ProductSpecification {

    // where p.title like %titlePart%
    public static Specification<Product> titleContains(String word) {
        return (root, query, builder) -> builder.like(root.get("title"), "%" + word + "%");
    }

    // where p.price >= minPrice
    public static Specification<Product> priceGreaterOrEqualsThan(Double minPrice) {
        return ((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), minPrice));
    }

    //where p.price <= maxPrice
    public static Specification<Product> priceLessOrEqualsThan(Double maxPrice) {
        return ((root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), maxPrice));
    }

    //where p.category = category
    public static Specification<Product> categoryIs(Category category) {
        return (root, query, builder) -> builder.isMember(category, root.get("categories"));
    }
}
