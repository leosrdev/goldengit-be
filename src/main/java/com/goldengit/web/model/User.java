package com.goldengit.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@DynamicInsert
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    @Size(max = 50)
    private String name;

    @Column(unique = true, length = 50, nullable = false)
    @Size(max = 50)
    private String email;

    @Column(nullable = false)
    @Size(max = 128)
    private String password;

    @Column(nullable = false)
    private Integer active;
}
