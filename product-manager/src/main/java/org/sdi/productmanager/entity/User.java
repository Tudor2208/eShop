package org.sdi.productmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name="users")
public class User {

    @Id
    private String email;
    @JsonIgnore
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews;
}
