package com.example.test.domain.request.company;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateCompanyDTO extends CreateCompanyDTO {

    public UpdateCompanyDTO(String name, String address, String logo, String description) {
        super(name, address, logo, description);
    }
}
