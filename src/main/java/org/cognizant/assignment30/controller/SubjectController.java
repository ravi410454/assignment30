package org.cognizant.assignment28.controller;

import org.cognizant.assignment28.api.SubBookIdentity;
import org.cognizant.assignment28.api.Subject;
import org.cognizant.assignment28.repo.BookRepository;
import org.cognizant.assignment28.repo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SubjectController {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "subject/get", method = RequestMethod.GET)
    @ResponseBody
    @Transactional(readOnly = true)
    public String get(@RequestParam long subjectId) {
        List<Subject> subjects = subjectRepository.findBySubjectId(subjectId);
        System.out.println(subjects);
        Subject subject = new Subject();
        for (Subject s : subjects) {
            subject.setSubBookIdentity(s.getSubBookIdentity());
            subject.setSubTitle(s.getSubTitle());
            subject.setDurationInHours(s.getDurationInHours());
            subject.getBooks().add(bookRepository.findById(s.getSubBookIdentity().getBookId()).orElse(null));
        }
        return "Found Subject: " + subject;
    }

    @RequestMapping(value = "subject/add", method = RequestMethod.GET)
    @ResponseBody
    public String add(@RequestParam long subjectId, @RequestParam String subTitle, @RequestParam int duration,
                      @RequestParam long bookId) {
        Subject subject = new Subject(new SubBookIdentity(subjectId, bookId), subTitle, duration);
        subjectRepository.save(subject);
        return "Successfully Added " + subject;
    }

    @RequestMapping(value = "subject/delete", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@RequestParam long subjectId) {
        List<Subject> subjects = subjectRepository.findBySubjectId(subjectId);
        // delete all linked books before deleting subject
        subjects.forEach(s -> {
            if (bookRepository.existsById(s.getSubBookIdentity().getBookId())) {
                bookRepository.deleteById(s.getSubBookIdentity().getBookId());
            }
        });
        // delete all the rows from subject table for each book
        subjects.forEach(s -> subjectRepository.delete(s));
        return "Successfully Deleted: " + subjects;
    }
}
