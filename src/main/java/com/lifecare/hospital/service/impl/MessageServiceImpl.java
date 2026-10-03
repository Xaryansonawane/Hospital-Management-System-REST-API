package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.MessageRequest;
import com.lifecare.hospital.dto.response.MessageResponse;
import com.lifecare.hospital.entity.Department;
import com.lifecare.hospital.entity.Message;
import com.lifecare.hospital.entity.Patient;
import com.lifecare.hospital.entity.User;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.DepartmentRepository;
import com.lifecare.hospital.repository.MessageRepository;
import com.lifecare.hospital.repository.PatientRepository;
import com.lifecare.hospital.repository.UserRepository;
import com.lifecare.hospital.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public MessageResponse sendMessage(MessageRequest request, String userEmail) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String email = request.getEmail();
        String phone = request.getPhone();
        Long patientId = null;

        if (userEmail != null && !userEmail.isBlank()) {
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                if (firstName == null || firstName.isBlank()) firstName = u.getFirstName();
                if (lastName == null || lastName.isBlank()) lastName = u.getLastName();
                if (email == null || email.isBlank()) email = u.getEmail();
                if (phone == null || phone.isBlank()) phone = u.getPhone();

                Optional<Patient> pOpt = patientRepository.findByUserId(u.getId());
                if (pOpt.isPresent()) {
                    patientId = pOpt.get().getId();
                }
            }
        }

        Message msg = Message.builder()
                .patientId(patientId)
                .departmentId(request.getDepartmentId())
                .subject(request.getSubject())
                .firstName(firstName != null ? firstName : "Anonymous")
                .lastName(lastName != null ? lastName : "Patient")
                .email(email != null ? email : (userEmail != null ? userEmail : "guest@lifecare.com"))
                .phone(phone != null ? phone : "0000000000")
                .message(request.getMessage())
                .isRead(false)
                .build();

        msg = messageRepository.save(msg);
        return mapToResponse(msg);
    }

    @Override
    @Transactional
    public Message sendMessageLegacy(MessageRequest request) {
        Message message = Message.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .message(request.getMessage())
                .isRead(false)
                .build();
        return messageRepository.save(message);
    }

    @Override
    public List<MessageResponse> getAllMessages() {
        return messageRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse getMessageById(Long id) {
        Message msg = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return mapToResponse(msg);
    }

    @Override
    @Transactional
    public MessageResponse markAsRead(Long id) {
        Message msg = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        msg.setIsRead(true);
        return mapToResponse(messageRepository.save(msg));
    }

    private MessageResponse mapToResponse(Message msg) {
        String deptName = null;
        if (msg.getDepartmentId() != null) {
            deptName = departmentRepository.findById(msg.getDepartmentId())
                    .map(Department::getName)
                    .orElse(null);
        }

        return MessageResponse.builder()
                .id(msg.getId())
                .patientId(msg.getPatientId())
                .departmentId(msg.getDepartmentId())
                .departmentName(deptName)
                .subject(msg.getSubject())
                .firstName(msg.getFirstName())
                .lastName(msg.getLastName())
                .email(msg.getEmail())
                .phone(msg.getPhone())
                .message(msg.getMessage())
                .isRead(msg.getIsRead())
                .createdAt(msg.getCreatedAt())
                .build();
    }
}
