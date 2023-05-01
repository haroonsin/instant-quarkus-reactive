package com.sample.instant.reactive.domain.book;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "post-api")
@ApplicationScoped
public interface PostApiClient {

    @GET
    @Path("/posts/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Post> getPost(@PathParam("postId") Long postId);
}