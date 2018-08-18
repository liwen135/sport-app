package com.ca.sport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/console/frame")
public class FrameController {

    @RequestMapping("/product_main")
    public String gotoProductIndex(){
        return "frame/product_main";
    }

    @RequestMapping("/product_left")
    public String gotoProductLeft(){
        return "frame/product_left";
    }
    @RequestMapping("/product/list")
    public String gotoProductList(){
        return "/product/list";
    }

    @RequestMapping("/order_main")
    public String gotoOrderIndex(){
        return "frame/order_main";
    }

    @RequestMapping("/order_left")
    public String gotoOrderLeft(){
        return "frame/order_left";
    }


    @RequestMapping("/order/list")
    public String gotoOrderList(){
        return "order/list";
    }
}
