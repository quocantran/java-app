package com.example.test.domain.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CreateRoleDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Active is required")
    private Boolean active;

    @NotNull(message = "Permissions list cannot be null")
    @Size(min = 1, message = "Permissions list must contain at least one element")
    private List<Long> permissions;

    public String getName() {
        return name;
    }

    public CreateRoleDTO(@NotBlank(message = "Name is required") String name,
            @NotBlank(message = "Description is required") String description,
            @NotNull(message = "Active is required") Boolean active,
            @NotNull(message = "Permissions list cannot be null") @Size(min = 1, message = "Permissions list must contain at least one element") List<Long> permissions) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.permissions = permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Long> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Long> permissions) {
        this.permissions = permissions;
    }
}