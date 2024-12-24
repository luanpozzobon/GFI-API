package br.com.fynncs.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class Notification {

    @GetMapping
    public String obter(){
        return "mike";
    }
}
