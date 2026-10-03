package com.lifecare.hospital.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hospital Management System REST API")
                        .description("Enterprise RESTful APIs for Hospital Management System covering Walk-In, Emergency, Appointment, Pharmacy, Beds, Consultations, Invoicing, Messaging, Dashboard, and Reports.")
                        .version("1.0.0")
                        .contact(new Contact().name("Hospital Engineering Team").email("engineering@hospital.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder().group("0. All APIs").pathsToMatch("/api/**").build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("1. Authentication").pathsToMatch("/api/auth/**").build();
    }

    @Bean
    public GroupedOpenApi patientsApi() {
        return GroupedOpenApi.builder().group("2. Patients").pathsToMatch("/api/patients/**").build();
    }

    @Bean
    public GroupedOpenApi doctorsApi() {
        return GroupedOpenApi.builder().group("3. Doctors").pathsToMatch("/api/doctors/**").build();
    }

    @Bean
    public GroupedOpenApi departmentsApi() {
        return GroupedOpenApi.builder().group("4. Departments").pathsToMatch("/api/departments/**").build();
    }

    @Bean
    public GroupedOpenApi walkInApi() {
        return GroupedOpenApi.builder().group("5. Walk-In").pathsToMatch("/api/walk-ins/**").build();
    }

    @Bean
    public GroupedOpenApi emergencyApi() {
        return GroupedOpenApi.builder().group("6. Emergency").pathsToMatch("/api/emergency/**").build();
    }

    @Bean
    public GroupedOpenApi appointmentsApi() {
        return GroupedOpenApi.builder().group("7. Appointments").pathsToMatch("/api/appointments/**").build();
    }

    @Bean
    public GroupedOpenApi consultationsApi() {
        return GroupedOpenApi.builder().group("8. Consultations").pathsToMatch("/api/consultations/**").build();
    }

    @Bean
    public GroupedOpenApi prescriptionsApi() {
        return GroupedOpenApi.builder().group("9. Prescriptions").pathsToMatch("/api/prescriptions/**").build();
    }

    @Bean
    public GroupedOpenApi pharmacyApi() {
        return GroupedOpenApi.builder().group("10. Pharmacy").pathsToMatch("/api/medicines/**", "/api/inventory/**", "/api/pharmacy/**").build();
    }

    @Bean
    public GroupedOpenApi admissionsApi() {
        return GroupedOpenApi.builder().group("11. Admissions").pathsToMatch("/api/admissions/**").build();
    }

    @Bean
    public GroupedOpenApi bedsApi() {
        return GroupedOpenApi.builder().group("12. Beds").pathsToMatch("/api/beds/**").build();
    }

    @Bean
    public GroupedOpenApi billingApi() {
        return GroupedOpenApi.builder().group("13. Billing").pathsToMatch("/api/billing/**").build();
    }

    @Bean
    public GroupedOpenApi messagesApi() {
        return GroupedOpenApi.builder().group("14. Messages").pathsToMatch("/api/messages/**").build();
    }

    @Bean
    public GroupedOpenApi dashboardApi() {
        return GroupedOpenApi.builder().group("15. Dashboard").pathsToMatch("/api/dashboard/**").build();
    }

    @Bean
    public GroupedOpenApi reportsApi() {
        return GroupedOpenApi.builder().group("16. Reports").pathsToMatch("/api/reports/**").build();
    }
}
