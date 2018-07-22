package org.wallride.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.domain.Notification;
import org.wallride.service.NotificationService;
import org.wallride.support.AuthorizedUser;

import java.util.List;

@Controller
@RequestMapping(value = "/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/unread")
    public String getTop10Notification(AuthorizedUser authorizedUser, Model model) {

        List<Notification> top10Notifications = notificationService.findTop10NotificationByUser(authorizedUser);
        long count = notificationService.countByUser(authorizedUser);
        notificationService.markAsRead(top10Notifications);
        model.addAttribute("notifications", top10Notifications);
        model.addAttribute("count", count);
        return "common/top10-notifications";
    }

    @GetMapping
    public String getNotifications(@PageableDefault Pageable pageable, Model model) {

        Page<Notification> notifications = notificationService.getNotifications(pageable);
        model.addAttribute("notifications", notifications);
        return "common/notifications";
    }

}
