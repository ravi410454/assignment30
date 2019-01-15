package org.cognizant.controller;

import org.cognizant.api.Book;
import org.cognizant.api.SubBookIdentity;
import org.cognizant.api.Subject;
import org.cognizant.repo.BookRepository;
import org.cognizant.repo.SubjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(SubjectController.class)
public class SubjectControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private SubjectRepository subjectRepository;

    @Test
    public void getSubjectByIdMissingParam() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/subject/get")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.BAD_REQUEST.value()));
        assertThat(response.getContentAsString(), is(""));

    }

    @Test
    public void getSubjectByIdExistingId() throws Exception {
        Optional<Book> book = Optional.of(new Book(1111, "Book-1111", 10.99, 1, LocalDate.now()));
        Subject subject = new Subject(new SubBookIdentity(2222, 1111), "Sub-2222", 24);
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(subject);
        given(subjectRepository.findBySubjectId(2222L)).willReturn(subjectList);
        given(bookRepository.findById(1111L)).willReturn(book);

        MockHttpServletResponse response = mvc.perform(get("/subject/get?subjectId=2222")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        subject.getBooks().add(book.get());
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Found Subject: " + subject));

    }

    @Test
    public void getSubjectByIdNonExistingId() throws Exception {
        given(subjectRepository.findBySubjectId(2222L)).willReturn(new ArrayList<>());

        MockHttpServletResponse response = mvc.perform(get("/subject/get?subjectId=1111")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is(""));

    }

    @Test
    public void saveMissingParam() throws Exception {
        Subject subject = new Subject(new SubBookIdentity(2222, 1111), "Sub-2222", 12);
        given(subjectRepository.save(subject)).willReturn(subject);

        MockHttpServletResponse response = mvc.perform(get("/subject/add?subjectId=2222&bookId=1111&duration=12")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.BAD_REQUEST.value()));
        assertThat(response.getContentAsString(), is(""));

    }

    @Test
    public void saveNewBook() throws Exception {
        Subject subject = new Subject(new SubBookIdentity(2222, 1111), "Sub-2222", 12);
        given(subjectRepository.save(any(Subject.class))).willReturn(subject);

        MockHttpServletResponse response = mvc.perform(get("/subject/add?subjectId=2222&bookId=1111&subTitle=Sub-2222&duration=12")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Successfully Added: " + subject));

    }

    @Test
    public void saveFailed() throws Exception {
        Subject subject = new Subject(new SubBookIdentity(2222, 1111), "Sub-2222", 12);
        given(subjectRepository.save(subject)).willReturn(null);

        MockHttpServletResponse response = mvc.perform(get("/subject/add?subjectId=2222&bookId=1111&subTitle=Sub-2222&duration=12")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Successfully Added: " + null));

    }

    @Test
    public void deleteBookByIdMissingParam() throws Exception {
        doNothing().when(subjectRepository).deleteById(2222L);

        MockHttpServletResponse response = mvc.perform(get("/subject/delete")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.BAD_REQUEST.value()));
        assertThat(response.getContentAsString(), is(""));

    }

    @Test
    public void deleteBookByIdExistingId() throws Exception {
        Subject subject = new Subject(new SubBookIdentity(2222, 1111), "Sub-2222", 24);
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(subject);
        given(subjectRepository.findBySubjectId(2222L)).willReturn(subjectList);
        given(bookRepository.existsById(1111L)).willReturn(true);
        doNothing().when(bookRepository).deleteById(1111L);
        doNothing().when(subjectRepository).delete(subject);

        MockHttpServletResponse response = mvc.perform(get("/subject/delete?subjectId=2222")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Successfully Deleted: [Subject{subjectId=2222, " +
                "subTitle=Sub-2222, durationInHours=24, bookId=1111}]"));
        verify(subjectRepository, times(1)).delete(subject);

    }

    @Test(expected = NestedServletException.class)
    public void deleteBookByIdNonExistingId() throws Exception {
        Subject subject = new Subject(new SubBookIdentity(2222, 1111), "Sub-2222", 24);
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(subject);
        given(subjectRepository.findBySubjectId(2222L)).willReturn(subjectList);
        given(bookRepository.existsById(1111L)).willReturn(true);
        doNothing().when(bookRepository).deleteById(1111L);
        doThrow(new RuntimeException("Test Exception")).when(subjectRepository).delete(subject);

        mvc.perform(get("/subject/delete?subjectId=2222")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();
    }

}
