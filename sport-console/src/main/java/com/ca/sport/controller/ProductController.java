package com.ca.sport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {


    @RequestMapping("/list")
    public String gotoProductList(){
        return "product/list";
    }
}
