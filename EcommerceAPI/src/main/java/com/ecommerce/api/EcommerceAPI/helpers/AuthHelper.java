package com.ecommerce.api.EcommerceAPI.helpers;

import com.ecommerce.api.EcommerceAPI.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public class AuthHelper {

    /**
     * Retrieves the currently authenticated user from the SecurityContext.
     * Assumes that the principal in the Authentication object is an instance of User.
     * @return The authenticated User object, or null if no user is authenticated or principal is not a User.
     */
    public static User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Retrieves the ID of the currently authenticated user.
     *
     * @return The UUID of the authenticated user, or null if no user is authenticated.
     */
    public static Integer getCurrentAuthenticatedUserId() {
        User user = getCurrentAuthenticatedUser();
        return user != null ? user.getId() : null;
    }

    /**
     * Checks if a user is currently authenticated.
     * @return true if a user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Checks if the currently authenticated user has a specific role.
     * @param role The role to check (e.g., "ADMIN", "USER"). Note: Roles are typically stored with a "ROLE_" prefix in Spring Security (e.g., "ROLE_ADMIN").
     * @return true if the user has the specified role, false otherwise.
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    /**
     * Checks if the currently authenticated user is an administrator.
     * @return true if the user has the ROLE_ADMIN, false otherwise.
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Checks if the currently authenticated user is a regular user (not necessarily admin).
     * @return true if the user has the ROLE_USER, false otherwise.
     */
    public static boolean isUser() {
        return hasRole("USER");
    }
}
