package com.meynier.quarkus.kafka;

import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.resteasy.annotations.jaxrs.FormParam;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

@Path("/backlog")
@ApplicationScoped
public class BacklogResource {

    private static int COUNTER = 0;
    private static String JIRA_ID_PATTERN = "JIRA_%n";

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(
            @FormParam("title") final String title,
            @FormParam("description") final String description) {
        final String id = String.format(JIRA_ID_PATTERN, COUNTER);
        this.manageJira(new Jira(id, title, description));
        return Response.accepted(id).build();
    }

    //How do ?

    @Outgoing("backlog")
    public Flowable<String> manageJira() {
        return Flowable.interval(5, TimeUnit.SECONDS).map(tick -> jira.toString());
    }

}
