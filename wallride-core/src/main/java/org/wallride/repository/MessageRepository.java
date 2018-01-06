package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallride.domain.Message;
import org.wallride.domain.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findTop10ByReceivedUser(User user);

    long countByIsRead(Integer isRead);

    long countByReceivedUser(User user);

    long countByIsReadAndSendUserAndReceivedUser(Integer isRead, User sendUser, User receivedUser);
}
