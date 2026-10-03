package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateAppointmentRequest;
import com.lifecare.hospital.dto.request.LoginRequest;
import com.lifecare.hospital.dto.request.MessageRequest;
import com.lifecare.hospital.dto.request.RegisterRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.AppointmentResponse;
import com.lifecare.hospital.dto.response.AuthResponse;
import com.lifecare.hospital.dto.response.DoctorResponse;
import com.lifecare.hospital.dto.response.MessageResponse;
import com.lifecare.hospital.entity.AppointmentStatus;
import com.lifecare.hospital.entity.Doctor;
import com.lifecare.hospital.entity.DoctorStatus;
import com.lifecare.hospital.entity.Message;
import com.lifecare.hospital.entity.Role;
import com.lifecare.hospital.entity.User;
import com.lifecare.hospital.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Legacy Compatibility", description = "Backwards-compatible API adapter for existing React Admin & Patient frontends")
public class LegacyCompatibilityController {

    private final AuthService authService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final MessageService messageService;

    // --- USER / AUTH ENDPOINTS ---

    @PostMapping("/user/patient/register")
    @Operation(summary = "Legacy Patient Register Endpoint")
    public ResponseEntity<AuthResponse> legacyPatientRegister(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        request.setRole(Role.PATIENT);
        AuthResponse authResponse = authService.register(request);
        setCookie(response, "patientToken", authResponse.getToken());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/user/login")
    @Operation(summary = "Legacy User Login Endpoint")
    public ResponseEntity<AuthResponse> legacyLogin(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);
        String cookieName = "ADMIN".equalsIgnoreCase(authResponse.getUser().getRole().name()) ? "adminToken" : "patientToken";
        setCookie(response, cookieName, authResponse.getToken());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/user/admin/addnew")
    @Operation(summary = "Legacy Add New Admin Endpoint")
    public ResponseEntity<Map<String, Object>> legacyAddNewAdmin(@Valid @RequestBody RegisterRequest request) {
        authService.registerAdmin(request);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "New Admin Registered!");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/user/doctor/addnew")
    @Operation(summary = "Legacy Add New Doctor Endpoint with Avatar Upload")
    public ResponseEntity<Map<String, Object>> legacyAddNewDoctor(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("gender") String gender,
            @RequestParam("aadhar") String aadhar,
            @RequestParam("dob") String dob,
            @RequestParam("doctrDptmnt") String doctrDptmnt,
            @RequestParam(value = "doctrAvatar", required = false) MultipartFile avatar) {

        RegisterRequest regReq = RegisterRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .password(password)
                .gender(gender)
                .aadhar(aadhar)
                .dob(LocalDate.parse(dob))
                .role(Role.DOCTOR)
                .doctrDptmnt(doctrDptmnt)
                .doctorAvatar(avatar != null && !avatar.isEmpty() ? "/uploads/" + avatar.getOriginalFilename() : "/docHolder.jpg")
                .build();

        AuthResponse registered = authService.register(regReq);

        Doctor doc = doctorService.findOrCreateDoctorEntity(firstName, lastName, doctrDptmnt);
        doc.setEmail(email);
        doc.setPhone(phone);
        doc.setDoctorAvatar(regReq.getDoctorAvatar());

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "New Doctor Registered!");
        res.put("doctor", registered.getUser());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user/doctors")
    @Operation(summary = "Legacy Get All Doctors Endpoint")
    public ResponseEntity<Map<String, Object>> legacyGetAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("doctors", doctors);
        return ResponseEntity.ok(res);
    }

    @GetMapping({"/user/admin/me", "/user/patient/me"})
    @Operation(summary = "Legacy Get User Details Endpoint")
    public ResponseEntity<Map<String, Object>> legacyGetUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        AuthResponse current = authService.getCurrentUser(userDetails.getUsername());
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("user", current.getUser());
        return ResponseEntity.ok(res);
    }

    @GetMapping({"/user/admin/logout", "/user/patient/logout"})
    @Operation(summary = "Legacy Logout Endpoint")
    public ResponseEntity<Map<String, Object>> legacyLogout(HttpServletResponse response) {
        clearCookie(response, "adminToken");
        clearCookie(response, "patientToken");
        clearCookie(response, "token");
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Logged Out Successfully!");
        return ResponseEntity.ok(res);
    }

    // --- APPOINTMENT ENDPOINTS ---

    @PostMapping("/appointment/post")
    @Operation(summary = "Legacy Post Appointment Endpoint")
    public ResponseEntity<Map<String, Object>> legacyPostAppointment(@RequestBody CreateAppointmentRequest request,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : request.getEmail();
        AppointmentResponse appointment = appointmentService.createAppointment(request, email);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Appointment Sent Successfully!");
        res.put("appointment", appointment);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/appointment/getall")
    @Operation(summary = "Legacy Get All Appointments Endpoint")
    public ResponseEntity<Map<String, Object>> legacyGetAllAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("appointments", appointments);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/appointment/update/{id}")
    @Operation(summary = "Legacy Update Appointment Status Endpoint")
    public ResponseEntity<Map<String, Object>> legacyUpdateAppointmentStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        AppointmentStatus status = "Accepted".equalsIgnoreCase(statusStr) ? AppointmentStatus.CONFIRMED :
                                   "Rejected".equalsIgnoreCase(statusStr) ? AppointmentStatus.CANCELLED :
                                   AppointmentStatus.valueOf(statusStr.toUpperCase());

        AppointmentResponse updated = appointmentService.updateStatus(id, status);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Appointment Status Updated!");
        res.put("appointment", updated);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/appointment/delete/{id}")
    @Operation(summary = "Legacy Delete Appointment Endpoint")
    public ResponseEntity<Map<String, Object>> legacyDeleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Appointment Deleted!");
        return ResponseEntity.ok(res);
    }

    // --- MESSAGE ENDPOINTS ---

    @PostMapping("/message/send")
    @Operation(summary = "Legacy Send Message Endpoint")
    public ResponseEntity<Map<String, Object>> legacySendMessage(@Valid @RequestBody MessageRequest request) {
        messageService.sendMessageLegacy(request);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Message Send Successfully!");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/message/getall")
    @Operation(summary = "Legacy Get All Messages Endpoint")
    public ResponseEntity<Map<String, Object>> legacyGetAllMessages() {
        List<MessageResponse> messages = messageService.getAllMessages();
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("messages", messages);
        return ResponseEntity.ok(res);
    }

    private void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);

        Cookie generic = new Cookie("token", value);
        generic.setHttpOnly(true);
        generic.setPath("/");
        generic.setMaxAge(86400);
        response.addCookie(generic);
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
