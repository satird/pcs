package ru.satird.pcs.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Category;

public class AdSpecification {

    private static final String PREMIUM = "premium";

    private AdSpecification() {
        throw new IllegalStateException("AdSpecification class");
    }

    public static Specification<Ad> titleLike(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or
                (criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"),
                        criteriaBuilder.isTrue(root.get(PREMIUM)));
    }

    public static Specification<Ad> textLike(String text) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or
                (criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), "%" + text.toLowerCase() + "%"),
                        criteriaBuilder.isTrue(root.get(PREMIUM)));
    }

    public static Specification<Ad> priceGreaterThanOrEqual(Float value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or
                (criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value),
                        criteriaBuilder.isTrue(root.get(PREMIUM)));
    }

    public static Specification<Ad> priceLesserThanOrEqual(Float value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or
                (criteriaBuilder.lessThanOrEqualTo(root.get("price"), value),
                        criteriaBuilder.isTrue(root.get(PREMIUM)));
    }

    public static Specification<Ad> belongsToCategory(Category category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or
                (criteriaBuilder.equal(root.get("category"), category),
                        criteriaBuilder.isTrue(root.get(PREMIUM)));
    }
}
