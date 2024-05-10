package com.goldengit.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull
    @Size(max = 100)
    private String name;
    @NotNull
    @Size(max = 100)
    private String email;
    @NotNull
    @Size(max = 30)
    private String password;
}
