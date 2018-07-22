package org.wallride.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.Message;
import org.wallride.domain.User;

import java.util.List;

public interface MessageService {

    List<Message> findTop10Messages(User user);

    long count();

    long countUnRead();

    long countByUser(User user);

    Page<Message> getMessages(Pageable pageable);

    List<Message> markAsRead(List<Message> messages);
}
