package com.ca.sport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TestIndexController {

    @RequestMapping("/index")
    public String gotoIndex() {
        return "index";
    }
}
