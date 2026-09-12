package com.pacesonline.identityservice.auth.logout;


import com.pacesonline.identityservice.auth.refreshtoken.RefreshTokenService;
import com.pacesonline.identityservice.config.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogoutController.class)
@Import(SecurityConfiguration.class)
class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void logsOutWithValidRefreshToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "valid-refresh-token"
                                }
                                """))
                .andExpect(status().isNoContent());

        verify(refreshTokenService)
                .revokeFamilyForToken("valid-refresh-token");
    }

    @Test
    void rejectsBlankRefreshToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(refreshTokenService);
    }

    @Test
    void rejectsMissingRequestBody() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(refreshTokenService);
    }
}