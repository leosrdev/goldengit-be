package com.goldengit.web.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Project {
    @Id
    @Column(unique = true, length = 36, nullable = false)
    private String uuid;
    @Column(name = "full_name", unique = true, length = 100, nullable = false)
    private String fullName;
}
