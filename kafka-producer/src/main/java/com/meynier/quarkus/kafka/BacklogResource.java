package com.meynier.quarkus.kafka;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.annotations.Stream;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.resteasy.annotations.jaxrs.FormParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.concurrent.TimeUnit;

@Path("/backlog")
@ApplicationScoped
public class BacklogResource {

    private static int COUNTER = 0;
    private static String JIRA_ID_PATTERN = "JIRA_%n";

    @Inject @Stream("backlog")
    Emitter<Jira> emitter;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(
             @NotEmpty final String title,
             @NotEmpty final String description,
             @Suspended final AsyncResponse asyncResponse) {
        final String id = String.format(JIRA_ID_PATTERN, COUNTER);
        asyncResponse.setTimeoutHandler(asyncResp -> asyncResp.resume(Response.accepted(id).build()));
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);
        emitter.send(new Jira(id, title, description));
    }

   /* @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response get(@NotEmpty final String id) {

        if (id == null) {
            throw new WebApplicationException("Jira id " + id + " does not exist.", 404);
        }
        //searchJira
        return Response.accepted(id).build();
    }*/



    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }
            return Response.status(code)
                    .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
                    .build();
        }

    }
}
