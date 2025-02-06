package com.example.test.domain.request.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePermissionDTO {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "api path is required")
    private String apiPath;
    @NotBlank(message = "method is required")
    private String method;

    @NotBlank(message = "module is required")
    private String module;


    public UpdatePermissionDTO(String name, String apiPath, String method, String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }
}
