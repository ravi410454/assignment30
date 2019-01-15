package org.cognizant.repo;

import org.cognizant.api.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void testFindBookById_Existing() {
        Book book = new Book(1111, "Book-1111", 24.99, 1, LocalDate.now());
        entityManager.persist(book);
        entityManager.flush();

        Optional<Book> found = bookRepository.findById(1111L);

        assertTrue(found.isPresent());
        assertEquals(book.getBookId(), found.get().getBookId());
        assertEquals(book.getPrice(), found.get().getPrice(), 0);
        assertEquals(book.getPublishDate(), found.get().getPublishDate());
        assertEquals(book.getTitle(), found.get().getTitle());
        assertEquals(book.getVolume(), found.get().getVolume());
    }

    @Test
    public void testBookFindById_NonExisting() {
        Optional<Book> found = bookRepository.findById(2222L);

        assertFalse(found.isPresent());
    }

    @Test
    public void testSaveBook() {
        Book book = new Book(2222, "Book-2222", 23.99, 2, LocalDate.now());

        Book saved = bookRepository.save(book);

        assertNotNull(saved);
        assertEquals(book.getBookId(), saved.getBookId());
        assertEquals(book.getPrice(), saved.getPrice(), 0);
        assertEquals(book.getPublishDate(), saved.getPublishDate());
        assertEquals(book.getTitle(), saved.getTitle());
        assertEquals(book.getVolume(), saved.getVolume());
    }

    @Test
    public void testDeleteBookById() {
        Book book = new Book(3333, "Book-3333", 22.99, 3, LocalDate.now());
        entityManager.persist(book);
        entityManager.flush();

        bookRepository.deleteById(3333L);

        Optional<Book> found = bookRepository.findById(3333L);

        assertNotNull(found);
        assertFalse(found.isPresent());
    }
}
