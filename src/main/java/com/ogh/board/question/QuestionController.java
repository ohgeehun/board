package com.ogh.board.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ogh.board.answer.AnswerForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @RequestMapping(value = "/list")
    public String list(Model model) {
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    // @RequestMapping(value = "/create") : GET 요청을 처리하는 메소드
    // @RequestMapping(value = "/create", method = RequestMethod.GET) : GET 요청을 처리하는 메소드
    // @RequestMapping(value = "/create")
    // public String questionCreate() {
    //     return "question_form";
    // }
    @GetMapping(value = "/create")
    public String questionCreate(QuestionForm questionForm) {
        // QuestionForm questionForm : question_form.html에서 사용될 객체
        return "question_form";
    }

    // @RequestParam : request parameter를 메소드의 파라미터로 받을 수 있도록 해주는 어노테이션
    // @RequestParam("subject") String subject : request parameter의 이름이 subject인 값을 subject라는 이름의 String 변수로 받겠다.
    // @RequestParam("content") String content : request parameter의 이름이 content인 값을 content라는 이름의 String 변수로 받겠다.
    // @PostMapping(value = "/create")
    // public String questionCreate(@Valid QuestionForm questionForm, @RequestParam("subject") String subject, @RequestParam("content") String content) {
    //     this.questionService.create(subject, content);
    //     return "redirect:/question/list";
    // }
    // Changed to use QuestionForm
    @PostMapping(value = "/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        // @Valid : validation을 수행하겠다.
        // BindingResult : validation 결과를 담는 객체
        // @Valid가 붙은 객체의 validation 결과를 bindingResult에 담아준다.
        if(bindingResult.hasErrors()) {
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }

}
