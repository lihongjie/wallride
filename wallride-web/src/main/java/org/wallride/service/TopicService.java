package org.wallride.service;

import com.github.pagehelper.PageInfo;
import org.wallride.domain.Article;
import org.wallride.domain.News;
import org.wallride.domain.Topic;
import org.wallride.model.TopicExample;
import org.wallride.model.TopicRequest;

import java.util.List;

public interface TopicService {

    PageInfo<Topic> getTopics(TopicExample example);

    List<News> getNewsByTopicIdAndLanguage(String id, String language);

    List<Article> getArticlesByTopicIdAndLanuage(String id, String language);

    void createTopic(TopicRequest topicRequest);

    void updateTopic(TopicRequest topicRequest, String id);

    void deleteTopic(String id, String language);

    Topic findOne(String id, String language);

    List<Topic> findTopicsByTopicCategoryId(String topicCategoryId, String language);

    Topic findOneByCode(String code, String language);


}
