package org.sdi.productmanager.dto;

import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.entity.Category;
import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.Review;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    public Product toEntity(CreateProductRequest request, Category category) {
        return Product.builder()
                .category(category)
                .price(request.getPrice())
                .specifications(request.getSpecifications())
                .title(request.getTitle())
                .stock(request.getStock())
                .postDate(new Date())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        double rating = computeProductRating(product);
        String formattedRating = String.format("%.2f", rating);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(product.getPostDate());

        return ProductResponse.builder()
                .id(product.getId())
                .category(product.getCategory())
                .price(product.getPrice())
                .specifications(product.getSpecifications())
                .title(product.getTitle())
                .stock(product.getStock())
                .rating(Double.valueOf(formattedRating))
                .nrOfReviews(getNrOfProductReviews(product))
                .postDate(formattedDate)
                .build();
    }

    private double computeProductRating(Product product) {
        List<Review> reviews = product.getReviews();
        if (reviews == null) {
            return 0;
        }
        OptionalDouble average = reviews.stream()
                .mapToDouble(Review::getStars)
                .average();
        return average.orElse(0.0);
    }

    private int getNrOfProductReviews(Product product) {
        List<Review> reviews = product.getReviews();
        if (reviews == null) {
            return 0;
        }
        return reviews.size();
    }
}
