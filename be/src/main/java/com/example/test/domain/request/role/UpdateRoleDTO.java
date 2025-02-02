package com.example.test.domain.request.role;

import java.util.List;

public class UpdateRoleDTO extends CreateRoleDTO {

    public UpdateRoleDTO(String name, String description, Boolean active, List<Long> permissions) {
        super(name, description, active, permissions);
    }
}
