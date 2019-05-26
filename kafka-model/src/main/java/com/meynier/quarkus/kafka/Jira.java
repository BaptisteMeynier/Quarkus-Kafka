package com.meynier.quarkus.kafka;

public class Jira {
    public String id;
    public String title;
    public String description;
    public String developper;

    public Jira(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Jira{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", developper='" + developper + '\'' +
                '}';
    }
}
