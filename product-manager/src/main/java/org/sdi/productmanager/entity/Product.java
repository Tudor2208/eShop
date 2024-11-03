package org.sdi.productmanager.entity;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Double price;
    @Column(columnDefinition = "CLOB", nullable = false)
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> specifications;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;
}
