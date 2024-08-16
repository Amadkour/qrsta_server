package com.softkour.qrsta_server.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.softkour.qrsta_server.entity.enumeration.EssayCorrectionType;
import com.softkour.qrsta_server.entity.enumeration.QuestionType;
import com.softkour.qrsta_server.entity.quiz.*;
import com.softkour.qrsta_server.payload.request.QuizCorrectionRequest;
import com.softkour.qrsta_server.repo.quiz.StudentQuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.course.StudentCourse;
import com.softkour.qrsta_server.entity.enumeration.NotificationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.public_entity.StudentSchedule;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.quiz.QuizRepository;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;
import com.softkour.qrsta_server.service.public_service.NotificationService;

@Service
public class QuizService {

    @Autowired
    AuthService authService;
    @Autowired
    StudentScheduleRepo scheduleRepo;
    @Autowired
    NotificationService notificationService;
   @Autowired
    StudentQuizRepo studentQuizRepo;

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Transactional
    public Quiz save(Quiz quiz) {
        System.out.println(quiz.getCourses().stream().map(e->e.getSessions().stream().map(r->r.getId()).toList()).toList());
        quiz = quizRepository.save(quiz);
        Set<User> users = new HashSet<>();
        for (CourseQuiz c : quiz.getCourses()) {
            for (StudentCourse u : c.getSessions().iterator().next().getCourse().getStudents()) {
                users.add(u.getStudent().getUser());
            }
        }
        notificationService.addNotification(
                NotificationType.QUIZ, "there are a new exam successfully and will start in " + quiz.getCourses().stream().map(e -> e.getSessions().iterator().next().getCourse().getName()),
                quiz.getId(),
                users);

        return quiz;
    }

    public Quiz update(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Transactional(readOnly = true)
    public List<Quiz> findAll(String phone) {
        User u;
        if (phone != null) {
            u = authService.getUserByPhoneNumber(phone);
        } else {
            u = MyUtils.getCurrentUserSession(authService);
        }
        if (u.getType() == UserType.TEACHER) {
            quizRepository.findAllByCourses_course_teacher_id(u.getId());
        } else {
            quizRepository.findAllByCourses_students_student_id(u.getId());

        }
        return quizRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(() -> new ClientException("quiz", "not found"));
    }

    @Transactional
    public String correct(QuizCorrectionRequest quizCorrectionRequest) {
        Quiz q = quizRepository.findById(quizCorrectionRequest.getQuizId()).orElseThrow(() -> new ClientException("quiz", "not found"));
        List<Question> questions = q.getQuestions().stream().takeWhile(e -> quizCorrectionRequest.getQuestionsId().contains(e.getId())).toList();
        //=================================
        List<Question> wrongQuestions = new ArrayList<>();
        User u = MyUtils.getCurrentUserSession(authService);
        int totalPoints = questions.stream().mapToInt(Question::getGrade).sum();
        int points = 0;
        for (int i = 0; i < questions.size(); i++) {
            List<String> correctAnswer = questions.get(i).getOptions().stream().takeWhile(Option::getIsCorrectAnswer).map(Option::getTitle).toList();
            if (questions.get(i).getType() == QuestionType.TRUEFALSE) {
                System.out.println(quizCorrectionRequest.getAnswers().get(i).get(0));
                if(correctAnswer.isEmpty()){
                    points += questions.get(i).getGrade();
                    System.out.println("error=========>"+ questions.get(i).getOptions().stream().map(e->e.getTitle()+":"+e.getIsCorrectAnswer()).toList());
                }else{
                    if (!quizCorrectionRequest.getAnswers().get(i).isEmpty() && Objects.equals(correctAnswer.get(0), quizCorrectionRequest.getAnswers().get(i).get(0))) {
                        points += questions.get(i).getGrade();
                    } else {
                        wrongQuestions.add(questions.get(i));

                    }
                }
            } else if (questions.get(i).getType() == QuestionType.MCQ) {

                if (!quizCorrectionRequest.getAnswers().get(i).isEmpty() &&
                        new HashSet<>(correctAnswer).containsAll(quizCorrectionRequest.getAnswers().get(i)) &&
                        quizCorrectionRequest.getAnswers().get(i).size() == correctAnswer.size()) {
                    points += questions.get(i).getGrade();
                } else {
                    wrongQuestions.add(questions.get(i));

                }
            } else if (questions.get(i).getType() == QuestionType.SPACE) {
                if (new HashSet<>(quizCorrectionRequest.getAnswers().get(i)).containsAll(correctAnswer)) {
                    points += questions.get(i).getGrade();
                } else {
                    wrongQuestions.add(questions.get(i));
                }
            } else {
                EssayCorrectionType correctionType = questions.get(i).getCorrectionType();
                if (correctionType == EssayCorrectionType.MATCHINGAI && Objects.equals(correctAnswer.get(0), quizCorrectionRequest.getAnswers().get(i).get(0))) {
                    points += questions.get(i).getGrade();
                } else if (correctionType == EssayCorrectionType.KEYWORDS && new HashSet<>(quizCorrectionRequest.getAnswers().get(i)).containsAll(correctAnswer)) {
                    points += questions.get(i).getGrade();
                } else {
                    wrongQuestions.add(questions.get(i));
                }
            }
        }
        //================================[add student]
        Set<CourseQuiz> courses = new HashSet<>(q.getCourses());
        // for (int i = 0; i < q.getCoveredSessions().size(); i++) {
        System.out.println("user info==============");
        System.out.println(u.getId());
        System.out.println(u.getStudent().getId());
        System.out.println(courses.stream()
                .takeWhile(e -> e.getCourse().getStudents().stream().anyMatch(s -> Objects.equals(s.getId(), u.getStudent().getId()))).map(e->e.getCourse().getName()).toList());
        CourseQuiz c = courses.stream()
                .takeWhile(e -> e.getCourse().getStudents().stream().anyMatch(s -> Objects.equals(s.getId(), u.getStudent().getId())))
                .toList().get(0);
        StudentQuiz student = new StudentQuiz();
        student.setCourse(c);
        System.out.println("user answers is: "+quizCorrectionRequest.getAnswers());
        String answersAsString = IntStream.range(0, quizCorrectionRequest.getQuestionsId().size()).mapToObj(i -> quizCorrectionRequest.getQuestionsId().get(i) + "==="+ wrongQuestions.stream().noneMatch(e-> Objects.equals(e.getId(), quizCorrectionRequest.getQuestionsId().get(i))) +"===" + String.join(":::", quizCorrectionRequest.getAnswers().get(i)) + ";;;").collect(Collectors.joining());
        student.setAnswers(answersAsString);
        student.setGrade(points);
        student.setStudent(u.getStudent());
        Set<StudentQuiz> students=c.getStudents();
        System.out.println(student.getStudent().getUser().getName());
        students.add(student);
        c.setStudents(students);
        courses.add(c);
        q.setCourses(courses);
        q = quizRepository.save(q);
        System.out.println("============================>");
        System.out.println(c.getStudents().size());
        /// add it in student schedule
        System.out.println("total of wrong answers:" + wrongQuestions.size());
        if (((double) points / totalPoints) < 0.5) {
            ///============================notify parent
            HashSet<User> parents=new HashSet<>();
            parents.add(u.getStudent().getParent());
            notificationService.addNotification(NotificationType.QUIZ,"your son's score is"+points+"from"+totalPoints,q.getId(),parents);
            //================================[add schedule]
            StudentSchedule item = new StudentSchedule();
            item.setDone(false);
            item.setRead(false);
            item.setCourse(c.getSessions().iterator().next().getCourse());
            item.setSession(c.getSessions().iterator().next());
            item.setUser(u);
            item.setQuestion(wrongQuestions.get((new Random()).nextInt(wrongQuestions.size())));
            scheduleRepo.save(item);
        }
        return String.valueOf(points) + '/' + totalPoints;

    }

    public void delete(Long id) {
        quizRepository.deleteById(id);
    }

    public Quiz findByQuizIdAndCourseName(Long quizId, String courseName) {
//        return quizRepository.findByIdAndCourses_course_name(quizId, courseName);
        return quizRepository.findById(quizId).orElseThrow(() -> new ClientException("course", "this course not found"));
    }

    public StudentQuiz updateDegree(Long quizId, Long studentId, double newDegree) {
        List<StudentQuiz> students= studentQuizRepo.findByStudent_idAndCourse_quiz_id(studentId,quizId);
        StudentQuiz student=students.get(students.size()-1);
       student.setModified(true);
       student.setGrade(newDegree);
       return studentQuizRepo.save(student);
    }
}
