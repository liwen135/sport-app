package com.ca.sport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/type")
public class TypeController {

    @RequestMapping("/list")
    public String gotoTypeList() {
        return "type/list";
    }
}
