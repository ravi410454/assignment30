package org.cognizant.assignment30.controller;

import org.cognizant.assignment30.api.Book;
import org.cognizant.assignment30.repo.BookRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void getBookByIdMissingParam() throws Exception {
        Optional<Book> book = Optional.of(new Book(1111, "Book-1111", 10.99, 1, LocalDate.now()));
        given(bookRepository.findById(1111l)).willReturn(book);

        MockHttpServletResponse response = mvc.perform(get("/book/get")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.BAD_REQUEST.value()));
        assertThat(response.getContentAsString(), is(""));

    }

    @Test
    public void getBookByIdExistingId() throws Exception {
        Optional<Book> book = Optional.of(new Book(1111, "Book-1111", 10.99, 1, LocalDate.now()));
        given(bookRepository.findById(1111l)).willReturn(book);

        MockHttpServletResponse response = mvc.perform(get("/book/get?bookId=1111")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Found Book: " + book.get()));

    }

    @Test
    public void getBookByIdNonExistingId() throws Exception {
        Optional<Book> book = Optional.empty();
        given(bookRepository.findById(1111l)).willReturn(book);

        MockHttpServletResponse response = mvc.perform(get("/book/get?bookId=1111")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Found Book: " + book.orElse(null)));

    }

    @Test
    public void saveMissingParam() throws Exception {
        Book book = new Book(1111, "Book-1111", 10.99, 1, LocalDate.parse("2012-01-01", DateTimeFormatter.ISO_DATE));
        given(bookRepository.save(book)).willReturn(book);

        MockHttpServletResponse response = mvc.perform(get("/book/add?bookId=1111&title=Book-1111" +
                "&price=10.99&volume=1")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.BAD_REQUEST.value()));
        assertThat(response.getContentAsString(), is(""));

    }

    @Test
    public void saveNewBook() throws Exception {
        Book book = new Book(1111, "Book-1111", 10.99, 1, LocalDate.parse("2012-01-01", DateTimeFormatter.ISO_DATE));
        given(bookRepository.save(book)).willReturn(book);

        MockHttpServletResponse response = mvc.perform(get("/book/add?bookId=1111&title=Book-1111" +
                "&price=10.99&volume=1&publishDate=2012-01-01")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Successfully Added: " + book));

    }

    @Test
    public void saveFailed() throws Exception {
        Book book = new Book(1111, "Book-1111", 10.99, 1, LocalDate.parse("2012-01-01", DateTimeFormatter.ISO_DATE));
        given(bookRepository.save(book)).willReturn(null);

        MockHttpServletResponse response = mvc.perform(get("/book/add?bookId=1111&title=Book-1111" +
                "&price=10.99&volume=1&publishDate=2012-01-01")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Successfully Added: " + null));

    }

    @Test
    public void deleteBookByIdMissingParam() throws Exception {
        doNothing().when(bookRepository).deleteById(1111L);

        MockHttpServletResponse response = mvc.perform(get("/book/delete")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.BAD_REQUEST.value()));
        assertThat(response.getContentAsString(), is(""));

    }

    @Test
    public void deleteBookByIdExistingId() throws Exception {
        doNothing().when(bookRepository).deleteById(1111L);

        MockHttpServletResponse response = mvc.perform(get("/book/delete?bookId=1111")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("Successfully Deleted: " + 1111L));
        verify(bookRepository, times(1)).deleteById(1111L);

    }

    @Test(expected = NestedServletException.class)
    public void deleteBookByIdNonExistingId() throws Exception {
        doThrow(new RuntimeException("Book not found")).when(bookRepository).deleteById(1111L);

        mvc.perform(get("/book/delete?bookId=1111")
                .contentType(MediaType.TEXT_PLAIN)).andReturn().getResponse();
    }

}
