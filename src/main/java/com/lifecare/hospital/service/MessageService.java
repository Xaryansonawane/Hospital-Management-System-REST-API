package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.MessageRequest;
import com.lifecare.hospital.dto.response.MessageResponse;
import com.lifecare.hospital.entity.Message;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest request, String userEmail);
    Message sendMessageLegacy(MessageRequest request);
    List<MessageResponse> getAllMessages();
    MessageResponse getMessageById(Long id);
    MessageResponse markAsRead(Long id);
}
