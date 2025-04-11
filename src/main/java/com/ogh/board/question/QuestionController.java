package com.ogh.board.question;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ogh.board.answer.AnswerForm;
import com.ogh.board.user.SiteUser;
import com.ogh.board.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @RequestMapping(value = "/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        // @Valid : validation을 수행하겠다.
        // BindingResult : validation 결과를 담는 객체
        // @Valid가 붙은 객체의 validation 결과를 bindingResult에 담아준다.
        if(bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        // @PathVariable("id") Integer id : URL의 경로에서 id라는 변수를 추출하여 Integer id 변수에 담겠다.
        // Principal : 인증된 사용자의 정보를 담고 있는 객체
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal) {
        // @PathVariable("id") Integer id : URL의 경로에서 id라는 변수를 추출하여 Integer id 변수에 담겠다.
        // Principal : 인증된 사용자의 정보를 담고 있는 객체
        if(bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/delete/{id}")
    public String questionDelete(@PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

}
