package org.wallride.model;

import org.wallride.domain.Topic;

import java.io.Serializable;

public class TopicRequest implements Serializable {

    private String language;

    private String topicCategoryId;

    private String code;

    private String name;

    private String description;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTopicCategoryId() {
        return topicCategoryId;
    }

    public void setTopicCategoryId(String topicCategoryId) {
        this.topicCategoryId = topicCategoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topic buildTopic() {
        Topic topic = new Topic();
        topic.setLanguage(this.language);
        topic.setTopicCategoryId(this.topicCategoryId);
        topic.setCode(this.code);
        topic.setName(this.name);
        topic.setDescription(this.description);
        return topic;
    }
}
