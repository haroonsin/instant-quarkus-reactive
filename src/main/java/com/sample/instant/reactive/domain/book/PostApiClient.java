package com.sample.instant.reactive.domain.book;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "post-api")
@ApplicationScoped
public interface PostApiClient {

    @GET
    @Path("/posts/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Post> getPost(@PathParam("postId") Long postId);
}