package com.propertiesanywhere.properties_anywhere_backend.controller;

import com.propertiesanywhere.properties_anywhere_backend.model.Message;
import com.propertiesanywhere.properties_anywhere_backend.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Message sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam Long propertyId,
            @RequestBody MessageRequest request) {

        return messageService.sendMessage(
                senderId,
                receiverId,
                propertyId,
                request.getContent()
        );
    }

    @GetMapping("/property/{propertyId}")
    public List<Message> getMessagesByProperty(
            @PathVariable Long propertyId) {

        return messageService.getMessagesByProperty(propertyId);
    }

    @GetMapping("/conversation")
    public List<Message> getConversation(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id,
            @RequestParam Long propertyId) {

        return messageService.getConversation(
                user1Id,
                user2Id,
                propertyId
        );
    }

    @GetMapping("/user/{userId}")
    public List<Message> getMessagesByUser(
            @PathVariable Long userId) {

        return messageService.getMessagesByUser(userId);
    }

    @GetMapping("/unread/{userId}")
    public List<Message> getUnreadMessages(
            @PathVariable Long userId) {

        return messageService.getUnreadMessages(userId);
    }

    @GetMapping("/unread/count/{userId}")
    public long getUnreadCount(
            @PathVariable Long userId) {

        return messageService.getUnreadCount(userId);
    }

    @PutMapping("/read")
    public int markConversationAsRead(
            @RequestParam Long receiverId,
            @RequestParam Long senderId,
            @RequestParam Long propertyId) {

        return messageService.markConversationAsRead(
                receiverId,
                senderId,
                propertyId
        );
    }

    public static class MessageRequest {

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}