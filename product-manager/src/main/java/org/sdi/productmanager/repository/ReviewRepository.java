package org.sdi.productmanager.repository;

import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProduct(Product product, Pageable pageable);
    List<Review> findAllByProduct(Product product);
}
