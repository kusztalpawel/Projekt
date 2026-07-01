package org.project.server;

import org.project.server.repository.ExamRepository;
import org.project.server.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ExamServiceTest {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRepository examRepository;
}
