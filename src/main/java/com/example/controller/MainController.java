package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/company/all")
    public String getCompaniesPage(){

        return "company/companies";
    }
}
