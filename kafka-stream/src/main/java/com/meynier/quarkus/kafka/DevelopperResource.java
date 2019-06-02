package com.meynier.quarkus.kafka;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.smallrye.reactive.messaging.annotations.Stream;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class DevelopperResource {


    @ConfigProperty(name = "developper.name", defaultValue = "Unknow")
    private String name;

    @ConfigProperty(name="developper.timeToResolve",defaultValue = "5000")
    private int timeToResolve;

    @Incoming("backlog")
    @Outgoing("jiras")
    public Jira process(Jira jira) throws InterruptedException {
        Thread.sleep(timeToResolve);
        jira.developper = name;
        return jira;
    }
/*
    @Incoming("backlog")
    @Outgoing("jiras")
    public Jira process(KafkaMessage<String, Jira> jiraMessage) throws InterruptedException {

        Thread.sleep(timeToResolve);
        jiraMessage.getPayload().developper = name;
        return jiraMessage.getPayload();
    }*/

}
