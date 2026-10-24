package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 컨트롤러 어노테이션 명시
public class DemoController {

    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", "반갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }

    // 2주차 연습문제: /hello2 매핑 추가, 속성 5개를 모델에 담아 hello2.html로 전달
    @GetMapping("/hello2")
    public String hello2(Model model) {
        model.addAttribute("name", "이름: 이세영");
        model.addAttribute("major", "과목: 자바웹프로그래밍(2)");
        model.addAttribute("subject", "주제: URL 맵핑과 컨트롤러");
        model.addAttribute("week", "주차: 2주차");
        model.addAttribute("message", "메시지: 스프링 부트 실습 완료!");
        return "hello2"; // hello2.html 연결
    }
}
