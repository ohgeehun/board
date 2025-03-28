package com.ogh.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {

    @RequestMapping("/board")
    @ResponseBody
    public String index() {
        return "안녕하세요. index 테스트 입니다.";
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
