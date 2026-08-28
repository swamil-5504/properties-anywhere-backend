package com.propertiesanywhere.properties_anywhere_backend.repository;

import com.propertiesanywhere.properties_anywhere_backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByPropertyIdOrderBySentAtAsc(
            Long propertyId
    );

    List<Message> findByPropertyIdAndSenderIdAndReceiverIdOrPropertyIdAndSenderIdAndReceiverIdOrderBySentAtAsc(
            Long propertyId1,
            Long senderId1,
            Long receiverId1,
            Long propertyId2,
            Long senderId2,
            Long receiverId2
    );

    /*
     * Returns all messages involving a user.
     * Kept for other parts of the application if needed.
     */
    List<Message> findBySenderIdOrReceiverIdOrderBySentAtDesc(
            Long senderId,
            Long receiverId
    );

    /*
     * Returns only the latest message for each conversation.
     *
     * A conversation is identified by:
     * - the other user
     * - the property
     *
     * PostgreSQL DISTINCT ON is used here.
     */
    @Query(value = """
        SELECT m.*
        FROM messages m
        WHERE m.id IN (
            SELECT DISTINCT ON (
                LEAST(sender_id, receiver_id),
                GREATEST(sender_id, receiver_id),
                property_id
            ) id
            FROM messages
            WHERE sender_id = :userId
               OR receiver_id = :userId
            ORDER BY
                LEAST(sender_id, receiver_id),
                GREATEST(sender_id, receiver_id),
                property_id,
                sent_at DESC,
                id DESC
        )
        ORDER BY sent_at DESC
        """, nativeQuery = true)
    List<Message> findLatestMessagesByUser(
            @Param("userId") Long userId
    );

    List<Message> findByReceiverIdAndIsReadFalse(
            Long receiverId
    );

    long countByReceiverIdAndIsReadFalse(
            Long receiverId
    );

    @Modifying
    @Query("""
        UPDATE Message m
        SET m.isRead = true
        WHERE m.receiver.id = :receiverId
        AND m.sender.id = :senderId
        AND m.property.id = :propertyId
        AND m.isRead = false
    """)
    int markConversationAsRead(
            @Param("receiverId") Long receiverId,
            @Param("senderId") Long senderId,
            @Param("propertyId") Long propertyId
    );
}