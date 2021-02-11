package ru.geekbrains.market.utils;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.repositories.specifications.ProductSpecification;

import java.util.Map;

@Getter
public class ProductFilter {
    @Getter
    private Specification<Product> spec;
    @Getter
    private String filterDefinition;

    public ProductFilter(Map<String, String> params) {
        StringBuilder filterDefinitionBuilder = new StringBuilder();
        spec = Specification.where(null);

        String filterTitle = params.get("title");
        if (filterTitle != null && !filterTitle.isBlank()) {
            spec = spec.and(ProductSpecification.titleContains(filterTitle));
            filterDefinitionBuilder.append("&title=").append(filterTitle);
        }

        if (params.containsKey("min_price") && !params.get("min_price").isBlank()) {
            Double minPrice = Double.valueOf(params.get("min_price"));
            spec = spec.and(ProductSpecification.priceGreaterOrEqualsThan(minPrice));
            filterDefinitionBuilder.append("&min_price=").append(minPrice);
        }

        if (params.containsKey("max_price") && !params.get("max_price").isBlank()) {
            Double maxPrice = Double.valueOf(params.get("max_price"));
            spec = spec.and(ProductSpecification.priceLessOrEqualsThan(maxPrice));
            filterDefinitionBuilder.append("&max_price=").append(maxPrice);
        }

        filterDefinition = filterDefinitionBuilder.toString();
    }

    public Specification<Product> getSpec() {
        return spec;
    }

    public String getFilterDefinition() {
        return filterDefinition;
    }
}
