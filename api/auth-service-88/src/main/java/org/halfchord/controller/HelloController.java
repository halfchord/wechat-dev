package org.halfchord.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.emailSMSTask;

@RestController
@RequestMapping("/a")
public class HelloController {
    @Resource
    private emailSMSTask emailSmsTask;
    @GetMapping("/hello")
    public Object hello() {
        return "hello world";
    }

}