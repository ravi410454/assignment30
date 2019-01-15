package org.cognizant.assignment30.repo;

import org.cognizant.assignment30.api.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
