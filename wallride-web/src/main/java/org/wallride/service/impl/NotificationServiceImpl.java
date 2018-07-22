package org.wallride.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wallride.domain.Notification;
import org.wallride.domain.User;
import org.wallride.repository.NotificationRepository;
import org.wallride.service.NotificationService;
import org.wallride.service.UserService;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Notification> findTop10NotificationByUser(User user) {

        return notificationRepository.findTop10ByUser(user);
    }

    @Override
    public long countByUser(User user) {
        return notificationRepository.countByUser(user);
    }

    @Override
    public long countByUser(Long userId) {
        User user = userService.getUserById(userId);
        return countByUser(user);
    }

    @Override
    public long countUnRead() {
        int isRead = 0;
        return countByStatus(isRead);
    }

    @Override
    public Page<Notification> getNotifications(Pageable pageable) {

        return notificationRepository.findAll(pageable);
    }

    @Override
    public List<Notification> markAsRead(List<Notification> notifications) {
        for (Notification notification : notifications) {
            notification.setIsRead(1);
        }
        return notificationRepository.save(notifications);
    }

    private long countByStatus(int isRead) {
        return notificationRepository.countByIsRead(isRead);
    }
}
