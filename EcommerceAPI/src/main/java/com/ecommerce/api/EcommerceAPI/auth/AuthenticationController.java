package com.ecommerce.api.EcommerceAPI.auth;

import com.ecommerce.api.EcommerceAPI.helpers.ApiResponse;
import com.ecommerce.api.EcommerceAPI.helpers.ResponseHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseHelper.ok("Registration successful. Please check your email for activation.");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = service.authenticate(request);
        return ResponseHelper.ok("Authentication successful", response);
    }

    @GetMapping("/activate-account")
    public ResponseEntity<ApiResponse<String>> confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
        return ResponseHelper.ok("Account activated successfully");
    }
}