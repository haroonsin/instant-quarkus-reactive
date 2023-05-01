package com.sample.instant.reactive.domain.book;

import com.sample.instant.reactive.book.BookRepo;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

public class BookResource implements BookAPI {

    @Inject
    @RestClient
    PostApiClient postApiClient;
    private final BookRepo bookRepo;

    public BookResource(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public String hello() {
        return "Hello from book service!";
    }

    @Override
    public Uni<Post> getPost(Long postIdentifier) {
        return postApiClient.getPost(postIdentifier);
    }

    @Override
    public Uni<Response> create(Book book) {
        return Panache.
                <Book>withTransaction(book::persist)
                .map(newBook -> Response.created(URI.create(newBook.getId().toString())).build());
    }

    @Override
    @WithSession
    public Uni<List<Book>> findAllBooks() {
        return Book.listAll();
    }

    @Override
    public Multi<Post> findAllBookPosts() {
        return findAllBooks()
                .onItem().transformToMulti(books -> Multi.createFrom().iterable(books))
                .onItem().transformToUniAndMerge(book -> postApiClient.getPost((long) book.getIsbn().charAt(0)));
    }
}