package com.lifecare.hospital.security;

import com.lifecare.hospital.controller.DashboardController;
import com.lifecare.hospital.service.DashboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    // ==========================================
    // 9. Unauthorized users cannot access admin APIs
    // ==========================================
    @Test
    @DisplayName("Test 9a: Anonymous unauthenticated user is rejected (403/401) on admin dashboard API")
    void unauthenticatedUserCannotAccessAdminApis() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "patient@example.com", roles = {"PATIENT"})
    @DisplayName("Test 9b: Patient user cannot access admin APIs (403 Forbidden)")
    void patientCannotAccessAdminApis() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin@hms.com", roles = {"ADMIN"})
    @DisplayName("Test 9c: Admin user can successfully access admin dashboard API")
    void adminCanAccessAdminApis() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk());
    }
}
