package com.sample.instant.reactive.domain.book;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;


import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * API Spec for the Book domain.
 */
@Path("/book")
@Produces("application/json")
@Consumes("application/json")
public interface BookAPI {

    @GET
    @Path("/hello")
    @Operation(operationId = "ping", summary = "Ping book service", description = "Ping from Book service")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Hello from book service",
                    content = @Content(mediaType = TEXT_PLAIN, schema = @Schema(implementation = String.class)))})
    @Produces(TEXT_PLAIN)
    String hello();

    @GET
    @Path("/post")
    @Operation(operationId = "getPost", summary = "Fetch post information", description = "Get a specific post related data")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Post Details",
                    content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = String.class)))})
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Post> getPost(@QueryParam("postIdentifier") Long postIdentifier);

    @POST
    @Operation(operationId = "createBook", summary = "Create a valid book", description = "Enter the details of book to create a new one")
    @APIResponse(responseCode = "201", description = "The created book",
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Book.class)))
    Uni<Response> create(Book book);

    @GET
    @Operation(operationId = "getBooks", summary = "Get all books", description = "Retrieves all books found in system")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of all books",
                    content = @Content(mediaType = APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = Book.class))),
            @APIResponse(responseCode = "204", description = "No books found",
                    content = @Content(mediaType = APPLICATION_JSON))})
    Uni<List<Book>> findAllBooks();

    @GET
    @Path("/posts")
    @Operation(operationId = "getAllBookAndPost", summary = "Get all books and its related post", description = "Retrieves all books found in system along with the related post")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of all books and its related post",
                    content = @Content(mediaType = APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = Post.class))),
            @APIResponse(responseCode = "204", description = "No books found",
                    content = @Content(mediaType = APPLICATION_JSON))})
    Multi<Post> findAllBookPosts();
}
