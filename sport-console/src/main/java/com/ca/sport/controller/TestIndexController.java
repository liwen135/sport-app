package com.ca.sport.controller;

import com.ca.sport.bean.TestApp;
import com.ca.sport.service.test.TestAppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class TestIndexController {

    @Resource
    private TestAppService testAppService;


    @RequestMapping("/index")
    public String gotoIndex(Model model) {
        TestApp testApp = testAppService.selectById(1L);
        model.addAttribute("name", testApp.getName());
        model.addAttribute("age", testApp.getAge());
        return "index";
    }
}
