package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.MessageRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.MessageResponse;
import com.lifecare.hospital.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Endpoints for patient-to-department messaging and communication")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "Send a message to a department or general inquiry")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        MessageResponse response = messageService.sendMessage(request, email);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all messages (Admin / Doctor)")
    public ResponseEntity<List<MessageResponse>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get message by ID")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark message as read")
    public ResponseEntity<ApiResponse<MessageResponse>> markAsRead(@PathVariable Long id) {
        MessageResponse updated = messageService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Message marked as read", updated));
    }
}
