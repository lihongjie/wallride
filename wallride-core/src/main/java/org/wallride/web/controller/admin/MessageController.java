package org.wallride.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.domain.Message;
import org.wallride.service.MessageService;
import org.wallride.support.AuthorizedUser;

import java.util.List;

@Controller
@RequestMapping(value = "/messages")
public class MessageController {


    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/unread")
    public String getTop10Messages(AuthorizedUser authorizedUser, Model model) {

        List<Message> messages = messageService.findTop10Messages(authorizedUser);
        long count = messageService.countByUser(authorizedUser);
        messageService.markAsRead(messages);
        model.addAttribute("messages", messages);
        model.addAttribute("count", count);
        return "common/top10-messages";
    }

    @GetMapping
    public String getMessages(@PageableDefault Pageable pageable, Model model) {

        Page<Message> messages = messageService.getMessages(pageable);
        model.addAttribute("messages", messages);
        return "common/messages";
    }
}
