package com.pacesonline.identityservice.auth.logout;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pacesonline.identityservice.auth.refreshtoken.RefreshTokenRequest;
import com.pacesonline.identityservice.auth.refreshtoken.RefreshTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class LogoutController {
    
    private final RefreshTokenService refreshTokenService;

    public LogoutController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping ("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void logout(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeFamilyTokens(request.refreshToken());
    }
}
