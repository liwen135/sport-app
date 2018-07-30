package com.ca.sport.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TestIndexController {

    private Logger logger = LoggerFactory.getLogger(TestIndexController.class);
//    @Resource
//    private TestAppService testAppService;


    @RequestMapping("/index")
    public String gotoIndex(Model model) {
//        TestApp testApp = testAppService.selectById(1L);
//        model.addAttribute("name", testApp.getName());
//        model.addAttribute("age", testApp.getAge());
        model.addAttribute("test", "testApp");
        logger.error("test controller");
        return "index";
    }
}
