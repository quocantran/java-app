package com.example.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.test.domain.Role;
import com.example.test.domain.User;
import com.example.test.service.UserService;
import com.example.test.core.error.ForbiddenException;
import com.example.test.domain.Permission;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();
        String method = request.getMethod();
        // if (path.equals("/api/v1/resumes") && method.equals("POST")) {
        // return true;
        // }
        // boolean isVerified = false;
        // String email =
        // SecurityContextHolder.getContext().getAuthentication().getName();
        // if (email != null && !email.isEmpty()) {
        // User user = userService.getUserByEmail(email);
        // if (user != null) {
        // Role role = user.getRole();
        // if (role != null) {
        // List<Permission> permissions = role.getPermissions();
        // for (Permission permission : permissions) {
        // String tmp[] = path.split("/");
        // if (Character.isDigit(tmp[tmp.length - 1].charAt(0))) {
        // String pathTmp = "";

        // for (int i = 0; i < tmp.length - 1; i++) {
        // pathTmp += tmp[i] + "/";
        // }
        // pathTmp += ":id";

        // if (permission.getApiPath().equals(pathTmp)
        // && permission.getMethod().equals(permission.getMethod())) {
        // isVerified = true;
        // break;
        // }
        // } else if (permission.getApiPath().equals(path) &&
        // permission.getMethod().equals(method)) {
        // isVerified = true;
        // break;
        // }
        // }

        // }
        // }
        // }
        // if (!isVerified) {
        // throw new ForbiddenException("You don't have permission to access this
        // resource");
        // }
        return true;
    }
}
