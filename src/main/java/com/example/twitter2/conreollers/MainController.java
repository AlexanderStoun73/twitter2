package com.example.twitter2.conreollers;

import com.example.twitter2.entity.Message;
import com.example.twitter2.entity.User;
import com.example.twitter2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepository repository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "")
                           String filter, Model model) {
        Iterable<Message> messages = repository.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = repository.findByTag(filter);
        } else {
            messages = repository.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model,
            @RequestParam("file") MultipartFile file) throws IOException {
        Message message = new Message(text, tag, user);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFie = UUID.randomUUID().toString();
            String resultFileName =  uuidFie + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }

        repository.save(message);

        Iterable<Message> messages = repository.findAll();

        model.put("messages", messages);

        return "main";
    }
}
