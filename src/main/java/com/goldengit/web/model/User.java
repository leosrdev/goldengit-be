package com.goldengit.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    @Size(max = 100)
    private String name;

    @Column(unique = true, length = 100, nullable = false)
    @Size(max = 100)
    private String email;

    @Column(nullable = false)
    @Size(max = 50)
    private String password;

    private Date deleted;
}
