package com.goldengit.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    @Size(min = 5, max = 50)
    @Email
    private String email;
    @NotNull
    @Size(min = 6, max = 30)
    private String password;
}
