package com.hasssektor.bean;

import com.hasssektor.eksiapi.models.Topic;

/**
 * Created by umut on 4.08.2017.
 */
public class TopicEntity {

    private int id;
    private String tittle;
    private String slug;
    private int entryCount;

    public TopicEntity() {
    }

    public TopicEntity(Topic topic) {
        this.id = topic.getId();
        this.tittle = topic.getTitle();
        this.entryCount = topic.getEntryCounts().getTotal();
        this.slug = topic.getSlug();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(int entryCount) {
        this.entryCount = entryCount;
    }

    @Override
    public String toString() {
        return "TopicEntity{" +
                "id=" + id +
                ", tittle='" + tittle + '\'' +
                ", slug='" + slug + '\'' +
                ", entryCount=" + entryCount +
                '}';
    }
}
