package org.sdi.productmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name="categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
}
