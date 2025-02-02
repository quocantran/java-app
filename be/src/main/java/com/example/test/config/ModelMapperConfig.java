package com.example.test.config;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.test.domain.JobResume;
import com.example.test.domain.Role;
import com.example.test.domain.request.role.UpdateRoleDTO;
import com.example.test.domain.response.resume.ResponseJobResumeDTO;
import com.example.test.repository.PermissionRepository;

@Configuration
public class ModelMapperConfig {
    private final PermissionRepository permissionRepository;

    public ModelMapperConfig(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Bean
    protected ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true);

        modelMapper.addMappings(new PropertyMap<UpdateRoleDTO, Role>() {
            @Override
            protected void configure() {
                skip(destination.getPermissions());
            }
        });

        modelMapper.addMappings(new PropertyMap<Role, UpdateRoleDTO>() {
            @Override
            protected void configure() {
                skip(destination.getPermissions());
            }
        });

        modelMapper.addMappings(new PropertyMap<JobResume, ResponseJobResumeDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId().getJobId());
            }
        });

        return modelMapper;

    }
}
