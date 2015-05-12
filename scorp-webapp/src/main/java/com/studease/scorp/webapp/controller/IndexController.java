package com.studease.scorp.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: liushaoping
 * Date: 2015/5/12.
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public
    @ResponseBody
    String index() {
        return "You are visiting the index page.";
    }

}
