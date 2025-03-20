package vn_hcmute.Real_Time_Chat_Final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn_hcmute.Real_Time_Chat_Final.entity.Conversation;
import vn_hcmute.Real_Time_Chat_Final.entity.ConversationMember;
import vn_hcmute.Real_Time_Chat_Final.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c WHERE c.id = :conversationId") // 🛠 Viết query rõ ràng
    List<Conversation> findChatMessages(@Param("conversationId") Long conversationId);

    // ❌ XÓA `createConversation()` vì JPA không hỗ trợ INSERT
    // @Query("INSERT INTO Conversation (is_group, name) VALUES (:isGroup, :name) RETURNING c")
    // Conversation createConversation(@Param("isGroup") boolean isGroup, @Param("name") String name);

    // ✅ Trả về ID của cuộc trò chuyện nếu có, tránh lỗi ép kiểu Optional<Integer>
    @Query("SELECT c.id FROM Conversation c WHERE c.id = :conversationId")
    Optional<Long> getChatRoomId(@Param("conversationId") Long conversationId);

    @Query("SELECT new map(c.id as conversation_id, " +
            "CASE WHEN c.group = true THEN c.name ELSE u.username END as conversation_name, " +
            "u.id as user_id, u.username as username, u.email as email) " +
            "FROM Conversation c " +
            "JOIN ConversationMember cm ON c.id = cm.conversation.id " +
            "JOIN User u ON cm.user.id = u.id " +
            "WHERE c.id IN (SELECT cm2.conversation.id FROM ConversationMember cm2 WHERE cm2.user.id = :userId) " +
            "AND (c.group = true OR u.id <> :userId)")
    List<Map<String, Object>> findUserConversationsWithParticipants(@Param("userId") int userId);

    @Query("SELECT cm FROM ConversationMember cm " +
            "JOIN cm.conversation c " +
            "JOIN cm.user u " +
            "WHERE cm.conversation.id IN (" +
            "   SELECT cm2.conversation.id FROM ConversationMember cm2 WHERE cm2.user.id = :userId" +
            ") AND (c.group = true OR cm.user.id <> :userId)")
    List<ConversationMember> findListChat(@Param("userId") int userId);




}
