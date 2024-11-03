package org.sdi.productmanager.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
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
    private Date review_date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User reviewer;
}
