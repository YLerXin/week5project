package vttp.batchb.ssf.week5project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/","index"})
public class IndexController {
    @GetMapping
    public String getIndex(Model model){
        return "index";
    }
}
