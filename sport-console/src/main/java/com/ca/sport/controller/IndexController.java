package com.ca.sport.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/console")
public class IndexController {

    @RequestMapping(value = "index")
    public String gotoIndex() {
        return "index";
    }

    @RequestMapping(value = "top")
    public String gotoTop() {
        return "top";
    }

    @RequestMapping(value = "main")
    public String gotoMain() {
        return "main";
    }

    @RequestMapping(value = "left")
    public String gotoLeft() {
        return "left";
    }

    @RequestMapping(value = "right")
    public String gotoRight() {
        return "right";
    }


}
