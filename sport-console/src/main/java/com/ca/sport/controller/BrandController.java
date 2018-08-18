package com.ca.sport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/brand")
public class BrandController {

    @RequestMapping("/list")
    public String gotoBandList() {
        return "brand/list";
    }
}
