package org.cognizant.assignment28.repo;

import org.cognizant.assignment28.api.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
