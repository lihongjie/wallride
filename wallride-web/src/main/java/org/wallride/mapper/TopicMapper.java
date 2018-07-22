package org.wallride.mapper;

import org.wallride.domain.Article;
import org.wallride.domain.News;
import org.wallride.domain.Topic;

import java.util.List;
import java.util.Map;

public interface TopicMapper extends BaseMapper<Topic, String> {

    List<News> findNewsByTopicIdAndLanguage(Map<String, Object> params);

    List<Article> findArticlesByTopicIdAndLanuage(Map<String, Object> params);

    List<Topic> findTopicsByTopicCategoryId(Map<String, Object> params);

    Topic findOne(Map<String, Object> params);

    Topic findOneByCode(Map<String, Object> params);

    int deleteTopic(Map<String, Object> params);


}
