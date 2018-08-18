package com.ca.sport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/order")
@Controller
public class OrderController {

    @RequestMapping("/view")
    public String gotoOrderVeiw() {

        return "order/view";
    }
}
