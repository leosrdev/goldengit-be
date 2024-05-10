package com.goldengit.web.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Document(value = "users")
public class User {
    @Id
    private String id;
    @Size(max = 100)
    private String name;
    @Size(max = 100)
    private String email;
    @Size(max = 50)
    private String password;
    private String deleted;
}
