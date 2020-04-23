package ru.itis.websockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.websockets.model.Chat;
import ru.itis.websockets.repository.ChatRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    ChatRepository chatRepository;


    @GetMapping("/chats")
    public ModelAndView chatsPage() {
        ModelAndView modelAndView = new ModelAndView();
        List<Chat> chats = chatRepository.findAllChats();

        modelAndView.addObject("chats", chats);
        return modelAndView;
    }


    @GetMapping("/chats/{chatId}")
    public ModelAndView chat(@PathVariable Long chatId) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Chat> chat = chatRepository.findChatById(chatId);
        if (chat.isPresent()) {
            modelAndView.addObject("chat", chat.get());
            modelAndView.setViewName("chat");
        } else {
            modelAndView.setViewName("redirect:/chats");
        }
        return modelAndView;
    }

}
