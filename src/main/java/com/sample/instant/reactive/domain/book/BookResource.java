package com.sample.instant.reactive.domain.book;

import com.sample.instant.reactive.book.BookRepo;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
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
    public Uni<List<Book>> findAllBooks() {
        return Book.listAll();
    }

    @Override
    public Multi<Post> findAllBookPosts() {
        return bookRepo.listAll()
                .onItem().transformToMulti(books -> Multi.createFrom().iterable(books))
                .onItem().transformToUniAndMerge(book -> postApiClient.getPost((long) book.getIsbn().charAt(0)));
    }
}