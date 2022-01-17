package com.sample.instant.reactive.book;

import com.sample.instant.reactive.domain.book.Book;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookRepo implements PanacheRepository<Book> {
}
