package org.sdi.productmanager.entity;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Product> titleContains(String keywords) {
        return (root, query, criteriaBuilder) -> {
            if (keywords == null || keywords.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(root.get("title"), "%" + keywords + "%");
        };
    }
}
