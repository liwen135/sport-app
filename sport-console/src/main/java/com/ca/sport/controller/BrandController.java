package com.ca.sport.controller;

import com.ca.sport.page.Pagination;
import com.ca.sport.product.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/brand")
public class BrandController {

    @Resource
    private BrandService brandService;


    @RequestMapping("/list")
    public String gotoBandList(Model model) {
        Pagination pagination = brandService.selectPaginationByQuery(null, null, 1);
        model.addAttribute("pagination",pagination);
        return "brand/list";
    }
}
