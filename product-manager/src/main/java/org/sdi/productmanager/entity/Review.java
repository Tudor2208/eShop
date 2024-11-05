package org.sdi.productmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private Integer stars;
    @Column(nullable = false)
    private Date reviewDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User reviewer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;
}
