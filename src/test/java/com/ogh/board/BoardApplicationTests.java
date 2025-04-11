package com.ogh.board;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ogh.board.question.Question;
import com.ogh.board.question.QuestionRepository;
import com.ogh.board.question.QuestionService;

@SpringBootTest
class BoardApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;


	@Test
	void contextLoads() {
		Question question1 = new Question();
		question1.setSubject("제목1");
		question1.setContent("board 테스트1");
		question1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(question1);

		Question question2 = new Question();
		question2.setSubject("제목2");
		question2.setContent("board 테스트2");
		question2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(question2);
	}

	@Test
	void testList() {
		List<Question> all = this.questionRepository.findAll();
        // assertEquals(2, all.size());

        Question q = all.get(0);
        // assertEquals("제목1", q.getSubject());
	}

	@Test
	void testJpa() {
		// Optional<Question> oq = this.questionRepository.findById(1);
        // assertTrue(oq.isPresent());
        // Question q = oq.get();
        // q.setSubject("수정된 제목");
        // this.questionRepository.save(q);
		for(int i=1; i<= 300; i++) {
			String subject = String.format("테스트 데이터입니다.:[%03d]", i);
			String content = String.format("Test Content : [%03d]", i);
			this.questionService.create(subject, content, null);
		}
	}

}
