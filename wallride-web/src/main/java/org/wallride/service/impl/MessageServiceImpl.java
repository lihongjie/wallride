package org.wallride.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wallride.domain.Message;
import org.wallride.domain.User;
import org.wallride.repository.MessageRepository;
import org.wallride.service.MessageService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> findTop10Messages(User user) {

        return messageRepository.findTop10ByReceivedUser(user);
    }

    @Override
    public long count() {
        return messageRepository.count();
    }

    @Override
    public long countUnRead() {
        return messageRepository.countByIsRead(0);
    }

    @Override
    public long countByUser(User user) {
        return messageRepository.countByReceivedUser(user);
    }

    @Override
    public Page<Message> getMessages(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }

    @Override
    public List<Message> markAsRead(List<Message> messages) {
        for(Message message : messages) {
            message.setIsRead(1);
        }
        return messageRepository.save(messages);
    }
}
