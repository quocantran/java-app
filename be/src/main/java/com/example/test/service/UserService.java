package com.example.test.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.Role;
import com.example.test.domain.User;
import com.example.test.domain.request.user.RegisterUserDTO;
import com.example.test.domain.request.user.UpdatePasswordUserDTO;
import com.example.test.domain.request.user.UpdateUserDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.user.ResponseUserDTO;
import com.example.test.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CompanyService companyService;
    private final RoleService roleService;
    private final MailService mailService;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            CompanyService companyService, RoleService roleService, MailService mailService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
        this.mailService = mailService;
        this.modelMapper = modelMapper;
    }

    public ResponseUserDTO createUser(User user) throws BadRequestException {
        User checkUser = this.userRepository.findByEmail(user.getEmail());
        if (checkUser != null) {
            throw new BadRequestException("User already exists");
        }

        Company company = null;
        if (user.getCompany() != null) {
            company = this.companyService.getById(String.valueOf(user.getCompany().getId()));

            if (company == null) {
                throw new BadRequestException("Company not found");
            }
        }

        Role role = null;

        if (user.getRole() != null) {
            role = this.roleService.getRoleById(String.valueOf(user.getRole().getId()));

            if (role == null) {
                throw new BadRequestException("Role not found");
            }
        }

        user.setRole(role != null ? role : this.roleService.getRoleByName("NORMAL_USER"));
        user.setCompany(company);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        User res = this.userRepository.save(user);

        return res.convertResponseUserDto();
    }

    public RegisterUserDTO register(RegisterUserDTO entity) throws BadRequestException {
        if (this.userRepository.findByEmail(entity.getEmail()) != null) {
            throw new BadRequestException("User already exists");
        }

        Role role = this.roleService.getRoleByName("NORMAL_USER");

        User user = new User();
        user.setEmail(entity.getEmail());
        user.setName(entity.getName());
        user.setAddress(entity.getAddress());
        user.setAge(entity.getAge());
        user.setGender(entity.getGender());
        user.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
        user.setRole(role);

        this.userRepository.save(user);

        return entity;
    }

    public Long countUsers() {
        return this.userRepository.count();
    }

    public ResponseUserDTO updateUser(String id, @Valid UpdateUserDTO user) throws BadRequestException {
        try {
            long temp = Long.parseLong(id);
        } catch (Exception e) {
            throw new BadRequestException("Invalid user id");
        }
        User existingUser = userRepository.findById(Long.parseLong(id));

        if (existingUser == null) {
            throw new BadRequestException("User not found");
        }
        Company company = null;
        if (user.getCompany().getId() != null) {
            company = companyService.getById(String.valueOf(user.getCompany().getId()));
            if (company == null) {
                throw new BadRequestException("Company not found");
            }
            existingUser.setCompany(company);
        }

        if (user.getRole().getId() != null) {
            Role role = roleService.getRoleById(String.valueOf(user.getRole().getId()));
            if (role == null) {
                throw new BadRequestException("Role not found");
            }
            existingUser.setRole(role);
        }

        modelMapper.map(user, existingUser);
        existingUser.setId(Long.parseLong(id));
        existingUser.setCompany(company);
        User updatedUser = userRepository.save(existingUser);
        return updatedUser.convertResponseUserDto();
    }

    public void deleteUser(String id) throws BadRequestException {
        try {
            long temp = Long.parseLong(id);
        } catch (Exception e) {
            throw new BadRequestException("Invalid user id");
        }
        User user = this.userRepository.findById(Long.parseLong(id));
        if (user == null) {
            throw new BadRequestException("User not found");
        }

        this.userRepository.deleteById(Long.parseLong(id));
    }

    public ResponsePaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> user = this.userRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(user.getNumber() + 1);
        meta.setPageSize(user.getSize());

        meta.setPages(user.getTotalPages());
        meta.setTotal(user.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        Page<ResponseUserDTO> userDTO = user.map(User::convertResponseUserDto);
        resultPaginationDTO.setResult(userDTO.getContent());
        return resultPaginationDTO;
    }

    public ResponseUserDTO getUserById(long id) throws BadRequestException {
        User user = this.userRepository.findById(id);
        if (user == null) {
            throw new BadRequestException("User not found");
        }
        return user.convertResponseUserDto();
    }

    public User getUserByEmail(String username) {

        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String email, String refreshToken) {
        User currUser = this.getUserByEmail(email);
        if (currUser != null) {
            currUser.setRefreshToken(refreshToken);
            this.userRepository.save(currUser);
        }
    }

    public void resetPassword(String email) {
        String newPassword = UUID.randomUUID().toString().substring(0, 8);

        this.mailService.sendNewPassword(newPassword, email);
    }

    public void updateUserPassword(UpdatePasswordUserDTO entity) throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = this.getUserByEmail(email);

        if (currentUser == null) {
            throw new BadRequestException("User not found");
        }

        if (!bCryptPasswordEncoder.matches(entity.getPassword(), currentUser.getPassword())) {
            throw new BadRequestException("Password incorrect");
        }

        if (!entity.getNewPassword().equals(entity.getRepeatedPassword())) {
            throw new BadRequestException("Repeated password not match");
        }

        currentUser.setPassword(bCryptPasswordEncoder.encode(entity.getNewPassword()));

        this.userRepository.save(currentUser);
    }

    public Long countUserByWeek() {
        return this.userRepository.countUsersPast1Week();
    }

    public Long countUserByMonth() {
        return this.userRepository.countUsersPast1Month();
    }

    public Long countUserByYear() {
        return this.userRepository.countUsersPast1Year();
    }

    public Long countTotalUser() {
        return this.userRepository.count();
    }
}
