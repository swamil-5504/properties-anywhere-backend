package com.propertiesanywhere.properties_anywhere_backend.service;

import com.propertiesanywhere.properties_anywhere_backend.model.Message;
import com.propertiesanywhere.properties_anywhere_backend.model.Property;
import com.propertiesanywhere.properties_anywhere_backend.model.User;
import com.propertiesanywhere.properties_anywhere_backend.repository.MessageRepository;
import com.propertiesanywhere.properties_anywhere_backend.repository.PropertyRepository;
import com.propertiesanywhere.properties_anywhere_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public MessageService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            PropertyRepository propertyRepository) {

        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    public Message sendMessage(
            Long senderId,
            Long receiverId,
            Long propertyId,
            String content) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() ->
                        new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() ->
                        new RuntimeException("Receiver not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        Message message = new Message();

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setProperty(property);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByProperty(Long propertyId) {
        return messageRepository.findByPropertyIdOrderBySentAtAsc(propertyId);
    }

    public List<Message> getConversation(
            Long user1Id,
            Long user2Id,
            Long propertyId) {

        return messageRepository
                .findByPropertyIdAndSenderIdAndReceiverIdOrPropertyIdAndSenderIdAndReceiverIdOrderBySentAtAsc(
                        propertyId,
                        user1Id,
                        user2Id,
                        propertyId,
                        user2Id,
                        user1Id
                );
    }

    public List<Message> getMessagesByUser(Long userId) {
        return messageRepository.findLatestMessagesByUser(userId);
    }

    public long getUnreadCount(Long userId) {
        return messageRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    public List<Message> getUnreadMessages(Long userId) {
        return messageRepository.findByReceiverIdAndIsReadFalse(userId);
    }

    @Transactional
    public int markConversationAsRead(
            Long receiverId,
            Long senderId,
            Long propertyId) {

        return messageRepository.markConversationAsRead(
                receiverId,
                senderId,
                propertyId
        );
    }
}