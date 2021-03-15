package com.hc.sbase.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class HomeController {
	@RequestMapping("/")
    public String viewHome() {
        return "index";
    }
}
